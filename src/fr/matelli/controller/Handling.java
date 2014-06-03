package fr.matelli.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Handling {

	/**
	 * Retourne un cookie
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for(int i=0;i<cookies.length;i++){
                if(cookies[i].getName().equals(name)){
                	return cookies[i].getValue();
                }
            }
		}
		return null;
	}
	
	/**
	 * Créer un cookie
	 * 
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void createCookie(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60*60);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
