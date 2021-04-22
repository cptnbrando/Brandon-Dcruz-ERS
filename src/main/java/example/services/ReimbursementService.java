package example.services;

import java.sql.Timestamp;

/**
 * Business logic that concerns DB for Reimbursement records
 *
 * -
 */
public interface ReimbursementService {

    /**
     * Approve a reimbursement in the DB
     * @param reimbursementID the ID of the reimbursement
     * @param resolverID the ID of the resolver
     * @param resolvedDate the date of resolution
     */
    void approveReimbursementInDB(int reimbursementID, int resolverID, Timestamp resolvedDate);

    /**
     * Deny a reimbursement in the DB
     * @param reimbursementID the ID of the reimbursement
     * @param resolverID the ID of the resolver
     * @param resolvedDate the date of resolution
     */
    void denyReimbursementInDB(int reimbursementID, int resolverID, Timestamp resolvedDate);
}
