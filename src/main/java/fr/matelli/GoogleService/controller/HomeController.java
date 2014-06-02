package fr.matelli.GoogleService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author <a href="http://www.matelli.fr">Matelli</a>
 */
@Controller
public class HomeController {

	@RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
	public String printHome(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
            return "home";
        } else {
            return "redirect:/userinfo";
        }
	}
}