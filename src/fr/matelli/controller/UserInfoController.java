package fr.matelli.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.oauth2.model.Userinfoplus;

import fr.matelli.GoogleService.GoogleAuthHelper;
import fr.matelli.GoogleService.service.UserInfoService;

/**
 * Classe d'exemple afin d'utiliser le SSO de google
 * 
 * @author <a href="http://www.matelli.fr">Matelli</a>
 * @see GoogleAuthHelper
 * @see UserInfoService
 */
@Controller
@RequestMapping("/userinfo")
public class UserInfoController {

	@RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
	public String printHome(ModelMap model) throws Exception {
		// "/userinfo/login" : Url de callback pour google
		UserInfoService userInfo = new UserInfoService("/userinfo/login");
		model.addAttribute("authorizationUrlGoogle",
				userInfo.getAuthorizationUrl());
		return "userinfo/index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String printInfoUser(ModelMap model, HttpServletRequest request,
			HttpSession session, HttpServletResponse response) throws Exception {
		model.addAttribute("error", null);

		if (request.getParameter("code") != null) {
			UserInfoService userInfo = new UserInfoService("/userinfo/login");
			userInfo.setAuthorizationCode(request.getParameter("code"));
			Credential credential = userInfo.exchangeCode();
			Userinfoplus user = userInfo.getUserInfo(credential);
			if (user != null && user.isVerifiedEmail()) {
				Handling.createCookie(response, "userId", user.getId());
				Handling.createCookie(response, "refreshToken", credential.getRefreshToken());
	            model.addAttribute("user", user);

				return "home";
			} else {
				model.addAttribute("error", "Problèmes d'authentification");
			}
		}
		return printHome(model);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String signout(ModelAndView model, HttpServletRequest request,
			HttpSession httpSession) {
		httpSession.invalidate();
		return "redirect:/";
	}

}