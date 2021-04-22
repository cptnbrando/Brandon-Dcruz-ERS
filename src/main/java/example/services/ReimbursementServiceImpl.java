package example.services;

import example.dao.ReimbursementDAOImpl;

import java.sql.Timestamp;

public class ReimbursementServiceImpl extends ReimbursementDAOImpl implements ReimbursementService
{
    @Override
    public void approveReimbursementInDB(int reimbursementID, int resolverID, Timestamp resolvedDate)
    {
        updateReimbursementInDB(reimbursementID, resolverID, resolvedDate, 3);
    }

    @Override
    public void denyReimbursementInDB(int reimbursementID, int resolverID, Timestamp resolvedDate)
    {
        updateReimbursementInDB(reimbursementID, resolverID, resolvedDate, 2);
    }
}
