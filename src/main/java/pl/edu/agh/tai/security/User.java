package pl.edu.agh.tai.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class User {

	protected static final String SESSION_AUTHORIZED = "authorized";
	protected static final String SESSION_TOKEN = "token";

	/**
	 * Fetches current user's name.
	 * 
	 * @return Current user's name.
	 */
	public static String getUsername() {
		Subject currentUser = SecurityUtils.getSubject();
		return currentUser.isAuthenticated() ? currentUser.getPrincipal().toString() : null;
	}

    /**
     * Attempts to log current user in. On success stores information that user
     * is unauthorized in session storage.
     *
     * @param login
     *            Username
     * @param password
     *            Password
     */
    public static void login(String login, String password) {
        Subject currentUser = SecurityUtils.getSubject();
        if (!isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(login, password);
            token.setRememberMe(true);
            currentUser.login(token);
            currentUser.getSession().setAttribute(SESSION_AUTHORIZED, false);
        }
    }

    /**
     * Logs current user out.
     */
    public static void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }

    /**
     * Checks if current user is logged in.
     *
     * @return True if user is logged in, otherwise false
     */
    public static boolean isAuthenticated() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isAuthenticated();
    }

    /**
     * Checks if current user is authorized in Dropbox application.
     *
     * @return True if user is authorized, otherwise false
     */
    public static boolean isAuthorized() {
        if (!isAuthenticated()) {
            return false;
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Object attribute = session.getAttribute(SESSION_AUTHORIZED);
        return attribute == null ? false : (Boolean) attribute;
    }

	/**
	 * Sets Dropbox token in current user's session storage.
	 * 
	 * @param token
	 *            Dropbox authorization token
	 */
	public static void setAccessToken(String token) {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.getSession().setAttribute(SESSION_TOKEN, token);
		currentUser.getSession().setAttribute(SESSION_AUTHORIZED, true);
	}

	/**
	 * Fetches Dropbox token stored in current user's session storage.
	 * 
	 * @return Current user's Dropbox authorization token
	 */
	public static String getAccessToken() {
		Subject currentUser = SecurityUtils.getSubject();
		return (String) currentUser.getSession().getAttribute(SESSION_TOKEN);
	}

	public static boolean isPermitted(String permission) {
		Subject currentUser = SecurityUtils.getSubject();
		return currentUser.isPermitted(permission);
	}

}
