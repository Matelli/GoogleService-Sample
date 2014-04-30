package fr.matelli.GoogleService.googleService.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import fr.matelli.GoogleService.googleService.GoogleAuthHelper;
import fr.matelli.GoogleService.googleService.utils.MimeUtils;
import org.apache.log4j.Logger;

import java.io.*;
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
     * Insertion d'un nouveau dossier
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/insert
     *
     * @param credential
     * @param nameFolder Nom du dossier
     * @param useExisting Si true, recherche un dossier qui possede le meme nameFolder et retourne la 1ere occurance
     * @return Les métadonnées du dossier créer en cas de succès, sinon {@code null}
     * @throws java.io.IOException
     */
    public File insertFolder(Credential credential, String nameFolder, boolean useExisting) throws IOException {
        createServiceDrive(credential);
        if(!useExisting) {
            // File's content
            File body = new File();
            body.setTitle(nameFolder);
            body.setMimeType("application/vnd.google-apps.folder");
            com.google.api.services.drive.Drive.Files.Insert file = serviceDrive.files().insert(body);
            File result = file.execute();
            logger.debug("Folder ID: " + result.getId());
            return result;
        } else {
            List<File> listFile = searchFile(credential, "application/vnd.google-apps.folder", nameFolder, true).getItems();
            if (listFile.isEmpty()) {
                // Relance la méthode afin de crée le dossier
                return this.insertFolder(credential, nameFolder, false);
            } else {
                logger.debug("Old Folder ID: " + listFile.get(0).getId());
                return listFile.get(0);
            }
        }
    }

    /**
     * Recherche un fichier selon le mimeType (recherche aussi dans la corbeille)
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/list
     *
     * @param credential
     * @param mimeType
     * @param title
     * @param trashed Si true alors recherche aussi dans la corbeille
     * @return FileList
     * @throws IOException
     */
    public FileList searchFile(Credential credential, String mimeType, String title, boolean trashed) throws IOException {
        Drive serviceDrive = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName).build();
        Drive.Files.List request = serviceDrive.files().list().setQ(
                "mimeType='" + mimeType + "' and trashed=" + trashed + " and title='" +  title + "'");
        return request.execute();
    }

    /**
     * Retourne les métadonnées du fichier
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/get
     *
     * @param credential
     * @param fileId
     * @return InputStream contenant le contenu du fichier en cas de succès, sinon {@code null}
     * @throws java.io.IOException
     */
    public File getFile(Credential credential, String fileId) throws IOException {
        createServiceDrive(credential);
        return serviceDrive.files().get(fileId).execute();
    }

    /**
     * Récupère une liste de tous les fichiers
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/list
     *
     * @param credential
     * @param maxResults Nombre maximum de fichiers à retourner. Les valeurs acceptables sont 0 à 1000 (Défaut 100)
     * @param q Paramètre https://developers.google.com/drive/web/search-parameters
     * @return List of File resources.
     */
    public List<File> retrieveAllFiles(Credential credential, Integer maxResults, String q) throws IOException {
        createServiceDrive(credential);
        List<File> result = new ArrayList<File>();
        Drive.Files.List request = serviceDrive.files().list();

        if (maxResults != null) request.setMaxResults(maxResults);
        if (q != null) request.setQ(q);

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                logger.debug("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);

        return result;
    }

    /**
     * Insert new file
     * Maximum file size: 10GB
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/insert
     *
     * @param credential
     * @param parentId (Facultatif) Id du dossier parent
     * @param fileContent Fichier a transfer sur GDoc
     * @param mimeType MINE Type du fichier à insérer
     * @return File
     */
    public File insertFile(Credential credential, String parentId, java.io.File fileContent, String mimeType) throws IOException {
        createServiceDrive(credential);

        // File's metadata.
        File body = new File();
        body.setTitle(fileContent.getName());
        body.setMimeType(mimeType);
        body.setEditable(true);

        // Set the parent folder.
        if (parentId != null && parentId.length() > 0) {
            body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
        }

        FileContent mediaContent = new FileContent(mimeType, fileContent);
        try {
            File file = serviceDrive.files().insert(body, mediaContent).setConvert(true).execute();

            // Uncomment the following line to print the File ID.
            logger.debug("insertFile : File ID: %s" + file.getId());

            return file;
        } catch (IOException e) {
            logger.debug("insertFile : An error occured: " + e);
        }
        return null;

    }

    /**
     * Supprimer définitivement un fichier, en sautant la poubelle.
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/delete
     *
     * @param credential
     * @param fileId id du fichier a supprimer
     */
    public boolean deleteFile(Credential credential, String fileId) {
        createServiceDrive(credential);
        try {
            serviceDrive.files().delete(fileId).execute();
            return true;
        } catch (IOException e) {
            logger.debug("An error occurred: " + e);
        }
        return false;
    }

    /**
     * Copie un fichier existant
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/copy
     *
     * @param credential
     * @param originFileId Id du fichier d'origine à copier
     * @param copyTitle Titre de la copie
     * @return Le fichier copier en cas de succès, sinon {@code null}
     */
    public File copyFile(Credential credential, String originFileId, String copyTitle) {
        createServiceDrive(credential);
        File copiedFile = new File();
        copiedFile.setTitle(copyTitle);
        try {
            return serviceDrive.files().copy(originFileId, copiedFile).execute();
        } catch (IOException e) {
            logger.debug("An error occurred: " + e);
        }
        return null;
    }

    /**
     * Renome un fichier
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/patch
     *
     * @param credential
     * @param fileId Id du fichier a modifier
     * @return Updated file metadata if successful, {@code null} otherwise.
     */
    public File renameFile(Credential credential, String fileId, String newTitle) {
        createServiceDrive(credential);
        try {
            File file = new File();
            file.setTitle(newTitle);

            Drive.Files.Patch patchRequest = serviceDrive.files().patch(fileId, file);
            patchRequest.setFields("title");

            File updatedFile = patchRequest.execute();
            return updatedFile;
        } catch (IOException e) {
            logger.debug("An error occurred: " + e);
        }
        return null;
    }

    /**
     * Deplace le fichier dans la corbeille
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/trash
     *
     * @param credential
     * @param fileId ID of the file to trash.
     * @return Le fichier mise à jour en cas de succès, sinon {@code null}
     */
    public File trashFile(Credential credential, String fileId) {
        createServiceDrive(credential);
        try {
            return serviceDrive.files().trash(fileId).execute();
        } catch (IOException e) {
            logger.debug("An error occurred: " + e);
        }
        return null;
    }

    /**
     * Restaure un fichier de la corbeille
     *
     * Voir la documentation de google :
     * https://developers.google.com/drive/v2/reference/files/untrash
     *
     * @param fileId Id du fichier à restaurer
     * @return Le fichier mise à jour en cas de succès, sinon {@code null}
     */
    private static File restoreFile(Drive service, String fileId) {
        try {
            return service.files().untrash(fileId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return null;
    }
}
