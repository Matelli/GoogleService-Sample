package fr.matelli.GoogleService.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import fr.matelli.GoogleService.googleService.GoogleAuthHelper;
import fr.matelli.GoogleService.googleService.service.CalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'exemple afin d'utiliser le service Calendar
 *
 * @author <a href="http://www.matelli.fr">Matelli</a>
 * @see GoogleAuthHelper
 * @see CalendarService
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
     * Creation de plusieurs calendriers
     *
     * @param model
     * @param request
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/calendarInsert", method = RequestMethod.GET)
    public String printInsertCalendar(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
        if (session.getAttribute("user") != null) {
            CalendarService calendarService = new CalendarService(null);
            calendarService.setRefreshToken(session.getAttribute("refreshToken").toString());
            Credential credential = calendarService.exchangeCode();
            List<String> listSummaryCalendar = new ArrayList<String>();
            listSummaryCalendar.add(calendarService.calendarInsert(credential, "toto1").getSummary());
            listSummaryCalendar.add(calendarService.calendarInsert(credential, "toto2").getSummary());
            listSummaryCalendar.add(calendarService.calendarInsert(credential, "toto3").getSummary());
            model.addAttribute("listSummaryCalendar", listSummaryCalendar);
            return "calendar/calendarInsert";
        } else {
            return "redirect:/userinfo";
        }
    }


    @RequestMapping(value = "/listEvents/{calendarId}/", method = RequestMethod.GET)
    public String printCalendarList(ModelMap model, HttpServletRequest request, HttpSession session, @PathVariable String calendarId) throws Exception {
        if (session.getAttribute("user") != null) {
            CalendarService calendarService = new CalendarService(null);
            calendarService.setRefreshToken(session.getAttribute("refreshToken").toString());
            Credential credential = calendarService.exchangeCode();
            List<Event> eventListEntries = calendarService.eventsList(credential, calendarId, null);
            model.addAttribute("events", eventListEntries);
            model.addAttribute("calendarId", calendarId);
            return "calendar/eventList";
        } else {
            return "redirect:/userinfo";
        }
    }


    @RequestMapping(value = "/calendarList", method = RequestMethod.GET)
    public String printEventList(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
        if (session.getAttribute("user") != null) {
            CalendarService calendarService = new CalendarService(null);
            calendarService.setRefreshToken(session.getAttribute("refreshToken").toString());
            Credential credential = calendarService.exchangeCode();
            List<CalendarListEntry> calendarListEntries = calendarService.calendarList(credential);
            model.addAttribute("calendarListEntries", calendarListEntries);
            return "calendar/calendarList";
        } else {
            return "redirect:/userinfo";
        }
    }
	
}