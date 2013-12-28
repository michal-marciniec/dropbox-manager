package pl.edu.agh.tai.views;

import pl.edu.agh.tai.components.DropboxBrowser;
import pl.edu.agh.tai.components.DropboxUpload;
import pl.edu.agh.tai.navigation.Dispatcher;
import pl.edu.agh.tai.security.User;
import pl.edu.agh.tai.utils.DropboxConfig;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.GridLayout;

@SuppressWarnings("serial")
public class HomeView extends TAIView {

	protected static String[] requiredPermissions = { "dropbox:view" };
	protected static String dropboxUploadPermission = "dropbox:upload";
	public static final String URL = "home";

	protected DropboxConfig dropboxConfig;

	protected DropboxBrowser dropboxBrowser;
	protected DropboxUpload dropboxUpload;

	public HomeView(Dispatcher dispatcher, DropboxConfig dropboxConfig) {
		super(dispatcher);
		this.dropboxConfig = dropboxConfig;

		this.dropboxUpload = new DropboxUpload(dropboxConfig);
		this.dropboxBrowser = new DropboxBrowser(dropboxConfig, dropboxUpload.getDropboxPathPropertySetter());
		this.dropboxUpload.addFinishedListener(this.dropboxBrowser);

		GridLayout gridLayout = new GridLayout(2, 1, dropboxBrowser, dropboxUpload);
		for (int col = 0; col < gridLayout.getColumns(); ++col) {
			gridLayout.setColumnExpandRatio(col, 0.0f);
		}
		gridLayout.setSizeFull();
		this.addComponent(gridLayout);
	}

	@Override
	public String[] getRequiredPermissions() {
		return requiredPermissions;
	}

	public void enter(ViewChangeEvent event) {
		super.enter(event);

		if (isUserPermitted()) {
			if (!User.isAuthorized()) {
				dispatcher.navigateToAuth();
				return;
			}

			boolean isPermitted = User.isPermitted(dropboxUploadPermission);
			dropboxUpload.setVisible(isPermitted);

			dropboxBrowser.update();
			dropboxUpload.udpate();
		}
	}

}
