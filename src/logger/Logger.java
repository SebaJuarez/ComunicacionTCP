package logger;

import java.time.LocalTime;

public class Logger {

    public static void sysoConHora(String mensaje){
        System.out.println("[" + LocalTime.now() + "] "+mensaje);
    }

}
