package example.helpers;

import java.util.UUID;

/**
 * Helper functions for other classes
 *
 * - UUID int generation
 */
public class Utilities
{
    // AWS account info
    // Hard coded here for now, but should be changed to a private solution soon
    private static final String awsURL = "jdbc:postgresql://" + System.getenv("TRAINING_DB_ENDPOINT") + "/project1_ERS";
    private static final String awsUsername = System.getenv("TRAINING_DB_USERNAME");
    private static final String awsPassword = System.getenv("TRAINING_DB_PASSWORD");

    public static int generateUniqueId()
    {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }

    public static String getAwsURL()
    {
        return awsURL;
    }

    public static String getAwsUsername()
    {
        return awsUsername;
    }

    public static String getAwsPassword()
    {
        return awsPassword;
    }
}
