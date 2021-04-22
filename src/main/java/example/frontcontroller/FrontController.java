package example.frontcontroller;

import example.dao.UserDAO;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class FrontController
{
    Javalin app;
    Dispatcher dispatcher;

    public FrontController(Javalin app){
        this.app = app;

        app.post("/login", FrontController::login);
        app.get("/logout", FrontController::logout);

//        app.after("/*", FrontController::allowCORS);

        dispatcher = new Dispatcher(app);
    }

    /**
     * Logs a user in
     * Uses UserDAO to check username and password in db
     * Redirects back to login page with a "bad user" header if login was bad
     * @param context the Javalin context
     */
    public static void login(Context context)
    {
        String username = context.formParam("username");
        String password = context.formParam("password");

        if(username == null || password == null)
        {
            context.header("bad", "bad user");
            context.redirect("/");
        }
        else
        {
            if(UserDAO.checkUsernameAndPasswordInDB(username, password))
            {
                context.sessionAttribute("username", username);
//                context.cookie("username", username);
                context.redirect("/dashboard");
            }
            else
            {
                context.header("bad", "bad user");
                context.redirect("/");
            }
        }
    }

    public static void logout(Context context)
    {
        context.sessionAttribute("username", null);
        context.redirect("/");
    }

    /**
     * Checks if there's an active session
     * @param context the Javalin context
     */
    public static void checkSession(Context context)
    {
        if(context.sessionAttribute("username") == null)
        {
            context.redirect("/");
        }
    }

    public static void allowCORS(Context context)
    {
        context.header("Access-Control-Allow-Origin", "*");
    }
}