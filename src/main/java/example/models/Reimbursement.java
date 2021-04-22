package example.models;

import example.helpers.Utilities;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * A model for all reimbursements
 *
 * Each contains a reimbID(int), amount(decimal/money), dateSubmitted(timestamp), dateResolved(timestamp),
 * receiptImage(image/BYTEA), author(int that matches a userID), resolver(int that matches a userID),
 * status(enum), and type(enum)
 *
 * - reimbID = UUID with 3 leading 0s
 * - status = 1="Pending, 2="Denied", 3="Approved"
 * - type = 1="LODGING", 2="TRAVEL", 3="FOOD", 4="OTHER"
 */
public class Reimbursement
{
    private int reimbID, authorID, resolverID;
    public enum reimbType {
        LODGING,
        TRAVEL,
        FOOD,
        OTHER
    }
    public enum reimbStatus {
        PENDING,
        DENIED,
        APPROVED
    }
    private BigDecimal amount;
    private Timestamp dateSubmitted, dateResolved;
    private reimbStatus status;
    private reimbType type;
    private String description;

    private String authorUsername, authorFirstName, authorLastName;

    /**
     * Create a new Reimbursement, generates a new reimbursement UUID without a resolver
     *
     * @param authorID the author's userID
     * @param amount the amount
     * @param dateSubmitted the date it was submitted
     * @param description the description
     * @param type type of reimbursement
     * @param authorUsername author's username
     * @param authorFirstName author's first name
     * @param authorLastName author's last name
     */
    public Reimbursement(int authorID, BigDecimal amount, Timestamp dateSubmitted, String description, reimbType type,
                         String authorUsername, String authorFirstName, String authorLastName)
    {
        Integer newUUID = Utilities.generateUniqueId();
        int uuidLength = newUUID.toString().length();
        // Uses String.format to add 3 leading 0s to a newly generated uuid, sets it to the reimbID
        this.reimbID = Integer.parseInt(String.format("%0" + (uuidLength + 3) + "d", newUUID));

        this.authorID = authorID;
        this.resolverID = 0;
        this.amount = amount;
        this.dateSubmitted = dateSubmitted;
        this.dateResolved = null;
        this.status = reimbStatus.PENDING;
        this.type = type;
        this.description = description;

        this.authorFirstName = authorFirstName;
        this.authorLastName  = authorLastName;
        this.authorUsername = authorUsername;
    }

    /**
     * Create a new Reimbursement that was previously created, without a resolver
     *
     * @param reimbID the reimbursementID
     * @param authorID the author's userID
     * @param amount the amount
     * @param dateSubmitted the date it was submitted
     * @param description the description
     * @param type type of reimbursement
     * @param authorUsername author's username
     * @param authorFirstName author's first name
     * @param authorLastName author's last name
     */
    public Reimbursement(int reimbID, int authorID, BigDecimal amount, Timestamp dateSubmitted, String description,
                         reimbType type, String authorUsername, String authorFirstName, String authorLastName)
    {
        this.reimbID = reimbID;
        this.authorID = authorID;
        this.resolverID = 0;
        this.amount = amount;
        this.dateSubmitted = dateSubmitted;
        this.dateResolved = null;
        this.status = reimbStatus.PENDING;
        this.type = type;
        this.description = description;

        this.authorFirstName = authorFirstName;
        this.authorLastName  = authorLastName;
        this.authorUsername = authorUsername;
    }

    /**
     * Creates a new Reimbursement with a resolver
     *
     * @param reimbID the reimbursementID
     * @param authorID the author's userID
     * @param resolverID the resolver's userID
     * @param amount the amount
     * @param dateSubmitted the date it was submitted
     * @param description the description
     * @param type type of reimbursement
     * @param authorUsername author's username
     * @param authorFirstName author's first name
     * @param authorLastName author's last name
     */
    public Reimbursement(int reimbID, int authorID, int resolverID, BigDecimal amount, Timestamp dateSubmitted,
                         Timestamp dateResolved, reimbStatus status, String description, reimbType type,
                         String authorUsername, String authorFirstName, String authorLastName)
    {
        this.reimbID = reimbID;
        this.authorID = authorID;
        this.resolverID = resolverID;
        this.amount = amount;
        this.dateSubmitted = dateSubmitted;
        this.dateResolved = dateResolved;
        this.status = status;
        this.type = type;
        this.description = description;

        this.authorFirstName = authorFirstName;
        this.authorLastName  = authorLastName;
        this.authorUsername = authorUsername;
    }

    public int getReimbID() {
        return reimbID;
    }

    public void setReimbID(int reimbID) {
        this.reimbID = reimbID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public int getResolverID() {
        return resolverID;
    }

    public void setResolverID(int resolverID) {
        this.resolverID = resolverID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Timestamp dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public Timestamp getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(Timestamp dateResolved) {
        this.dateResolved = dateResolved;
    }

    public reimbStatus getStatus() {
        return status;
    }

    public void setStatus(reimbStatus status) {
        this.status = status;
    }

    public reimbType getType() {
        return type;
    }

    public void setType(reimbType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public int getStatusAsInt()
    {
        switch(this.status)
        {
            case PENDING:
                return 1;
            case DENIED:
                return 2;
            case APPROVED:
                return 3;
            default:
                return 1;
        }
    }

    public int getTypeAsInt()
    {
        switch(this.type)
        {
            case LODGING:
                return 1;
            case FOOD:
                return 2;
            case TRAVEL:
                return 3;
            case OTHER:
                return 4;
            default:
                return 4;
        }
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "reimbID=" + reimbID +
                ", authorID=" + authorID +
                ", resolverID=" + resolverID +
                ", amount=" + amount +
                ", dateSubmitted=" + dateSubmitted +
                ", dateResolved=" + dateResolved +
                ", status=" + status +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", authorUsername='" + authorUsername + '\'' +
                ", authorFirstName='" + authorFirstName + '\'' +
                ", authorLastName='" + authorLastName + '\'' +
                '}';
    }
}
