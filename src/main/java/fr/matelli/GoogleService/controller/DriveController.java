package fr.matelli.GoogleService.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;
import com.google.api.services.oauth2.model.Userinfoplus;
import fr.matelli.GoogleService.googleService.service.DriveService;
import fr.matelli.GoogleService.googleService.service.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/drive")
public class DriveController {

    @RequestMapping(method = RequestMethod.GET)
    public String printHome(ModelMap model, HttpSession session) throws Exception {
        if (session.getAttribute("user") != null) {
            DriveService driveService = new DriveService("/drive/retrieveAllFiles");
            driveService.setRefreshToken(session.getAttribute("refreshToken").toString());
            System.out.println("DriveController.printHome " + session.getAttribute("refreshToken").toString());
            model.addAttribute("authorizationUrlGoogle", driveService.getAuthorizationUrl());
            return "drive/index";
        } else {
            return "redirect:/userinfo";
        }
    }

    @RequestMapping(value = "/retrieveAllFiles", method = RequestMethod.GET)
    public String printRetrieveAllFiles(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
        if (session.getAttribute("user") != null) {
            if (request.getParameter("code") != null) {
                DriveService driveService = new DriveService("/drive/retrieveAllFiles");
                driveService.setAuthorizationCode(request.getParameter("code"));

                Credential credential = driveService.exchangeCode();
                List<File> files = driveService.retrieveAllFiles(credential);
                model.addAttribute("files", files);
            } else {
                model.addAttribute("error", "Aucun code de retour de google");
            }
            return "drive/retrieveAllFiles";
        } else {
            return "redirect:/userinfo";
        }
    }
	
}