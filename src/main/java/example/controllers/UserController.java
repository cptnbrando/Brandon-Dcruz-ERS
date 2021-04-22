package example.controllers;

import example.services.UserServiceImpl;
import io.javalin.Javalin;
import io.javalin.http.Context;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserController
{
    static UserServiceImpl userService;

    public UserController(Javalin app)
    {
        userService = new UserServiceImpl();

        app.routes(() -> {
            path("/api/user", () -> {
                get(UserController::getUser);
            });
        });
    }

    public static void getUser(Context context)
    {
        String username = context.sessionAttribute("username").toString();
        context.json(userService.getUserFromDB(username));
    }

    public static void newReimbursement(Context context)
    {

    }

    public static void updateReimbursement(Context context)
    {

    }
}
