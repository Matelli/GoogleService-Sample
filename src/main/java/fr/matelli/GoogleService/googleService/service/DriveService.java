package fr.matelli.GoogleService.googleService.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.oauth2.Oauth2Scopes;
import fr.matelli.GoogleService.googleService.GoogleAuthHelper;
import fr.matelli.GoogleService.googleService.utils.MimeUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe permettant d'interagir avec les services Google Drive
 *
 * @author <a href="http://www.matelli.fr">Matelli</a>
 * @see fr.matelli.GoogleService.googleService.GoogleAuthHelper
 * @see <a href"https://developers.google.com/drive/v2/reference/>https://developers.google.com/drive/v2/reference/</a>
 */
public class DriveService extends GoogleAuthHelper {

    {
        // Liste des autorisations
        this.scopes.addAll(Arrays.asList(DriveScopes.DRIVE));
    }

    private Drive serviceDrive = null;

    protected Logger logger = Logger.getLogger(DriveService.class);

    public DriveService(String redirectUri) throws Exception {
        super(redirectUri);
    }

    /**
     * Create a new authorized API Client
     *
     * @param credential
     * @return DriveService Service
     */
    private Drive createServiceDrive(Credential credential) {
        if (serviceDrive == null) {
            serviceDrive = new Drive.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(applicationName)
                    .build();
        }
        return serviceDrive;
    }

    /**
     * Insert new folder
     *
     * @param credential
     * @param nameFolder
     * @param useExisting
     * @return Inserted folder metadata if successful, {@code null} otherwise.
     * @throws java.io.IOException
     */
    public File insertFolder(Credential credential, String nameFolder, boolean useExisting) throws IOException {
        createServiceDrive(credential);
        // Desactiver searchFile si useExisting is false
        FileList searchFile = searchFile(credential, "application/vnd.google-apps.folder", nameFolder);
        if(searchFile.getItems().size() == 0 || !useExisting) {
            // File's content
            File body = new File();
            body.setTitle(nameFolder);
            body.setMimeType("application/vnd.google-apps.folder");
            com.google.api.services.drive.Drive.Files.Insert file = serviceDrive.files().insert(body);
            File result = file.execute();
            logger.info("Folder ID: " + result.getId());
            return result;
        } else {
            File file = searchFile.getItems().get(0);
            logger.info("Old Folder ID: " + file.getId());
            return file;
        }
    }

    /**
     * Recherche un fichier selon le mimeType (recherche aussi dans la corbeille)
     *
     * @param credential
     * @param mimeType
     * @param title
     * @return FileList
     * @throws IOException
     */
    public FileList searchFile(Credential credential, String mimeType, String title) throws IOException {
        Drive serviceDrive = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName).build();
        Drive.Files.List request = serviceDrive.files().list().setQ(
                "mimeType='" + mimeType + "' and trashed=false and title='" +  title + "'");
        return request.execute();
    }

    /**
     * Print a file's metadata
     *
     * @param credential
     * @param fileId
     * @throws java.io.IOException
     */
    public void printFile(Credential credential, String fileId) throws IOException {
        createServiceDrive(credential);
        File file = serviceDrive.files().get(fileId).execute();

        logger.info("Title: " + file.getTitle());
        logger.info("Description: " + file.getDescription());
        logger.info("MIME type: " + file.getMimeType());
    }

    /**
     * Download a file's content
     *
     * @param credential
     * @param fileId
     * @return InputStream containing the file's content if successful,
     *         {@code null} otherwise.
     * @throws java.io.IOException
     */
    public File downloadFile(Credential credential, String fileId) throws IOException {
        createServiceDrive(credential);
        File file = serviceDrive.files().get(fileId).execute();
        logger.info("downloadFile() ExportLinks : " + file.getExportLinks().get(MimeUtils.hasExtension("pdf")));
        return file;
    }

    /**
     * Retrieve a list of File resources.
     *
     * @param credential
     * @return List of File resources.
     * @see "https://developers.google.com/drive/v2/reference/files/list"
     */
    public List<File> retrieveAllFiles(Credential credential) throws IOException {
        createServiceDrive(credential);
        List<File> result = new ArrayList<File>();
        Drive.Files.List request = serviceDrive.files().list();

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);

        return result;
    }
}
