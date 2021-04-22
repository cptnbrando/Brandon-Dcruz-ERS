package example.dao;

import example.helpers.Utilities;
import example.models.User;

import java.sql.*;

public class UserDAOImpl implements UserDAO {

    public static Boolean userIsManager(String username)
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            String sql = "SELECT * FROM ERS_USERS WHERE ERS_USERNAME='" + username + "';";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); //<----query not update

            if(rs.next())
            {
                return rs.getInt("user_role_id") == 2;
            }

            return false;
        }catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Not available to create new users, users must be added manually into system
     *
     * @param newUser the user to be added
     */
    @Override
    public void createNewUserInDB(User newUser) {

    }

    @Override
    public User getUserFromDB(String username)
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            String sql = "SELECT * FROM ERS_USERS WHERE ERS_USERNAME='" + username + "';";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); //<----query not update

            return resultToUser(rs);
        }catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserFromDB(int userID)
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            String sql = "SELECT * FROM ERS_USERS WHERE ERS_USERS_ID='" + userID + "');";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); //<----query not update

            return resultToUser(rs);
        }catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private User resultToUser(ResultSet rs) throws SQLException
    {
        if(rs.next())
        {
            int userID = rs.getInt("ers_users_id");
            String username = rs.getString("ers_username");
            String password = rs.getString("ers_password");
            String firstName = rs.getString("user_first_name");
            String lastName = rs.getString("user_last_name");
            String email = rs.getString("user_email");

            User.userRole userRole;
            int roleNum = rs.getInt("user_role_id");

            switch(roleNum)
            {
                case 1:
                    userRole = User.userRole.Employee;
                    break;
                case 2:
                    userRole = User.userRole.Manager;
                    break;
                default:
                    userRole = User.userRole.Employee;
            }

            return new User(userID, username, password, firstName, lastName, email, userRole);
        }

        return null;
    }

    @Override
    public void updateUserUsernameInDB(String currentUsername, String newUsername) {

    }

    @Override
    public void updateUserPasswordInDB(String username, String newPassword) {

    }

    @Override
    public void updateUserEmailInDB(String username, String newEmail) {

    }

    @Override
    public void updateUserFullNameInDB(String username, String newFirstName, String newLastName) {

    }

    @Override
    public void updateUserRoleInDB(String username, int newRoleID) {

    }

    @Override
    public void deleteUserFromDB(String username) {

    }

}
