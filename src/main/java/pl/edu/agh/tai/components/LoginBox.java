package pl.edu.agh.tai.components;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class LoginBox extends CustomComponent {

	protected static final String STYLE_VERTICAL_LAYOUT = "form-verticallayout";
	protected static final String STYLE_LABEL = "form-label";
	protected static final String STYLE_TEXTFIELD = "login-textfield";
	protected static final String STYLE_BUTTON = "button";
	protected static final String STYLE_TITLE = "title";
	
	protected static final String TITLE = "Sign In";
	protected static final String LOGIN = "Login:";
	protected static final String PASSWORD = "Password:";
	

	protected VerticalLayout content;
	protected TextField loginField;
	protected PasswordField passwordField;
	protected Button loginButton;

	public LoginBox() {
		Label titleLabel = new Label(TITLE);
		titleLabel.setStyleName(STYLE_TITLE);
		this.loginField = new TextField();
		this.loginField.setStyleName(STYLE_TEXTFIELD);
		this.passwordField = new PasswordField();
		this.passwordField.setStyleName(STYLE_TEXTFIELD);
		this.passwordField.addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				loginButton.click();
			}
		});
		this.loginButton = new Button(TITLE);
		this.loginButton.setStyleName(STYLE_BUTTON);
		Label loginLabel = new Label(LOGIN);
		loginLabel.setStyleName(STYLE_LABEL);
		Label passwordLabel = new Label(PASSWORD);
		passwordLabel.setStyleName(STYLE_LABEL);
		this.content = new VerticalLayout(
				titleLabel,
				new HorizontalLayout(
						new VerticalLayout(loginLabel, passwordLabel), 
						new VerticalLayout(loginField, passwordField)), 
				this.loginButton);
		this.content.setStyleName(STYLE_VERTICAL_LAYOUT);
		this.setCompositionRoot(this.content);
	}

	/***
	 * Fetches value from login input box.
	 * 
	 * @return Value in login input box.
	 */
	public String getLogin() {
		return loginField.getValue();
	}

	/***
	 * Fetches value from password input box.
	 * 
	 * @return Value in password input box.
	 */
	public String getPassword() {
		return passwordField.getValue();
	}

	/***
	 * Adds ClickListener to the sign-in button.
	 * 
	 * @param clickListener
	 */
	public void addClickListener(ClickListener clickListener) {
		this.loginButton.addClickListener(clickListener);
	}

}
