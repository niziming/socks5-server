package ziming.socks5.aio.utils;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Log {
    public static void log(String message, Object ...args){
        Date date = new Date();
        String msg = String.format("%1$tF %1$tT %2$-3s %3$s%n", date, "线程ID:" + Thread.currentThread().getId(), String.format(message, args));
        System.out.println(">>  " + msg);
    }
}
