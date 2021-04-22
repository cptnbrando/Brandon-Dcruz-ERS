package example.models;

import example.helpers.PasswordAuthentication;
import example.helpers.Utilities;

/**
 * A model for a User, pre-populated with info from the database for each entity
 *
 * Holds ID(Number), Username(String), Password(String, hashed), First/Last Name(String), Email(String),
 * and Role(String)
 *
 * - Passwords should be hashed in Java and securely stored in DB
 *
 */
public class User
{
    private int userID;
    public enum userRole {
        Employee,
        Manager
    }

    private userRole role;
    private String username, password, firstName, lastName, email;

    public User(){

    }

    public User(String username, String password, String firstName, String lastName, String email, userRole role) {
        this.userID = Utilities.generateUniqueId();
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public User(int userID, String username, String password, String firstName, String lastName, String email, userRole role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Uses the PasswordAuthentication helper class to generate a new hash from a given password String
     * @param password string to hash
     * @return hashed string
     */
    public String passwordHash(String password)
    {
        return new PasswordAuthentication().hash(password.toCharArray());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public userRole getRole() {
        return role;
    }

    public void setRole(userRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
