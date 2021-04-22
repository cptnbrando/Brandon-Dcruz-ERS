package example;

import example.frontcontroller.FrontController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main
{
    public static void main(String[] args)
    {

        Javalin app = Javalin.create(javalinConfig ->
        {
            javalinConfig.addStaticFiles("/", "pages", Location.CLASSPATH);
            javalinConfig.addStaticFiles("/dashboard", "pages/dashboard.html", Location.CLASSPATH);
            javalinConfig.addStaticFiles("/favicon.ico", "pages/favicon.ico", Location.CLASSPATH);
        }).start(9002);

        FrontController fc = new FrontController(app);
    }
}
