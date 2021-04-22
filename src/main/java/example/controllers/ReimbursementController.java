package example.controllers;

import example.dao.UserDAO;
import example.models.Reimbursement;
import example.models.User;
import example.services.ReimbursementServiceImpl;
import example.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ReimbursementController
{
    static ReimbursementServiceImpl reimbursementService;

    public ReimbursementController(Javalin app)
    {
        reimbursementService = new ReimbursementServiceImpl();

        app.routes(() -> {
            path("/api/reimbursements/", () -> {
                get(ReimbursementController::getReimbursements);
                post(ReimbursementController::addNewReimbursement);
                get("pending/", ReimbursementController::getPendingReimbursements);
                path(":reimbursementID/", () -> {
                    patch("approve/", ReimbursementController::approveReimbursement);
                    patch("deny/", ReimbursementController::denyReimbursement);
                });
            });
        });
    }

    /**
     * Checks if the user is a employee or a manager, returns JSON of authorized reimbursements
     * @param context the Javalin context
     */
    public static void getReimbursements(Context context)
    {
        String username = context.sessionAttribute("username").toString();

        if(UserController.userService.userIsManager(username))
        {
            context.json(reimbursementService.getAllReimbursementsFromDB());
        }
        else
        {
            context.json(reimbursementService.getUserReimbursementsFromDB(username));
        }
    }

    public static void addNewReimbursement(Context context)
    {
        String username = context.sessionAttribute("username").toString();
        User theUser = UserController.userService.getUserFromDB(username);
        String amountParam = context.formParam("amount");
        String descriptionParam = context.formParam("description");

        if(amountParam.equals("") || descriptionParam.equals(""))
        {
            context.redirect("/dashboard#newReimbursement");
            return;
        }

        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(amountParam));
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        String typeParam = context.formParam("type");
        Reimbursement.reimbType type;
        switch(typeParam)
        {
            case "FOOD":
                type = Reimbursement.reimbType.FOOD;
                break;
            case "LODGING":
                type = Reimbursement.reimbType.LODGING;
                break;
            case "TRAVEL":
                type = Reimbursement.reimbType.TRAVEL;
                break;
            default:
                type = Reimbursement.reimbType.OTHER;
                break;
        }

        Reimbursement newReim = new Reimbursement(theUser.getUserID(), amount, currentTime, descriptionParam, type,
                theUser.getUsername(), theUser.getFirstName(), theUser.getLastName());
        reimbursementService.createNewReimbursementInDB(newReim);

        context.redirect("/dashboard");
    }

    public static void getPendingReimbursements(Context context)
    {
        context.json(reimbursementService.getPendingReimbursementsFromDB());
    }

    public static void approveReimbursement(Context context){
        int reimbursementID = Integer.parseInt(context.pathParam("reimbursementID"));
        String username = context.sessionAttribute("username").toString();
        int userID = UserDAO.usernameToID(username);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());

        reimbursementService.approveReimbursementInDB(reimbursementID, userID, currentTime);
    }

    public static void denyReimbursement(Context context){
        int reimbursementID = Integer.parseInt(context.pathParam("reimbursementID"));
        String username = context.sessionAttribute("username").toString();
        int userID = UserDAO.usernameToID(username);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());

        reimbursementService.denyReimbursementInDB(reimbursementID, userID, currentTime);
    }
}
