package pl.edu.agh.tai.views;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

import pl.edu.agh.tai.components.ErrorBox;
import pl.edu.agh.tai.components.Header;
import pl.edu.agh.tai.components.LoginBox;
import pl.edu.agh.tai.navigation.Dispatcher;
import pl.edu.agh.tai.security.User;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class LoginView extends TAIView {

	protected static String[] requiredPermissions = {};
	public static final String URL = "";

	protected Header header;
	protected ErrorBox errorBox;
	protected LoginBox loginBox;

	public LoginView(Dispatcher dispatcher) {
		super(dispatcher);
	
		this.loginBox = new LoginBox();
		this.loginBox.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				authenticate();
			}
		});
		this.addComponent(this.loginBox);

		this.errorBox = new ErrorBox();
		this.addComponent(this.errorBox);
	}

	@Override
	public String[] getRequiredPermissions() {
		return requiredPermissions;
	}

	public void enter(ViewChangeEvent event) {
		super.enter(event);
		
		if (isUserPermitted()) {
			if (User.isAuthenticated()) {
				dispatcher.navigateToAuth();
			}
			errorBox.clearMessage();
		}
	}

	/**
	 * Fetches login & password values from the form input boxes and tries to
	 * log user in. On success redirects to Auth subpage, displays exception
	 * otherwise.
	 */
	protected void authenticate() {
		try {
			User.login(loginBox.getLogin(), loginBox.getPassword());
			dispatcher.navigateToAuth();
		} catch (UnknownAccountException uae) {
			errorBox.setMessage("username wasn't in the system");
		} catch (IncorrectCredentialsException ice) {
			errorBox.setMessage("password didn't match, try again");
		} catch (LockedAccountException lae) {
			errorBox.setMessage("account for that username is locked - can't login");
		} catch (AuthenticationException ae) {
			errorBox.setMessage("unexpected error occured");
		}
	}
}
