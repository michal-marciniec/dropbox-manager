package pl.edu.agh.tai.components;

import java.util.Collection;

import pl.edu.agh.tai.components.utils.TreeItem;
import pl.edu.agh.tai.security.User;
import pl.edu.agh.tai.utils.DropboxConfig;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxEntry.WithChildren;
import com.dropbox.core.DbxException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DropboxBrowser extends CustomComponent implements FinishedListener {

	protected final static String STYLE_TREE = "tree";
	protected final static String ROOT_SEP = "/";
	protected final static String REFRESH = "Refresh";

	protected ObjectProperty<String> dropboxPathProperty;
	protected DropboxConfig dropboxConfig;

	protected Tree tree;
	protected ErrorBox errorBox;
	protected Button refreshButton;

	public DropboxBrowser(DropboxConfig dropboxConfig, ObjectProperty<String> dropboxPathProperty) {
		this.dropboxPathProperty = dropboxPathProperty;
		this.dropboxConfig = dropboxConfig;

		this.refreshButton = new Button(REFRESH);
		this.refreshButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				refresh();
			}
		});
		this.errorBox = new ErrorBox();
		this.tree = new Tree();

		this.setStyleName(STYLE_TREE);
		this.setCompositionRoot(new VerticalLayout(this.refreshButton, this.tree, this.errorBox));
	}

	@Override
	public void uploadFinished(FinishedEvent event) {
		refresh();
	}

	/***
	 * Refreshes component's content.
	 * 
	 */
	public void update() {
		tree.removeAllItems();
		errorBox.clearMessage();

		try {
			buildTree();
		} catch (DbxException e) {
			errorBox.setMessage(e.getMessage());
		}
	}

	protected void refresh() {

		for (Object itemId : tree.getItemIds()) {
			try {
				removeNonExistingFiles(itemId);
			} catch (DbxException e) {
				errorBox.setMessage(e.getMessage());
			}
		}

		for (Object itemId : tree.getItemIds()) {
			if (tree.isExpanded(itemId)) {
				try {
					updateNewFiles(itemId);
				} catch (DbxException e) {
					errorBox.setMessage(e.getMessage());
				}
			}
		}

	}

	protected void removeNonExistingFiles(Object itemId) throws DbxException {
		TreeItem item = (TreeItem) itemId;
		final DbxClient client = new DbxClient(dropboxConfig.getConfig(), User.getAccessToken());
		DbxEntry entry = client.getMetadata(item.getPath());
		if (entry == null) {
			tree.removeItem(itemId);
		}
	}

	protected void updateNewFiles(Object itemId) throws DbxException {
		TreeItem item = (TreeItem) itemId;
		final DbxClient client = new DbxClient(dropboxConfig.getConfig(), User.getAccessToken());

		WithChildren children = client.getMetadataWithChildren(item.getPath());
		for (DbxEntry child : children.children) {
			if (!containsItem(itemId, child)) {
				TreeItem newItem = new TreeItem(child);
				tree.addItem(newItem);
				tree.setChildrenAllowed(newItem, child.isFolder());
				tree.setParent(newItem, item);
			}
		}
	}

	protected boolean containsItem(Object parentId, DbxEntry child) {
		for (Object itemId : tree.getChildren(parentId)) {
			TreeItem item = (TreeItem) itemId;
			if (item.getPath().equals(child.path)) {
				return true;
			}
		}
		return false;
	}

	protected void buildTree() throws DbxException {
		final DbxClient client = new DbxClient(dropboxConfig.getConfig(), User.getAccessToken());

		WithChildren children = client.getMetadataWithChildren(ROOT_SEP);
		if (children == null || children.children == null || children.children.isEmpty()) {
			return;
		}

		for (DbxEntry child : children.children) {
			TreeItem treeItem = new TreeItem(child);
			tree.addItem(treeItem);
			tree.setChildrenAllowed(treeItem, child.isFolder());
		}

		tree.addExpandListener(new Tree.ExpandListener() {
			@Override
			public void nodeExpand(ExpandEvent event) {
				TreeItem treeItem = (TreeItem) event.getItemId();
				try {
					WithChildren children = client.getMetadataWithChildren(treeItem.getPath());
					if (children == null || children.children == null || children.children.isEmpty()) {
						tree.setChildrenAllowed(treeItem, false);
					} else {
						for (DbxEntry child : children.children) {
							TreeItem item = new TreeItem(child);
							tree.addItem(item);
							tree.setParent(item, treeItem);
							tree.setChildrenAllowed(item, child.isFolder());
						}
					}
				} catch (DbxException e) {
					errorBox.setMessage(e.getMessage());
				}
			}
		});

		tree.addCollapseListener(new Tree.CollapseListener() {
			@Override
			public void nodeCollapse(CollapseEvent event) {
				TreeItem treeItem = (TreeItem) event.getItemId();
				removeItemRecursively(treeItem);
			}
		});

		tree.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent itemClickEvent) {
				TreeItem clickedItem = (TreeItem) itemClickEvent.getItemId();
				TreeItem parentDirectory = getParentDirectoryOf(clickedItem);
				dropboxPathProperty.setValue(parentDirectory == null ? ROOT_SEP : parentDirectory.getPath() + ROOT_SEP);
			}
		});
	}

	protected TreeItem getParentDirectoryOf(TreeItem item) {
		return item.isFolder() ? item : (TreeItem) tree.getParent(item);
	}

	protected void removeItemRecursively(TreeItem treeItem) {
		Collection<?> children = tree.getChildren(treeItem);
		if (children == null)
			return;
		Object childrenArray[] = children.toArray();
		for (Object child : childrenArray) {
			TreeItem item = (TreeItem) child;
			removeItemRecursively(item);
			tree.removeItem(item);
		}
	}

}
