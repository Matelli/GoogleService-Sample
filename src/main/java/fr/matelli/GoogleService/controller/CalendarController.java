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
import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @RequestMapping(method = RequestMethod.GET)
    public String printHome(ModelMap model) throws Exception {
        DriveService driveService = new DriveService("/calendar/calendarList");
        model.addAttribute("authorizationUrlGoogle", driveService.getAuthorizationUrl());
        return "calendar/index";
    }

    @RequestMapping(value = "/calendarList", method = RequestMethod.GET)
    public String printCalendarList(ModelMap model, HttpServletRequest request) throws Exception {
        if (request.getParameter("code") != null) {
            CalendarService calendarService = new CalendarService("/calendar/calendarList");
            calendarService.setAuthorizationCode(request.getParameter("code"));
            Credential credential = calendarService.exchangeCode();
            System.out.println("refresh token = " + calendarService.getRefreshToken());
            List<CalendarListEntry> calendarListEntries = calendarService.calendarList(credential);
            model.addAttribute("calendarListEntries", calendarListEntries);
        } else {
            model.addAttribute("error", "Aucun code de retour de google");
        }
        System.out.println("model = [" + model + "], request = [" + request + "]");
        return "calendar/calendarList";
    }
	
}