package fr.matelli.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;

import fr.matelli.GoogleService.GoogleAuthHelper;
import fr.matelli.GoogleService.service.DriveService;
import fr.matelli.GoogleService.utils.MimeUtils;

/**
 * Classe d'exemple afin d'utiliser le service Drive
 *
 * @author <a href="http://www.matelli.fr">Matelli</a>
 * @see GoogleAuthHelper
 * @see DriveService
 */
@Controller
@RequestMapping("/drive")
public class DriveController {

    @RequestMapping(method = RequestMethod.GET)
    public String printHome(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
        if (Handling.getCookie(request, "userId") != null) {
            // Création de l'url de callback pour la liste des fichiers
            DriveService retrieveAllFiles = new DriveService("/drive/retrieveAllFiles");
            retrieveAllFiles.setRefreshToken(Handling.getCookie(request, "refreshToken"));
            model.addAttribute("authorizationUrlGoogleRetrieveAllFiles", retrieveAllFiles.getAuthorizationUrl());

            // Création de l'url de callback pour l'insertion d'un nouveau document
            DriveService insertFile = new DriveService("/drive/insertFile");
            insertFile.setRefreshToken(Handling.getCookie(request, "refreshToken"));
            model.addAttribute("authorizationUrlGoogleInsertFile", insertFile.getAuthorizationUrl());

            return "drive/index";
        } else {
            model.addAttribute("error", "Aucun code de retour de google" + Handling.getCookie(request, "userId") );
            return "userinfo/index";
            //return "redirect:/userinfo";
        }
    }

    @RequestMapping(value = "/retrieveAllFiles", method = RequestMethod.GET)
    public String retrieveAllFiles(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
    	if (Handling.getCookie(request, "userId") != null) {
            if (request.getParameter("code") != null) {
                DriveService driveService = new DriveService("/drive/retrieveAllFiles");
                driveService.setAuthorizationCode(request.getParameter("code"));

                Credential credential = driveService.exchangeCode();
                // Liste des photos et videos de votre compte Google Drive
                List<File> files = driveService.retrieveAllFiles(credential, null, "mimeType contains 'image/' or mimeType contains 'video/'");
                model.addAttribute("files", files);
            } else {
                model.addAttribute("error", "Aucun code de retour de google");
            }
            return "drive/retrieveAllFiles";
        } else {
            return "redirect:/userinfo";
        }
    }

    @RequestMapping(value = "/insertFile", method = RequestMethod.GET)
    public String insertFiles(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
    	if (Handling.getCookie(request, "userId") != null) {
            if (request.getParameter("code") != null) {
                DriveService driveService = new DriveService("/drive/insertFile");
                driveService.setAuthorizationCode(request.getParameter("code"));

                Credential credential = driveService.exchangeCode();
                java.io.File fileContent = new java.io.File("README.md");

                File file = driveService.insertFile(credential, null, fileContent, MimeUtils.TXT.getMimeType());
                model.addAttribute("fileInserted", file);
            } else {
                model.addAttribute("error", "Aucun code de retour de google" + Handling.getCookie(request, "userId"));
            }
            return "drive/insertFile";
        } else {
            model.addAttribute("error", "Erreur cookie" + Handling.getCookie(request, "userId"));
            return "redirect:/userinfo";
        }
    }
	
}