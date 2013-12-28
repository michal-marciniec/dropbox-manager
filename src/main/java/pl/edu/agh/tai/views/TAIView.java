package pl.edu.agh.tai.views;

import org.apache.shiro.SecurityUtils;

import pl.edu.agh.tai.components.ErrorBox;
import pl.edu.agh.tai.components.Header;
import pl.edu.agh.tai.navigation.Dispatcher;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public abstract class TAIView extends VerticalLayout implements View {

    protected Dispatcher dispatcher;
    protected String url;
    protected Header header;
    protected ErrorBox errorBox;

    public TAIView(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        
		this.header = new Header(dispatcher);
		addComponent(this.header);
		
		this.errorBox = new ErrorBox();
        addComponent(this.errorBox);
    }

    protected boolean isUserPermitted() {
        String[] permissions = getRequiredPermissions();
        if (permissions == null || permissions.length == 0)
            return true;

        if (!SecurityUtils.getSubject().isPermittedAll(permissions)) {
            dispatcher.navigateToLogin();
            return false;
        }
        return true;
    }
    
    public void enter(ViewChangeEvent event){
    	header.update();
    }

    public abstract String[] getRequiredPermissions();
}
