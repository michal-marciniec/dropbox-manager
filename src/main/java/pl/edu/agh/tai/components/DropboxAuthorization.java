package pl.edu.agh.tai.components;

import pl.edu.agh.tai.security.AuthorizationManager;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DropboxAuthorization extends CustomComponent {

	protected static final String STYLE_VERTICAL_LAYOUT = "form-verticallayout";
	protected static final String STYLE_CODE_LABEL = "auth-codelabel";
	protected static final String STYLE_PASTE_LABEL = "form-label";
	protected static final String STYLE_TEXTFIELD = "auth-textfield";
	protected static final String STYLE_BUTTON = "button";
	protected static final String STYLE_TITLE = "title";
	
	protected static final String TITLE = "Dropbox Authorization";
	protected static final String OK = "OK";
	protected static final String PASTE_CODE = "Paste code here:";
	protected static final String CLICK_LINK = "Click on this link to receive Dropbox authorization code:";
	protected static final String LINK = "link";
	
	protected VerticalLayout content;
	protected Button authorizeButton;
	protected TextField codeField;

	protected AuthorizationManager authorizationManager;

	public DropboxAuthorization(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
		
		Label titleLabel = new Label(TITLE);
		titleLabel.setStyleName(STYLE_TITLE);
		this.codeField = new TextField();
		this.codeField.setStyleName(STYLE_TEXTFIELD);
		this.codeField.addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				authorizeButton.click();
			}
		});
		this.authorizeButton = new Button(OK);
		this.authorizeButton.setStyleName(STYLE_BUTTON);
		Label pasteLabel = new Label(PASTE_CODE);
		pasteLabel.setStyleName(STYLE_PASTE_LABEL);
		Label codeLabel = new Label(CLICK_LINK);
		codeLabel.setStyleName(STYLE_CODE_LABEL);
		Link link = new Link(LINK, new ExternalResource(authorizationManager.getAuthorizationURL()));
		this.content = new VerticalLayout(
				titleLabel,
				new HorizontalLayout(codeLabel, link), 
				new HorizontalLayout(pasteLabel, this.codeField), 
				this.authorizeButton);
		this.content.setStyleName(STYLE_VERTICAL_LAYOUT);
		this.setCompositionRoot(this.content);
	}

	/***
	 * Adds ClickListener to the authorize button.
	 * 
	 * @param clickListener
	 */
	public void addClickListener(ClickListener clickListener) {
		authorizeButton.addClickListener(clickListener);
	}

	/***
	 * Fetches Dropbox authorization code from input box.
	 * 
	 * @return Dropbox authorization code
	 */
	public String getAuthorizationCode() {
		return codeField.getValue();
	}
	
	/***
	 * Clears value in code input box.
	 *  
	 */
	public void resetInput(){
		codeField.setValue("");
	}

}
