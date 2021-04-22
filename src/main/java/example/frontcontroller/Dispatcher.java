package example.frontcontroller;

import example.controllers.ReimbursementController;
import example.controllers.UserController;
import example.dao.UserDAO;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class Dispatcher
{
    Javalin app;
    static ReimbursementController reimbursementController;
    static UserController userController;

    public Dispatcher(Javalin app)
    {
        this.app = app;
        reimbursementController = new ReimbursementController(app);
        userController = new UserController(app);


        app.before("/api/*", FrontController::checkSession);
        app.before("/dashboard", Dispatcher::checkAllRequests);
    }

    /**
     * Checks for a "user" header in the incoming request
     * @param context the Javalin context
     */
    public static void checkAllRequests(Context context)
    {
        if(context.sessionAttribute("username") == null)
        {
            System.out.println("Bad!");
            context.redirect("/");
        }

        if(!UserDAO.checkUsernameInDB(context.sessionAttribute("username")))
        {
            System.out.println("Bad!");
            context.redirect("/");
        }
    }

    public static ReimbursementController getReimbursementController() {
        return reimbursementController;
    }

    public static UserController getUserController() {
        return userController;
    }
}
