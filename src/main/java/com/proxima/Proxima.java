/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   Proxima.java                                                             */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/05 14:05:27 by Loïc                                     */
/*   Updated: 2020/11/06 13:26:37 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima;

import com.proxima.config.ProximaConfig;
import com.proxima.utils.ExceptionUtils;
import com.proxima.utils.log.Logger;
import lombok.Getter;
import net.dv8tion.jda.bot.JDABot;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class Proxima {

    private JDABot bot;
    private ProximaConfig config;
    private static Proxima instance;

    public static Proxima getInstance(){ return instance; }

    public Proxima(String[] args) throws LoginException, InterruptedException {
        AtomicBoolean verbose = new AtomicBoolean(false);

        Arrays.stream(args).forEach(arg -> {
            if (arg.equals("-v") || arg.equals("-verbose"))
                verbose.set(true);
        });

        instance = this;
        config = new ProximaConfig(verbose.get());

        if (config.TOKEN == null)
        {
            Logger.error("Bot token is not set, please define it in the config (" + Constants.CONFIG_PATH + ")");
            System.exit(1);
        }

        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(config.TOKEN);
        builder.setGame(Game.watching("Everything"));
        try {
            bot = builder.buildBlocking().asBot();
        } catch (LoginException exception){
            Logger.error(exception.getMessage());
            Logger.verboseStackTrace(exception);
        }
    }

    public static void main(String[] args)
    {
        try {
            new Proxima(args);
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}