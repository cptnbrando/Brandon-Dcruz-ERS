package example.dao;

import example.helpers.Utilities;
import example.models.Reimbursement;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class ReimbursementDAOImpl implements ReimbursementDAO
{
    @Override
    public void createNewReimbursementInDB(Reimbursement newReimbursement)
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            //INSERT INTO ers_reimbursement VALUES(000185856351, 53.67, '2021-03-04 09:47:16', null, 'Tacos for some people', NULL, 1294111356, null, 1, 3);
            String sql = "INSERT INTO ers_reimbursement VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            // Statement.RETURN_GENERATED_KEYS will return the PK
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, newReimbursement.getReimbID());
            ps.setBigDecimal(2, newReimbursement.getAmount());
            ps.setTimestamp(3, newReimbursement.getDateSubmitted());
            ps.setNull(4, Types.TIMESTAMP);
            ps.setString(5, newReimbursement.getDescription());
            ps.setNull(6, Types.OTHER);
            ps.setInt(7, newReimbursement.getAuthorID());
            ps.setNull(8, Types.INTEGER);
            ps.setInt(9, newReimbursement.getStatusAsInt());
            ps.setInt(10, newReimbursement.getTypeAsInt());

            ps.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Reimbursement> getUserReimbursementsFromDB(String username)
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            String sql = "SELECT reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description" +
                    ", reimb_receipt, reimb_author, ers_username AS author_username, user_first_name AS author_first_name" +
                    ", user_last_name AS author_last_name, reimb_resolver, reimb_status_id, reimb_type_id FROM ERS_REIMBURSEMENT " +
                    "INNER JOIN ERS_USERS ON ERS_REIMBURSEMENT.reimb_author = ERS_USERS.ers_users_id WHERE REIMB_AUTHOR=" +
                    "(SELECT ERS_USERS_ID FROM ERS_USERS WHERE ERS_USERNAME='" + username + "');";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); //<----query not update

            return resultToReimbursements(rs);
        }catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Reimbursement> getAllReimbursementsFromDB()
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            String sql = "SELECT reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description" +
                    ", reimb_receipt, reimb_author, ers_username AS author_username, user_first_name AS author_first_name" +
                    ", user_last_name AS author_last_name, reimb_resolver, reimb_status_id, reimb_type_id FROM ERS_REIMBURSEMENT " +
                    "INNER JOIN ERS_USERS ON ERS_REIMBURSEMENT.reimb_author = ERS_USERS.ers_users_id;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); //<----query not update

            return resultToReimbursements(rs);
        }catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Reimbursement> getPendingReimbursementsFromDB()
    {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            String sql = "SELECT reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description" +
                    ", reimb_receipt, reimb_author, ers_username AS author_username, user_first_name AS author_first_name" +
                    ", user_last_name AS author_last_name, reimb_resolver, reimb_status_id, reimb_type_id FROM ERS_REIMBURSEMENT " +
                    "INNER JOIN ERS_USERS ON ERS_REIMBURSEMENT.reimb_author = ERS_USERS.ers_users_id WHERE reimb_status_id=1;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); //<----query not update

            return resultToReimbursements(rs);
        }catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Reimbursement> resultToReimbursements(ResultSet rs) throws SQLException
    {
        ArrayList<Reimbursement> reimbursements = new ArrayList<>();

        while(rs.next())
        {
            int authorID = rs.getInt("reimb_author");
            int reimbID = rs.getInt("reimb_id");
            String authorFirstName = rs.getString("author_first_name");
            String authorLastName = rs.getString("author_last_name");
            String authorUsername = rs.getString("author_username");
            BigDecimal amount = rs.getBigDecimal("reimb_amount");
            Timestamp dateSubmitted = rs.getTimestamp("reimb_submitted");
            String description = rs.getString("reimb_description");
            int typeNum = rs.getInt("reimb_type_id");
            Reimbursement.reimbType type = Reimbursement.reimbType.OTHER;
            switch(typeNum){
                case 1:
                    type = Reimbursement.reimbType.LODGING;
                    break;
                case 2:
                    type = Reimbursement.reimbType.FOOD;
                    break;
                case 3:
                    type = Reimbursement.reimbType.TRAVEL;
                    break;
                case 4:
                    type = Reimbursement.reimbType.OTHER;
                    break;
            }

            if(rs.getInt("reimb_resolver") == 0)
            {
                reimbursements.add(new Reimbursement(reimbID, authorID, amount, dateSubmitted, description, type,
                        authorUsername, authorFirstName, authorLastName));
            }
            else
            {
                int resolverID = rs.getInt("reimb_resolver");
                Timestamp dateResolved = rs.getTimestamp("reimb_resolved");
                int statusNum = rs.getInt("reimb_status_id");
                Reimbursement.reimbStatus status = Reimbursement.reimbStatus.PENDING;
                switch(statusNum){
                    case 1:
                        status = Reimbursement.reimbStatus.PENDING;
                        break;
                    case 2:
                        status = Reimbursement.reimbStatus.DENIED;
                        break;
                    case 3:
                        status = Reimbursement.reimbStatus.APPROVED;
                        break;
                }

                reimbursements.add(new Reimbursement(reimbID, authorID, resolverID, amount, dateSubmitted,
                        dateResolved, status, description, type, authorUsername, authorFirstName, authorLastName));
            }
        }

        return reimbursements;
    }

    @Override
    public void updateReimbursementInDB(int reimbursementID, int resolverID, Timestamp resolvedDate, int statusCode) {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            //INSERT INTO ers_reimbursement VALUES(000185856351, 53.67, '2021-03-04 09:47:16', null, 'Tacos for some people', NULL, 1294111356, null, 1, 3);
            String sql = "UPDATE ers_reimbursement SET reimb_resolved=?, reimb_resolver=?, reimb_status_id=? WHERE reimb_id=?;";

            // Statement.RETURN_GENERATED_KEYS will return the PK
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setTimestamp(1, resolvedDate);
            ps.setInt(2, resolverID);
            ps.setInt(3, statusCode);
            ps.setInt(4, reimbursementID);

            ps.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReimbursementFromDB(int reimbursementID) {
        try(Connection conn = DriverManager.getConnection(Utilities.getAwsURL(), Utilities.getAwsUsername(), Utilities.getAwsPassword()))
        {
            //INSERT INTO ers_reimbursement VALUES(000185856351, 53.67, '2021-03-04 09:47:16', null, 'Tacos for some people', NULL, 1294111356, null, 1, 3);
            String sql = "DELETE FROM ers_reimbursement WHERE reimb_id='" + reimbursementID + "';";

            // Statement.RETURN_GENERATED_KEYS will return the PK
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
