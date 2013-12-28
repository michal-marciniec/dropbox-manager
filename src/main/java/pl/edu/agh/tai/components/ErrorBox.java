package pl.edu.agh.tai.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class ErrorBox extends CustomComponent {

	protected static final String STYLE_HORIZONTAL_LAYOUT = "error-horizontallayout";
	protected static final String STYLE_LABEL = "error-label";

	protected HorizontalLayout content;
	protected Label errorLabel;

	public ErrorBox() {
		this.errorLabel = new Label();
		this.errorLabel.setStyleName(STYLE_LABEL);
		this.content = new HorizontalLayout(this.errorLabel);
		this.content.setStyleName(STYLE_HORIZONTAL_LAYOUT);
		this.setCompositionRoot(this.content);
	}

	/***
	 * Sets error message.
	 * 
	 * @param message Message to display
	 */
	public void setMessage(String message) {
		String caption = new String(message);
		if (caption.length() > 0) {
			Character first = Character.toUpperCase(caption.charAt(0));
			caption = first + message.substring(1);
		}
		this.errorLabel.setCaption(caption);
	}

	/***
	 * Removes error message from the screen.
	 * 
	 */
	public void clearMessage() {
		this.errorLabel.setCaption("");
	}

}
