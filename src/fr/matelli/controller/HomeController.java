package fr.matelli.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.oauth2.model.Userinfoplus;

import fr.matelli.GoogleService.service.UserInfoService;

/**
 * @author <a href="http://www.matelli.fr">Matelli</a>
 */
@Controller
public class HomeController {

	@RequestMapping(value = {"/home"}, method = RequestMethod.GET)
	public String printHome(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		if (Handling.getCookie(request, "userId") != null) {
			UserInfoService userInfoService = new UserInfoService(null);
			userInfoService.setRefreshToken(Handling.getCookie(request, "refreshToken"));
			Credential credential = userInfoService.exchangeCode();
			Userinfoplus userinfoplus = userInfoService.getUserInfo(credential);
            model.addAttribute("user", userinfoplus);
            return "home";
        } else {
            return "redirect:/userinfo";
        }
	}
}