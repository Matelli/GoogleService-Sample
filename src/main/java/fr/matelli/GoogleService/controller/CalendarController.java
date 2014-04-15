package fr.matelli.GoogleService.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.drive.model.File;
import fr.matelli.GoogleService.googleService.service.CalendarService;
import fr.matelli.GoogleService.googleService.service.DriveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @RequestMapping(method = RequestMethod.GET)
    public String printHome(ModelMap model, HttpSession session) throws Exception {
        if (session.getAttribute("user") != null) {
            CalendarService driveService = new CalendarService("/calendar/calendarList");
            driveService.setRefreshToken(session.getAttribute("refreshToken").toString());
            model.addAttribute("authorizationUrlGoogleCalendarList", driveService.getAuthorizationUrl());
            return "calendar/index";
        } else {
            return "redirect:/userinfo";
        }
    }

    @RequestMapping(value = "/calendarList", method = RequestMethod.GET)
    public String printCalendarList(ModelMap model, HttpServletRequest request, HttpSession session) throws Exception {
        if (session.getAttribute("user") != null) {
            if (request.getParameter("code") != null) {
                CalendarService calendarService = new CalendarService("/calendar/calendarList");
                calendarService.setAuthorizationCode(request.getParameter("code"));
                Credential credential = calendarService.exchangeCode();
                List<CalendarListEntry> calendarListEntries = calendarService.calendarList(credential);
                System.out.println("CalendarController.printCalendarList - getRefreshToken = " + calendarService.getRefreshToken());
                model.addAttribute("calendarListEntries", calendarListEntries);
            } else {
                model.addAttribute("error", "Aucun code de retour de google");
            }
            System.out.println("model = [" + model + "], request = [" + request + "]");
            return "calendar/calendarList";
        } else {
            return "redirect:/userinfo";
        }
    }
	
}