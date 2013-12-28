package pl.edu.agh.tai;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import pl.edu.agh.tai.navigation.Dispatcher;
import pl.edu.agh.tai.security.AuthorizationManager;
import pl.edu.agh.tai.utils.DropboxConfig;
import pl.edu.agh.tai.views.AuthView;
import pl.edu.agh.tai.views.HomeView;
import pl.edu.agh.tai.views.LoginView;
import pl.edu.agh.tai.views.TAIView;

import javax.servlet.annotation.WebServlet;
import java.util.HashMap;
import java.util.Map;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

    protected Map<String, TAIView> views;

    public MyVaadinUI() {
        this.views = new HashMap<String, TAIView>();
    }

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "pl.edu.agh.tai.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        DropboxConfig dropboxConfig = new DropboxConfig();
        AuthorizationManager authorizationManager = new AuthorizationManager(dropboxConfig);
        Navigator navigator = new Navigator(this, this);
        Dispatcher dispatcher = new Dispatcher(navigator);

        views.put(LoginView.URL, new LoginView(dispatcher));
        views.put(HomeView.URL, new HomeView(dispatcher, dropboxConfig));
        views.put(AuthView.URL, new AuthView(dispatcher, authorizationManager));

        for (String subpage : views.keySet()) {
            navigator.addView(subpage, views.get(subpage));
        }
    }

}
