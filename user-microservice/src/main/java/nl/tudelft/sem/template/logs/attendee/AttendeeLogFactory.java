package nl.tudelft.sem.template.logs.attendee;

import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.logs.LogFactory;

/**
 * The factory implementation for the Attendee ```LogFactory```.
 */
public class AttendeeLogFactory extends LogFactory {

    /**
     * Creates a Log representing the creation of a new Attendee.
     *
     * @param subject the newly created attendance.
     * @return a Log representing the creation of the attendance.
     */
    @Override
    public AttendeeLog registerCreation(Object subject) {
        return new CreatedAttendeeLog((Attendee) subject);
    }

    /**
     * Creates a Log representing the modification of the confirmation
     * status of an Attendee.
     *
     * @param subject the modified Attendee.
     * @return a log representing the modification of the attendance.
     */
    public AttendeeLog registerConfirmationChange(Attendee subject) {
        return new ConfirmationChangeAttendeeLog(subject);
    }

    /**
     * Creates a Log representing the modification of the attendance
     * role of an Attendee.
     *
     * @param subject the modified Attendee.
     * @return a log representing the modification of the attendance.
     */
    public AttendeeLog registerRoleChange(Attendee subject) {
        return new RoleChangedAttendeeLog(subject);
    }


    /**
     * Creates a Log representing the deletion of an Attendee.
     *
     * @param subject the to-be-deleted attendance.
     * @return a log representing the deletion of the attendance.
     */
    public AttendeeLog registerRemoval(Attendee subject) {
        return new RemovedAttendeeLog(subject);
    }



}
