package pl.edu.agh.tai.navigation;

import java.io.Serializable;

import pl.edu.agh.tai.views.AuthView;
import pl.edu.agh.tai.views.HomeView;
import pl.edu.agh.tai.views.LoginView;

import com.vaadin.navigator.Navigator;

@SuppressWarnings("serial")
public class Dispatcher implements Serializable {

	private Navigator navigator;

	public Dispatcher(Navigator navigator) {
		this.navigator = navigator;
	}

	/***
	 * Redirects to specified url.
	 * 
	 * @param url
	 *            Url to redirect to
	 */
	public void navigateTo(String url) {
		navigator.navigateTo(url);
	}

	/***
	 * Redirects to home subpage.
	 * 
	 */
	public void navigateToHome() {
		navigateTo(HomeView.URL);
	}

	/***
	 * Redirects to login subpage.
	 * 
	 */
	public void navigateToLogin() {
		navigateTo(LoginView.URL);
	}

	/***
	 * Redirects to Dropbox authorization subpage.
	 * 
	 */
	public void navigateToAuth() {
		navigateTo(AuthView.URL);
	}

}
