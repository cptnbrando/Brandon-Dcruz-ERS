package daoTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOImplTest
{
    @BeforeEach
    void setUp() {
        System.out.println("You did good!");
    }

    @AfterEach
    void tearDown() {
        System.out.println("You did good!");
    }

    @Test
    void userIsManager() {
        assertEquals("0", "0");
    }

    @Test
    void createNewUserInDB() {
        assertEquals("0", "0");
    }

    @Test
    void getUserFromDB() {
        assertEquals("0", "0");
    }

    @Test
    void testGetUserFromDB() {
        assertEquals("0", "0");
    }
}