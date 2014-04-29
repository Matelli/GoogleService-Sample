package fr.matelli.GoogleService.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.drive.model.File;
import fr.matelli.GoogleService.googleService.service.CalendarService;
import fr.matelli.GoogleService.googleService.service.DriveService;
import fr.matelli.GoogleService.googleService.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Classe d'exemple afin d'utiliser le service Calendar
 *
 * @author <a href="http://www.matelli.fr">Matelli</a>
 * @see fr.matelli.GoogleService.googleService.GoogleAuthHelper
 * @see fr.matelli.GoogleService.googleService.service.CalendarService
 */
@Controller
@RequestMapping("/calendar")
public class CalendarController {

    /**
     *
     * @param model
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    public String printHome(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
        if (session.getAttribute("user") != null) {
            if (request.getParameter("code") != null) {
                CalendarService calendar = new CalendarService("/calendar");
                calendar.setAuthorizationCode(request.getParameter("code"));
                Credential credential = calendar.exchangeCode();
                if (credential != null) {
                    // Mise a jour du refresh token
                    session.setAttribute("refreshToken", credential.getRefreshToken());
                    session.setAttribute("scopes", calendar.getScopes());
                    return "redirect:/calendar";
                }
            } else {
                // Si dans la session le scope Calendar existe alors le Refresh Token permet de faire des demande
                // de jetons pour calendar
                if (!((List<String>) session.getAttribute("scopes")).contains(CalendarScopes.CALENDAR)) {
                    CalendarService driveService = new CalendarService("/calendar");
                    model.addAttribute("authorizationUrlGoogleCalendar", driveService.getAuthorizationUrl());
                }
            }
            return "calendar/index";
        } else {
            return "redirect:/userinfo";
        }
    }

    /**
     *
     * @param model
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/calendarList", method = RequestMethod.GET)
    public String printCalendarList(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
        if (session.getAttribute("user") != null) {
            CalendarService calendarService = new CalendarService(null);
            calendarService.setRefreshToken(session.getAttribute("refreshToken").toString());
            Credential credential = calendarService.exchangeCode();
            Calendar calendarInsert = calendarService.calendarInsert(credential, "toto1");
            calendarService.calendarInsert(credential, "toto2");
            calendarService.calendarInsert(credential, "toto3");
            model.addAttribute("calendarListEntries", calendarInsert.getSummary());
            System.out.println("model = [" + model + "], request = [" + request + "]");
            return "calendar/calendarList";
        } else {
            return "redirect:/userinfo";
        }
    }
	
}