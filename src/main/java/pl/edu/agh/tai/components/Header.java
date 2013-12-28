package pl.edu.agh.tai.components;

import pl.edu.agh.tai.navigation.Dispatcher;
import pl.edu.agh.tai.security.User;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class Header extends CustomComponent {

	protected static final String STYLE_USER_BAR = "userbar-horizontallayout";
	protected static final String STYLE_RIGHT_PANEL = "userbar-rightpanel";
	protected static final String STYLE_USER_LABEL = "user-label";
	protected static final String STYLE_BUTTON = "button";
	protected static final String STYLE_TITLE = "userbar-title";
	
	protected static final String LOGOUT = "Logout";
	protected static final String APP_NAME = "TAI-Dropbox-Pro";
	protected static final String NOT_LOGGED_IN = "not logged in";

	protected Dispatcher dispatcher;

	protected HorizontalLayout content;
	protected Label userLabel;
	protected Button logoutButton;

	public Header(final Dispatcher dispatcher) {
		this.userLabel = new Label();
		this.userLabel.setStyleName(STYLE_USER_LABEL);

		this.logoutButton = new Button(LOGOUT);
		this.logoutButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				User.logout();
				dispatcher.navigateToLogin();
			}
		});
		this.logoutButton.setStyleName(STYLE_BUTTON);
		this.logoutButton.setVisible(false);
		
		HorizontalLayout rightPanel = new HorizontalLayout(userLabel, logoutButton);
		rightPanel.setStyleName(STYLE_RIGHT_PANEL);

		Label titleLabel = new Label(APP_NAME);
		titleLabel.setStyleName(STYLE_TITLE);
		titleLabel.setSizeFull();

		this.content = new HorizontalLayout(titleLabel, rightPanel);
		this.content.setStyleName(STYLE_USER_BAR);
		this.setCompositionRoot(this.content);
	}
	
	/***
	 * Refreshes component's content.
	 * 
	 */
	public void update(){
		String userName = User.getUsername();
		userLabel.setValue(userName == null ? NOT_LOGGED_IN : userName);
		logoutButton.setVisible(userName != null);
	}

}
