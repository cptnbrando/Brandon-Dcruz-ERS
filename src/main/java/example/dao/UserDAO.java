package example.dao;

import example.helpers.PasswordAuthentication;
import example.helpers.Utilities;
import example.models.User;

import java.sql.*;

/**
 * DAO to get/update db with User records
 *
 * - Create a new User record
 * - Get a User object from a record
 * - Update User record: username, password, email, first/last name, role
 */
public interface UserDAO
{
    /**
     * Make a new User record in the DB
     * @param newUser the user to be added
     */
    void createNewUserInDB(User newUser);

    /**
     * Get a User object from a username in the DB
     * @param username the username of the User to find
     * @return User object with record info
     */
    User getUserFromDB(String username);

    /**
     * Get a User object from a userID in the DB
     * @param userID the userID of the User to find
     * @return User object with record info
     */
    User getUserFromDB(int userID);

    static int usernameToID(String username)
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            String sql = "SELECT ERS_USERS_ID FROM ERS_USERS WHERE ERS_USERNAME='" + username + "';";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); //<----query not update

            if(rs.next())
            {
                return rs.getInt(1);
            }

            return 0;
        }catch(SQLException e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Login logic for a user
     * @param username the username to check
     * @param password the password, unhashed
     * @return true if user is good, false if not
     */
    static boolean checkUsernameAndPasswordInDB(String username, String password)
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            String sql = "SELECT * FROM ers_users WHERE ers_username=?;";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery(); //<----query not update

            if(rs.next())
            {
                String passHash = rs.getString(3);
                PasswordAuthentication passMan = new PasswordAuthentication();

                return (passMan.authenticate(password.toCharArray(), passHash));
            }

            return false;

        }catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a username exists in the DB
     * @param username the username to check
     * @return whether the user exists or not
     */
    static Boolean checkUsernameInDB(String username)
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            String sql = "SELECT * FROM ERS_USERS WHERE ERS_USERNAME='" + username + "';";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); //<----query not update

            return rs.next();
        }catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Change the username of a user in the DB
     * @param currentUsername the current username
     * @param newUsername the new username
     */
    void updateUserUsernameInDB(String currentUsername, String newUsername);

    /**
     * Change the password of a user in the DB
     * @param username the username of the user
     * @param newPassword the hash of a new password
     */
    void updateUserPasswordInDB(String username, String newPassword);

    /**
     * Change the email of a user in the DB
     * @param username the username of the user
     * @param newEmail the new email address
     */
    void updateUserEmailInDB(String username, String newEmail);

    /**
     * Change the first and last name of a user in the DB
     * @param username the username of the user
     * @param newFirstName new first name
     * @param newLastName new last name
     */
    void updateUserFullNameInDB(String username, String newFirstName, String newLastName);

    /**
     * Change the role of a user in the DB
     * @param username the username of the user
     * @param newRoleID new role ID for user
     */
    void updateUserRoleInDB(String username, int newRoleID);

    /**
     * Delete a user from the DB
     * @param username the username of the user
     */
    void deleteUserFromDB(String username);
}
