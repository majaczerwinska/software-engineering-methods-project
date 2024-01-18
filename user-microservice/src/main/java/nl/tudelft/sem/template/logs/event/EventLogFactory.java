package nl.tudelft.sem.template.logs.event;

import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.logs.LogFactory;

/**
 * The factory implementation for the Event ```LogFactory```.
 */
public class EventLogFactory extends LogFactory {
    /**
     * Creates a Log representing the creation of a new event.
     *
     * @param subject the newly created event.
     * @return a Log representing the creation of the event.
     */
    @Override
    public EventLog registerCreation(Object subject) {
        return new CreatedEventLog((Event) subject);
    }

    /**
     * Creates a Log representing the edition of an event's start date.
     *
     * @param subject the newly created event.
     * @return a Log representing the modification of the event.
     */
    public EventLog registerStartDateChange(Event subject) {
        return new StartDateChangedEventLog(subject);
    }

    /**
     * Creates a Log representing the edition of an event's end date.
     *
     * @param subject the newly created event.
     * @return a Log representing the modification of the event.
     */
    public EventLog registerEndDateChange(Event subject) {
        return new EndDateChangedEventLog(subject);
    }

    /**
     * Creates a Log representing the edition of an event's cancelled status.
     *
     * @param subject the newly created event.
     * @return a Log representing the modification of the event.
     */
    public EventLog registerIsCancelledChange(Event subject) {
        return new IsCancelledChangedEventLog(subject);
    }

    /**
     * Creates a Log representing the edition of an event's name.
     *
     * @param subject the newly created event.
     * @return a Log representing the modification of the event.
     */
    public EventLog registerEventNameChange(Event subject) {
        return new EventNameChangedEventLog(subject);
    }

    /**
     * Creates a Log representing the edition of an event's description.
     *
     * @param subject the newly created event.
     * @return a Log representing the modification of the event.
     */
    public EventLog registerEventDescriptionChange(Event subject) {
        return new EventDescriptionChangedEventLog(subject);
    }
}
