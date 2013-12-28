package pl.edu.agh.tai.security;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;

import pl.edu.agh.tai.utils.DropboxConfig;

public class AuthorizationManager {

    protected DropboxConfig dropboxConfig;

    public AuthorizationManager(DropboxConfig dropboxConfig) {
        this.dropboxConfig = dropboxConfig;
    }

    /***
     * Constructs URL for Dropbox authorization.
     * 
     * @return URL for Dropbox authorization
     */
    public String getAuthorizationURL() {
        return dropboxConfig.getWebAuth().start();
    }

    /***
     * Attempts to authorize in Dropbox. Sets Dropbox token in user's session on success.
     * 
     * @param code Dropbox authorization code
     * @throws DbxException
     */
    public void authorize(String code) throws DbxException {
        DbxAuthFinish authFinish = dropboxConfig.getWebAuth().finish(code);
        User.setAccessToken(authFinish.accessToken);
    }
}
