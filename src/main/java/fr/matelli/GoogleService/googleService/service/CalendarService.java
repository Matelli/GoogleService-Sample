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

/**
 * Classe permettant d'interagir avec les services Google Calendar
 *
 * @author <a href="http://www.matelli.fr">Matelli</a>
 * @see fr.matelli.GoogleService.googleService.GoogleAuthHelper
 * @see <a href"https://developers.google.com/google-apps/calendar/v3/reference/>https://developers.google.com/google-apps/calendar/v3/reference/</a>
 */
public class CalendarService extends GoogleAuthHelper {

    {
        // Liste des authorisations
        this.scopes.addAll(Arrays.asList(CalendarScopes.CALENDAR));
    }

    protected Calendar serviceCalendar = null;

    protected Logger logger = Logger.getLogger(CalendarService.class);

    public CalendarService(String redirectUri) throws Exception {
        super(redirectUri);
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
     * Subscribe à un calendrier
     *
     * Voir la documentation de google :
     * https://developers.google.com/google-apps/calendar/v3/reference/calendarList/insert?hl=fr
     *
     * @param calendarId
     * @return CalendarListEntry
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
     * Voir la documentation de google :
     * https://developers.google.com/google-apps/calendar/v3/reference/calendars/insert
     *
     * @param credential
     * @param nameOfCalendar
     * @return The new CalendarService
     * @throws java.io.IOException
     */
    public com.google.api.services.calendar.model.Calendar calendarInsert(Credential credential, String nameOfCalendar) throws IOException {
        createServiceCalendar(credential);
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary(nameOfCalendar);
        calendar.setTimeZone("Europe/Paris");

        com.google.api.services.calendar.model.Calendar createdCalendar = serviceCalendar.calendars().insert(calendar).execute();

        logger.debug("calendarInsert idCalendar : " + createdCalendar.getId());
        return createdCalendar;
    }

    /**
     * Retourne un calendrier
     *
     * Voir la documentation de google :
     * https://developers.google.com/google-apps/calendar/v3/reference/calendars/get
     *
     * @param credential
     * @param calendarIdGoogle
     * @return Un calendrier ou Null si il existe pas
     * @throws java.io.IOException
     */
    public com.google.api.services.calendar.model.Calendar calendarGet(Credential credential, String calendarIdGoogle) throws IOException {
        createServiceCalendar(credential);
        com.google.api.services.calendar.model.Calendar calendar = null;
        try {
            Calendar.Calendars.Get get = serviceCalendar.calendars().get(calendarIdGoogle);
            calendar = get.execute();
            logger.debug("calendarGet : " + calendar.getSummary());
            return calendar;
        } catch (com.google.api.client.googleapis.json.GoogleJsonResponseException e) {
            // Dans ce cas on ne doit pas arreter le script
            logger.debug("calendarGet : NULL");
            logger.debug("Warning (Le calendrier n'existe pas) : " + e);
            return null;
        }
    }

    /**
     * Returns entries on the user's calendar list
     *
     * Voir la documentation de google :
     * https://developers.google.com/google-apps/calendar/v3/reference/calendarList/list?hl=fr
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
     * Voir la documentation de google :
     * https://developers.google.com/google-apps/calendar/v3/reference/events/list
     *
     * @param credential
     * @param calendarIdGoogle
     * @param timeMin
     * @return A list of Event
     * @throws java.io.IOException
     */
    public List<Event> eventsList(Credential credential, String calendarIdGoogle, DateTime timeMin)
            throws IOException {
        createServiceCalendar(credential);
        String pageToken = null;
        List<Event> listAllEvents = new ArrayList<Event>();

        do {
            Calendar.Events.List listEvents = serviceCalendar.events().list(calendarIdGoogle).setPageToken(pageToken);

            if (timeMin != null)
                listEvents.setTimeMin(timeMin);

            Events events = listEvents.execute();

            List<Event> items = events.getItems();
            for (Event event : items) {
                listAllEvents.add(event);
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        logger.debug("nb Events : " + listAllEvents.size());
        return listAllEvents;
    }

    /**
     * Creates an event
     *
     * Voir la documentation de google :
     * https://developers.google.com/google-apps/calendar/v3/reference/events/insert
     *
     * @param credential
     * @param calendarIdGoogle
     * @param event
     * @return Event
     * @throws java.io.IOException
     */
    public Event eventInsert(Credential credential, String calendarIdGoogle, Event event)
            throws IOException {
        createServiceCalendar(credential);
        Event createdEvent = serviceCalendar.events().insert(calendarIdGoogle, event).execute();
        logger.debug("createdEvent : " + createdEvent.getId() + ", " + event.getSummary());
        return createdEvent;
    }

    /**
     * Updates an event
     *
     * Voir la documentation de google :
     * https://developers.google.com/google-apps/calendar/v3/reference/events/update
     *
     * @param credential
     * @param calendarIdGoogle
     * @param event
     * @throws java.io.IOException
     */
    public Event eventUpdate(Credential credential, String calendarIdGoogle, Event event)
            throws IOException {
        createServiceCalendar(credential);
        Event updatedEvent = serviceCalendar.events().update(calendarIdGoogle, event.getId(), event).execute();
        logger.debug("eventUpdate updatedEvent : " + updatedEvent.toPrettyString());
        logger.debug("createdUpdate : " + updatedEvent.getId() + ", " + event.getSummary());
        return updatedEvent;
    }

    /**
     * Returns an event
     *
     * Voir la documentation de google :
     * https://developers.google.com/google-apps/calendar/v3/reference/events/get
     *
     * @param credential
     * @param calendarIdGoogle
     * @param eventId
     * @return
     * @throws java.io.IOException
     */
    public Event eventGet(Credential credential, String calendarIdGoogle, String eventId)
            throws IOException {
        createServiceCalendar(credential);
        Event event = serviceCalendar.events().get(calendarIdGoogle, eventId).execute();
        return event;

    }

    /**
     * Deletes an event
     *
     * Voir la documentation de google :
     * https://developers.google.com/google-apps/calendar/v3/reference/events/delete
     *
     * @param credential
     * @param calendarIdGoogle
     * @param eventId
     * @throws java.io.IOException
     */
    public void eventDelete(Credential credential, String calendarIdGoogle, String eventId)
            throws IOException {
        createServiceCalendar(credential);
        serviceCalendar.events().delete(calendarIdGoogle, eventId).execute();
    }
}
