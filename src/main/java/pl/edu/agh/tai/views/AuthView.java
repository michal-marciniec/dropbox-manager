package pl.edu.agh.tai.views;

import pl.edu.agh.tai.components.DropboxAuthorization;
import pl.edu.agh.tai.navigation.Dispatcher;
import pl.edu.agh.tai.security.AuthorizationManager;
import pl.edu.agh.tai.security.User;

import com.dropbox.core.DbxException;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class AuthView extends TAIView {

	protected static String[] requiredPermissions = { "authenticated:view" };
	public static final String URL = "auth";

	protected AuthorizationManager authorizationManager;
	protected DropboxAuthorization dropboxAuthorization;

	public AuthView(final Dispatcher dispatcher, final AuthorizationManager authorizationManager) {
		super(dispatcher);
		this.authorizationManager = authorizationManager;

		this.dropboxAuthorization = new DropboxAuthorization(authorizationManager);
		this.dropboxAuthorization.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				try {
					authorizationManager.authorize(dropboxAuthorization.getAuthorizationCode());
					dispatcher.navigateToHome();
				} catch (DbxException e) {
					errorBox.setMessage(e.getMessage());
				}
			}
		});
		this.addComponent(this.dropboxAuthorization);
	}

	@Override
	public String[] getRequiredPermissions() {
		return requiredPermissions;
	}

	public void enter(ViewChangeEvent event) {
		super.enter(event);

		dropboxAuthorization.resetInput();

		if (!User.isAuthenticated()) {
			dispatcher.navigateToLogin();
		} else if (isUserPermitted()) {
			if (User.isAuthorized()) {
				dispatcher.navigateToHome();
				return;
			}
		}
	}

}
