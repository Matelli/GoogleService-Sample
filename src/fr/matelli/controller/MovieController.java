package fr.matelli.controller;
 
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
@RequestMapping("/movie")
public class MovieController {
 
	//DI via Spring
	String message;
 
	@RequestMapping(value="/{name}", method = RequestMethod.GET)
	public void getMovie(@PathVariable String name, ModelMap model, HttpServletResponse resp) throws IOException {
 
		model.addAttribute("movie", name);
		model.addAttribute("message", this.message);
		
		//return to jsp page, configured in mvc-dispatcher-servlet.xml, view resolver
		//return "list";
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world les Matellissen");
 
	}
 
	public void setMessage(String message) {
		this.message = message;
	}
 
}