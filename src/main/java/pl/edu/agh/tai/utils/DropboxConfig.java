package pl.edu.agh.tai.utils;

import java.io.Serializable;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

@SuppressWarnings("serial")
public class DropboxConfig implements Serializable {

	protected static final String APP_KEY = "wnnvhpwhn44oqse";
	protected static final String APP_SECRET = "zi3vk1bx1u544bz";
	protected static final String APP_NAME = "TAIprojekt2013";

	protected DbxWebAuthNoRedirect webAuth;
	protected DbxRequestConfig config;
	protected DbxAppInfo appInfo;

	public DropboxConfig() {
		this.appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		this.config = new DbxRequestConfig(APP_NAME, Locale.getDefault().toString());
		this.webAuth = new DbxWebAuthNoRedirect(this.config, this.appInfo);
	}

	/***
	 * Fetches Dropbox WebAuth object.
	 * @return Dropbox authorization object
	 */
	public DbxWebAuthNoRedirect getWebAuth() {
		return webAuth;
	}
	
	/***
	 * Fetches Dropbox config.
	 * @return Dropbox configuration object
	 */
	public DbxRequestConfig getConfig(){
		return config;
	}

}
