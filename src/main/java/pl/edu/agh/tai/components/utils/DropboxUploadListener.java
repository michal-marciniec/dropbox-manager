package pl.edu.agh.tai.components.utils;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Upload;

import pl.edu.agh.tai.security.User;
import pl.edu.agh.tai.utils.DropboxConfig;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Uploading listener. Handles start and finishing of uploading process
 * Fristly, uploading is done into internal output stream. After successful fetching data into stream, data is being sent to specified dropbox path
 */
@SuppressWarnings("serial")
public class DropboxUploadListener implements Upload.Receiver, Upload.SucceededListener, Upload.FinishedListener {

    protected final Upload uploadManager;
    protected final DropboxConfig dropboxConfig;
    protected String uploadPath;
    protected ObjectProperty<String> uploadPathProperty;
    protected ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();

    public DropboxUploadListener(Upload uploadManager, DropboxConfig dropboxConfig, ObjectProperty<String> uploadPathProperty) {
        this.uploadManager = uploadManager;
        this.dropboxConfig = dropboxConfig;
        this.uploadPathProperty = uploadPathProperty;
    }

    /**
     * Resets temporary output stream
     */
    public void reset() {
        memoryStream.reset();
    }

    /**
     * Receiver.Upload interface implementation
     *
     * @param filename Name of the file to be uploaded
     * @param mimeType MIME type
     * @return listener's temporary output stream
     */
    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        // HELP
        uploadManager.setEnabled(false);
        uploadPath = uploadPathProperty.getValue();
        memoryStream.reset();
        return memoryStream;
    }

    /**
     * Upload.SucceededListener interface implementation
     * Invoked after successful saving data in the temporary stream and uploads retrieved data to dropbox
     *
     * @param succeededEvent event (contains uploaded file's name etc.)
     */
    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
        // HELP
        uploadManager.setEnabled(false);
        byte[] bytesToUpload = memoryStream.toByteArray();
        if (bytesToUpload.length == 0) bytesToUpload = new byte[]{0};
        String destinationPath = uploadPath + succeededEvent.getFilename();

        final DbxClient client = new DbxClient(dropboxConfig.getConfig(), User.getAccessToken());
        try {
            String uploadingID = client.chunkedUploadFirst(bytesToUpload);
            client.chunkedUploadFinish(destinationPath, DbxWriteMode.add(), uploadingID);
            reset();
        } catch (DbxException e) {
            e.printStackTrace();
        } finally {
            // HELP
            uploadManager.setEnabled(true);
        }
    }

    @Override
    public void uploadFinished(Upload.FinishedEvent finishedEvent) {
        // HELP
        uploadManager.setEnabled(true);
    }
}