package example.dao;

import example.models.Reimbursement;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * DAO to get/update db with Reimbursement records
 *
 * - Create a new Reimbursement record
 * - Get a Reimbursement object from a record
 * - Approve a Reimbursement in the DB
 * - Deny a Reimbursement in the DB
 * - Delete a Reimbursement from the DB
 */
public interface ReimbursementDAO {
    /**
     * Add a new Reimbursement record to the DB
     * @param newReimbursement the Reimbursement to be added
     */
    void createNewReimbursementInDB(Reimbursement newReimbursement);

    /**
     * Get an ArrayList of Reimbursements from a username in the DB
     * @param username the user
     * @return ArrayList of Reimbursement objects with record info
     */
    ArrayList<Reimbursement> getUserReimbursementsFromDB(String username);

    /**
     * Get an ArrayList of all Reimbursements in the DB
     * @return ArrayList of Reimbursement objects with record info
     */
    ArrayList<Reimbursement> getAllReimbursementsFromDB();

    /**
     * Approve or deny a Reimbursement in the DB
     * @param reimbursementID the ID of the reimbursement
     * @param resolverID the ID of the resolver
     * @param resolvedDate the date of resolution
     * @param statusCode whether to approve or deny the reimbursement
     */
    public void updateReimbursementInDB(int reimbursementID, int resolverID, Timestamp resolvedDate, int statusCode);

    /**
     * Delete a reimbursement from the DB
     * @param reimbursementID the ID of the reimbursement to be deleted
     */
    void deleteReimbursementFromDB(int reimbursementID);
}
