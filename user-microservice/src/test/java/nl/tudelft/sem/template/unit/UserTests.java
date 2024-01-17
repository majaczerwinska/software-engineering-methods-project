package nl.tudelft.sem.template.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.CommunicationAttributeConverter;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.EmailAttributeConverter;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.domain.user.LinkAttributeConverter;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.NameAttributeConverter;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.domain.user.UserAffiliationAttributeConverter;
import nl.tudelft.sem.template.enums.LogKind;
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.user.UserAffiliationChangedEventLog;
import nl.tudelft.sem.template.logs.user.UserAttendanceChangedEventLog;
import nl.tudelft.sem.template.logs.user.UserCommunicationChangedEventLog;
import nl.tudelft.sem.template.logs.user.UserLinkChangedEventLog;
import nl.tudelft.sem.template.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class UserTests {

    static AppUser user1;
    static AppUser user2;
    static AppUser user3;

    /**
     * Set up the variables for the tests.
     */
    @BeforeAll
    public static void setup() {
        user1 = new AppUser(
                1L,
                new Email("user1@example.com"),
                new Name("John"),
                new Name("Doe"),
                new UserAffiliation("Company A"),
                new Link("http://user1.com"),
                new Communication("user1@example.com")
        );

        user2 = new AppUser(
                2L,
                new Email("user2@example.com"),
                new Name("Jane"),
                new Name("Doe"),
                new UserAffiliation("Company B"),
                new Link("http://user2.com"),
                new Communication("user2@example.com")
        );

        user3 = new AppUser(
                3L,
                new Email("user1@example.com"),
                new Name("John"),
                new Name("Doe"),
                new UserAffiliation("Company A"),
                new Link("http://user1.com"),
                new Communication("user1@example.com")
        );
    }

    @Test
    public void allArgsConstructorTest() {
        AppUser user = new AppUser(
                1L,
                new Email("user1@example.com"),
                new Name("John"),
                new Name("Doe"),
                new UserAffiliation("Company A"),
                new Link("http://user1.com"),
                new Communication("user1@example.com")
        );
        assertEquals(1L, user.getId());
        assertEquals("user1@example.com", user.getEmail().toString());
        assertEquals("John", user.getFirstName().toString());
        assertEquals("Doe", user.getLastName().toString());
        assertEquals("Company A", user.getAffiliation().toString());
        assertEquals("http://user1.com", user.getLink().toString());
        assertEquals("user1@example.com", user.getCommunication().toString());

    }

    @Test
    public void allArgsExceptIdConstructorTest() {
        AppUser user = new AppUser(
                new Email("user1@example.com"),
                new Name("John"),
                new Name("Doe"),
                new UserAffiliation("Company A"),
                new Link("http://user1.com"),
                new Communication("user1@example.com")
        );
        Communication communication = new Communication("user1@example.com");
        assertEquals("user1@example.com", user.getEmail().toString());
        assertEquals("John", user.getFirstName().toString());
        assertEquals("Doe", user.getLastName().toString());
        assertEquals("Company A", user.getAffiliation().toString());
        assertEquals("http://user1.com", user.getLink().toString());
        assertEquals("user1@example.com", user.getCommunication().toString());

    }

    @Test
    public void oneArgConstructorTest() {
        AppUser user4 = new AppUser(1L);
        assertNotNull(user4);
        assertEquals(1L, user4.getId());
    }

    @Test
    public void twoArgConstructorTest() {
        AppUser user5 = new AppUser(new Email("email@gmail.com"), new Name("Pete"), new Name("Peterson"));
        assertNotNull(user5);
        assertEquals("email@gmail.com", user5.getEmail().toString());
        assertEquals("Pete", user5.getFirstName().toString());
    }

    @Test
    public void constructorWithUserModelTest() {
        User userModel = new User();
        userModel.setId(1L);
        userModel.setEmail("user@example.com");
        userModel.setFirstName("John");
        userModel.setLastName("Doe");

        AppUser appUser = new AppUser(userModel);

        assertEquals(1L, appUser.getId());
        assertEquals("user@example.com", appUser.getEmail().toString());
        assertEquals("John", appUser.getFirstName().toString());
        assertEquals("Doe", appUser.getLastName().toString());

        User nullUser = null;
        assertThrows(IllegalArgumentException.class, () -> new AppUser(nullUser));
        userModel.setId(1L);
        userModel.setEmail("noEmail");
        assertThrows(IllegalArgumentException.class, () -> new AppUser(userModel));

    }

    @Test
    public void requiredArgsConstructorTest() {
        Email email = new Email("user@example.com");
        AppUser user = new AppUser(email);

        assertEquals(email, user.getEmail());
        assertNotNull(user);
    }


    @Test
    void equalsTests() {
        // Check if the equals method is implemented correctly

        // user1 and user1 are the same user
        assertEquals(user1, user1);

        // user1 and user2 are different users
        assertNotEquals(user1, user2);

        assertNotEquals(user1, null);

        // user1 and user3 have the same attributes, except for their id's
        assertNotEquals(user1, user3);
    }

    @Test
    void hashCodeTests() {
        // user 1 and user 1 are the same
        assertEquals(user1.hashCode(), user1.hashCode());

        // user1 and user2 have different emails, so different hashcode
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void gettersTest() {
        AppUser user5 = new AppUser(
                1L,
                new Email("user1@example.com"),
                new Name("John"),
                new Name("Doe"),
                new UserAffiliation("Company A"),
                new Link("http://user1.com"),
                new Communication("user1@example.com")
        );
        assertEquals("user1@example.com", user5.getEmail().toString());
        assertEquals(1L, user5.getId());
        assertEquals("John", user5.getFirstName().toString());
        assertEquals("Doe", user5.getLastName().toString());
        assertEquals("Company A", user5.getAffiliation().toString());
        assertEquals("http://user1.com", user5.getLink().toString());
        assertEquals("user1@example.com", user5.getCommunication().toString());
    }

    @Test
    void setIdTest() {
        user1.setId(1L);
        assertEquals(1L, user1.getId());
    }

    @Test
    void setEmailTest() {
        user1.setEmail(new Email("abc@org.nl"));
        assertEquals("abc@org.nl", user1.getEmail().toString());
    }

    @Test
    void setFirstNameTest() {
        user1.setFirstName(new Name("Piet"));
        assertEquals("Piet", user1.getFirstName().toString());
    }

    @Test
    void setLastNameTest() {
        user1.setLastName(new Name("Hein"));
        assertEquals("Hein", user1.getLastName().toString());
    }

    @Test
    void setAffiliationTest() {
        user1.setAffiliation(new UserAffiliation("New Affiliation"));
        assertEquals("New Affiliation", user1.getAffiliation().toString());

        assertFalse(user1.getDomainEvents().isEmpty());
        Object domainEvent = user1.getDomainEvents().get(user1.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof UserAffiliationChangedEventLog);
        UserAffiliationChangedEventLog eventLog = (UserAffiliationChangedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith(
                "The affiliation of the AppUser 1 has been successfully updated to \"New Affiliation\"."));
        assertEquals(eventLog.getLogType(), LogType.USER);
        assertEquals(eventLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(eventLog.getSubject(), user1);
    }

    @Test
    void setLinkTest() {
        user1.setLink(new Link("http://newlink.com"));
        assertEquals("http://newlink.com", user1.getLink().toString());

        assertFalse(user1.getDomainEvents().isEmpty());
        Object domainEvent = user1.getDomainEvents().get(user1.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof UserLinkChangedEventLog);
        UserLinkChangedEventLog eventLog = (UserLinkChangedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith(
                "The personal website (link) of the AppUser 1 has been successfully updated to \"http://newlink.com\"."));
        assertEquals(eventLog.getLogType(), LogType.USER);
        assertEquals(eventLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(eventLog.getSubject(), user1);
    }

    @Test
    void setCommunicationTest() {
        user1.setCommunication(new Communication("newemail@example.com"));
        assertEquals("newemail@example.com", user1.getCommunication().toString());

        assertFalse(user1.getDomainEvents().isEmpty());
        Object domainEvent = user1.getDomainEvents().get(user1.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof UserCommunicationChangedEventLog);
        UserCommunicationChangedEventLog eventLog = (UserCommunicationChangedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith(
                "The communication of the AppUser 1 has been successfully updated to \"newemail@example.com\"."));
        assertEquals(eventLog.getLogType(), LogType.USER);
        assertEquals(eventLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(eventLog.getSubject(), user1);
    }

    @Test
    void setAttendanceTest() {
        List<Attendee> attendance = new ArrayList<>();
        Attendee attendee = new Attendee();
        attendance.add(attendee);
        user1.setAttendance(attendance);
        assertEquals(attendee, user1.getAttendance().get(0));

        assertEquals(attendance, user1.getAttendance());

        assertFalse(user1.getDomainEvents().isEmpty());
        Object domainEvent = user1.getDomainEvents().get(user1.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof UserAttendanceChangedEventLog);
        UserAttendanceChangedEventLog eventLog = (UserAttendanceChangedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith(
                "The list of attendees of the AppUser 1 has been successfully updated"));
        assertEquals(eventLog.getLogType(), LogType.USER);
        assertEquals(eventLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(eventLog.getSubject(), user1);
    }

    @Test
    void emailConverterTest() {
        EmailAttributeConverter conv = new EmailAttributeConverter();

        Email email = conv.convertToEntityAttribute("email1@gmail.com");
        assertEquals(email.toString(), "email1@gmail.com");
        assertEquals(conv.convertToDatabaseColumn(email), "email1@gmail.com");
    }

    @Test
    void communicationConverterTest() {
        CommunicationAttributeConverter conv = new CommunicationAttributeConverter();

        Communication communication = conv.convertToEntityAttribute("facebook");
        assertEquals(communication.toString(), "facebook");
        assertEquals(conv.convertToDatabaseColumn(communication), "facebook");
    }

    @Test
    void linkConverterTest() {
        LinkAttributeConverter conv = new LinkAttributeConverter();

        Link link = conv.convertToEntityAttribute("url");
        assertEquals(link.toString(), "url");
        assertEquals(conv.convertToDatabaseColumn(link), "url");
    }

    @Test
    void nameConverterTest() {
        NameAttributeConverter conv = new NameAttributeConverter();

        Name name = conv.convertToEntityAttribute("Piet");
        assertEquals(name.toString(), "Piet");
        assertEquals(conv.convertToDatabaseColumn(name), "Piet");
    }

    @Test
    void userAffiliationConverterTest() {
        UserAffiliationAttributeConverter conv = new UserAffiliationAttributeConverter();

        UserAffiliation userAffiliation = conv.convertToEntityAttribute("fireman");
        assertEquals(userAffiliation.toString(), "fireman");
        assertEquals(conv.convertToDatabaseColumn(userAffiliation), "fireman");
    }

    @Test
    void toModelUserTest() {
        User userModel = user1.toModelUser();

        assertEquals(user1.getId(), userModel.getId());
        assertEquals(user1.getFirstName().toString(), userModel.getFirstName());
        assertEquals(user1.getLastName().toString(), userModel.getLastName());
        assertEquals(user1.getCommunication().toString(), userModel.getPreferredCommunication());
        assertEquals(user1.getLink().toString(), userModel.getPersonalWebsite());
        assertEquals(user1.getAffiliation().toString(), userModel.getAffiliation());
        assertEquals(user1.getEmail().toString(), userModel.getEmail());
    }



}
