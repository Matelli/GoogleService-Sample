package fr.matelli.GoogleService.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;
import com.google.api.services.oauth2.model.Userinfoplus;
import fr.matelli.GoogleService.googleService.service.DriveService;
import fr.matelli.GoogleService.googleService.service.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/drive")
public class DriveController {

    @RequestMapping(method = RequestMethod.GET)
    public String printHome(ModelMap model) throws Exception {
        DriveService driveService = new DriveService("/drive/retrieveAllFiles");
        model.addAttribute("authorizationUrlGoogle", driveService.getAuthorizationUrl());
        return "drive/index";
    }

    @RequestMapping(value = "/retrieveAllFiles", method = RequestMethod.GET)
    public String printRetrieveAllFiles(ModelMap model, HttpServletRequest request) throws Exception {
        if (request.getParameter("code") != null) {
            DriveService driveService = new DriveService("/drive/retrieveAllFiles");
            driveService.setAuthorizationCode(request.getParameter("code"));
            Credential credential = driveService.exchangeCode();
            System.out.println("refresh token = " + driveService.getRefreshToken());
            List<File> files = driveService.retrieveAllFiles(credential);
            model.addAttribute("files", files);
        } else {
            model.addAttribute("error", "Aucun code de retour de google");
        }
        System.out.println("model = [" + model + "], request = [" + request + "]");
        return "drive/retrieveAllFiles";
    }
	
}