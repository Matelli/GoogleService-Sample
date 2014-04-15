package fr.matelli.GoogleService.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.oauth2.model.Userinfoplus;
import fr.matelli.GoogleService.googleService.service.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String printInfoUser(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
        if (request.getParameter("code") != null) {
            UserInfo userInfo = new UserInfo("/userinfo/login");
            userInfo.setAuthorizationCode(request.getParameter("code"));
            Credential credential = userInfo.exchangeCode();
            Userinfoplus user = userInfo.getUserInfo(credential);
            if (user != null && user.isVerifiedEmail()) {
                session.setAttribute("user", user);
                session.setAttribute("refreshToken", credential.getRefreshToken());
                return "redirect:/home";
            }
        }
        model.addAttribute("error", "Problèmes d'authentification");
        return printHome(model);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String signout(ModelAndView model, HttpServletRequest request, HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }
	
}