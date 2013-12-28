package pl.edu.agh.tai.components;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Upload.FinishedListener;

import pl.edu.agh.tai.components.utils.DropboxUploadListener;
import pl.edu.agh.tai.utils.DropboxConfig;

/**
 * A component that provides UI for dropbox files upload.
 */
@SuppressWarnings("serial")
public class DropboxUpload extends CustomComponent {

    protected final static String DEFAULT_DESTINATION_PATH = "/";
    protected final static String DESTINATION_PATH_CAPTION = "Dropbox path to upload a file:";
    protected final static String UPLOAD_MESSAGE = "Upload a file";
    protected final static String STYLE_UPLOAD = "upload";

    protected DropboxConfig dropboxConfig;

    protected Label destinationDirectory;
    protected Upload uploadManager;
    protected DropboxUploadListener uploadListener;

    protected ObjectProperty<String> selectedDropboxPathProperty;

    /**
     * Constructor
     *
     * @param dropboxConfig Drobox client configuration
     */
    public DropboxUpload(DropboxConfig dropboxConfig) {
        this.dropboxConfig = dropboxConfig;
        this.selectedDropboxPathProperty = new ObjectProperty<String>(DEFAULT_DESTINATION_PATH, String.class);

		this.setStyleName(STYLE_UPLOAD);
        uploadManager = new Upload();
        uploadManager.setCaption(UPLOAD_MESSAGE);
        uploadListener = new DropboxUploadListener(uploadManager, dropboxConfig, selectedDropboxPathProperty);
        uploadManager.setReceiver(uploadListener);
        uploadManager.addSucceededListener(uploadListener);
        uploadManager.addFinishedListener(uploadListener);

        this.destinationDirectory = new Label();
        this.destinationDirectory.setCaption(DESTINATION_PATH_CAPTION);
        this.destinationDirectory.setPropertyDataSource(selectedDropboxPathProperty);
        this.destinationDirectory.setImmediate(true);

        this.setCompositionRoot(new VerticalLayout(this.uploadManager, this.destinationDirectory));
    }
    
    /***
     * Adds listener for uploading finished event.
     * 
     * @param finishedListener
     */
    public void addFinishedListener(FinishedListener finishedListener){
    	this.uploadManager.addFinishedListener(finishedListener);
    }

    /**
     * Getter
     *
     * @return DataModel for Dropbox upload path
     */
    public ObjectProperty<String> getDropboxPathPropertySetter() {
        return selectedDropboxPathProperty;
    }

    /**
     * Sets widget to default configuration
     */
    public void udpate() {
        uploadListener.reset();
        selectedDropboxPathProperty.setValue(DEFAULT_DESTINATION_PATH);
    }
}
