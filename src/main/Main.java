package main;

import events.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {

    private static JDA jda;

    public static void main(String[] args) {
        try {
            JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
            jdaBuilder.setToken(Token.getToken());
            jda = jdaBuilder.buildBlocking();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        jda.addEventListener(new Hello(), new Gey(), new Game(), new Online(), new Mute(), new Help(), new Clear(), new MuteTemp());
    }
}
