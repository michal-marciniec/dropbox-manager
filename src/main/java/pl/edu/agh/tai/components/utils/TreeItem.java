package pl.edu.agh.tai.components.utils;

import com.dropbox.core.DbxEntry;

public class TreeItem {
	
	protected String name;
	protected String path;
	protected boolean isFolder;

	public TreeItem(DbxEntry dbxEntry) {
		this.name = dbxEntry.name;
		this.path = dbxEntry.path;
		this.isFolder = dbxEntry.isFolder();
	}
	
	/***
	 * Fetches full filepath.
	 * 
	 * @return Filepath
	 */
	public String getPath(){
		return path;
	}
	
	/***
	 * Returns value indicating if item is a folder.
	 * 
	 * @return true if node is a folder, false otherwise
	 */
	public boolean isFolder(){
		return isFolder;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
