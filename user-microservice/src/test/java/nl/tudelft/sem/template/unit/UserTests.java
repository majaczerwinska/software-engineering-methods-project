package nl.tudelft.sem.template.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.domain.user.converters.CommunicationAttributeConverter;
import nl.tudelft.sem.template.domain.user.converters.EmailAttributeConverter;
import nl.tudelft.sem.template.domain.user.converters.LinkAttributeConverter;
import nl.tudelft.sem.template.domain.user.converters.NameAttributeConverter;
import nl.tudelft.sem.template.domain.user.converters.UserAffiliationAttributeConverter;
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
        // Check if the hashCode method is implemented correctly

        // user 1 and user 1 are the same
        assertEquals(user1.hashCode(), user1.hashCode());

        // user1 and user2 have different emails, so different hashcode
        assertNotEquals(user1.hashCode(), user2.hashCode());


    }

    @Test
    void settersAndGettersTest() {
        // Check if setters and getters work as expected

        // Check getEmail
        assertEquals("user1@example.com", user1.getEmail().toString());

        // Check setId
        user1.setId(1L);
        assertEquals(1L, user1.getId());

        // Check setEmail
        user1.setEmail(new Email("email@gmail.com"));
        assertEquals("email@gmail.com", user1.getEmail().toString());

        // Check setFirstName
        user1.setFirstName(new Name("New"));
        assertEquals("New", user1.getFirstName().toString());

        // Check setLastName
        user1.setLastName(new Name("New"));
        assertEquals("New", user1.getLastName().toString());

        // Check setAffiliation
        user1.setAffiliation(new UserAffiliation("New Affiliation"));
        assertEquals("New Affiliation", user1.getAffiliation().toString());

        // Check setLink
        user1.setLink(new Link("http://newlink.com"));
        assertEquals("http://newlink.com", user1.getLink().toString());

        // Check setCommunication
        user1.setCommunication(new Communication("newemail@example.com"));
        assertEquals("newemail@example.com", user1.getCommunication().toString());
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


}
