package fr.matelli.GoogleService.googleService.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import fr.matelli.GoogleService.googleService.GoogleAuthHelper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalendarService extends GoogleAuthHelper {

    {
        this.setScopes(Arrays.asList(CalendarScopes.CALENDAR));
    }

    protected Calendar serviceCalendar = null;

    protected Logger logger = Logger.getLogger(CalendarService.class);

    public CalendarService(String redirectUri) throws Exception {
        super(redirectUri);
    }

    public CalendarService(String redirectUri, List<String> scopes) throws Exception {
        super(redirectUri);
        this.scopes.clear();
        this.scopes.addAll(scopes);
    }

    /**
     * Create the calendar service
     *
     * @param credential
     * @return CalendarService Service
     */
    private Calendar createServiceCalendar(Credential credential) {
        if (serviceCalendar == null) {
            serviceCalendar = new Calendar.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(applicationName).build();
        }
        return serviceCalendar;
    }

    /**
     * Subscribe to calendar
     * @throws java.io.IOException
     */
    public CalendarListEntry calendarListInsert(String calendarId) throws IOException {
        CalendarListEntry calendarListEntry = new CalendarListEntry();
        calendarListEntry.setId(calendarId);
        return serviceCalendar.calendarList().insert(calendarListEntry).execute();
    }

    /**
     * Créer un Calendrier
     *
     * @param credential
     * @return The new CalendarService
     * @throws java.io.IOException
     * @see "https://developers.google.com/google-apps/calendar/v3/reference/calendars/insert"
     */
    public com.google.api.services.calendar.model.Calendar calendarInsert(Credential credential, String nameOfCalendar) throws IOException {
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary(nameOfCalendar);
        calendar.setTimeZone("Europe/Paris");

        com.google.api.services.calendar.model.Calendar createdCalendar = serviceCalendar.calendars().insert(calendar).execute();

        logger.info("calendarInsert idCalendar : " + createdCalendar.getId());
        return createdCalendar;
    }

    /**
     * Retourne un calendrier
     *
     * @param credential
     * @param calendarIdGoogle
     * @return Un calendrier ou Null si il existe pas
     * @throws java.io.IOException
     * @see "https://developers.google.com/google-apps/calendar/v3/reference/calendars/get"
     */
    public com.google.api.services.calendar.model.Calendar calendarGet(Credential credential, String calendarIdGoogle) throws IOException {
        com.google.api.services.calendar.model.Calendar calendar = null;
        try {
            Calendar.Calendars.Get get = serviceCalendar.calendars().get(calendarIdGoogle);
            calendar = get.execute();
            logger.info("calendarGet : " + calendar.getSummary());
            return calendar;
        } catch (com.google.api.client.googleapis.json.GoogleJsonResponseException e) {
            // Dans ce cas on ne doit pas arreter le script
            logger.warn("calendarGet : NULL");
            logger.warn("Warning (Le calendrier n'existe pas) : " + e);
            return null;
        }
    }

    /**
     * Returns entries on the user's calendar list
     *
     * @param credential
     * @return CalendarListEntry
     * @throws IOException
     */
    public List<CalendarListEntry> calendarList(Credential credential) throws IOException {
        createServiceCalendar(credential);
        String pageToken = null;
        List<CalendarListEntry> items = new ArrayList<CalendarListEntry>();
        do {
            CalendarList calendarList = serviceCalendar.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> listEntry = calendarList.getItems();
            items.addAll(listEntry);
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);
        return items;
    }

    /**
     * Returns events on the specified calendar
     *
     * @param credential
     * @param calendarIdGoogle
     * @param timeMin
     * @return A list of Event
     * @throws java.io.IOException
     * @see "https://developers.google.com/google-apps/calendar/v3/reference/events/list"
     */
    public List<Event> eventsList(Credential credential, String calendarIdGoogle, DateTime timeMin)
            throws IOException {
        String pageToken = null;
        List<Event> listEvents = new ArrayList<Event>();

        do {
            Events events = serviceCalendar.events().list(calendarIdGoogle)
                    .setPageToken(pageToken)
                    .setTimeMin(timeMin).execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                listEvents.add(event);
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        logger.info("nb Events : " + listEvents.size());
        return listEvents;
    }

    /**
     * Creates an event
     *
     * @param credential
     * @param calendarIdGoogle
     * @param event
     * @return Event
     * @throws java.io.IOException
     * @see "https://developers.google.com/google-apps/calendar/v3/reference/events/insert"
     */
    public Event eventInsert(Credential credential, String calendarIdGoogle, Event event)
            throws IOException {
        Event createdEvent = serviceCalendar.events().insert(calendarIdGoogle, event).execute();
        logger.info("createdEvent : " + createdEvent.getId() + ", " + event.getSummary());
        return createdEvent;
    }

    /**
     * Updates an event
     *
     * @param credential
     * @param calendarIdGoogle
     * @param event
     * @throws java.io.IOException
     * @see "https://developers.google.com/google-apps/calendar/v3/reference/events/update"
     */
    public Event eventUpdate(Credential credential, String calendarIdGoogle, Event event)
            throws IOException {
        Event updatedEvent = serviceCalendar.events().update(calendarIdGoogle, event.getId(), event).execute();
        logger.info("eventUpdate updatedEvent : " + updatedEvent.toPrettyString());
        logger.info("createdUpdate : " + updatedEvent.getId() + ", " + event.getSummary());
        return updatedEvent;
    }

    /**
     * Returns an event
     *
     * @param credential
     * @param calendarIdGoogle
     * @param eventId
     * @return
     * @throws java.io.IOException
     * @see "https://developers.google.com/google-apps/calendar/v3/reference/events/get"
     */
    public Event eventGet(Credential credential, String calendarIdGoogle, String eventId)
            throws IOException {
        Event event = serviceCalendar.events().get(calendarIdGoogle, eventId).execute();
        return event;

    }

    /**
     * Deletes an event
     *
     * @param credential
     * @param calendarIdGoogle
     * @param eventId
     * @throws java.io.IOException
     * @see "https://developers.google.com/google-apps/calendar/v3/reference/events/delete"
     */
    public void eventDelete(Credential credential, String calendarIdGoogle, String eventId)
            throws IOException {
        serviceCalendar.events().delete(calendarIdGoogle, eventId).execute();
    }
}
