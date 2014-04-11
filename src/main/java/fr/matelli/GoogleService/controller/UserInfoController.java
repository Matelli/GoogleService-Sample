package fr.matelli.GoogleService.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.oauth2.model.Userinfoplus;
import fr.matelli.GoogleService.googleService.service.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/userinfo")
public class UserInfoController {

    @RequestMapping(method = RequestMethod.GET)
    public String printHome(ModelMap model) throws Exception {
        UserInfo userInfo = new UserInfo("/userinfo/login");
        model.addAttribute("authorizationUrlGoogle", userInfo.getAuthorizationUrl());
        return "userinfo/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String printInfoUser(ModelMap model, HttpServletRequest request) throws Exception {
        if (request.getParameter("code") != null) {
            UserInfo userInfo = new UserInfo("/userinfo/login");
            userInfo.setAuthorizationCode(request.getParameter("code"));
            Credential credential = userInfo.exchangeCode();
            System.out.println("refresh token = " + userInfo.getRefreshToken());
            Userinfoplus user = userInfo.getUserInfo(credential);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("error", "Aucun code de retour de google");
        }
        System.out.println("model = [" + model + "], request = [" + request + "]");
        return "userinfo/login";
    }
	
}