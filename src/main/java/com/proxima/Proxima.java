/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   Proxima.java                                                             */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/05 14:05:27 by Loïc                                     */
/*   Updated: 2020/11/11 14:12:28 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima;

import com.proxima.api.modules.ModuleManager;
import com.proxima.config.ProximaConfig;
import com.proxima.utils.log.Logger;
import lombok.Getter;
import net.dv8tion.jda.bot.JDABot;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class Proxima {

    private JDABot bot;
    private final ProximaConfig config;
    private final ModuleManager moduleManager;

    private static Proxima instance;


    public static Proxima getInstance(){ return instance; }

    public Proxima(String[] args) {
        AtomicBoolean verbose = new AtomicBoolean(false);

        Arrays.stream(args).forEach(arg -> {
            if (arg.equals("-v") || arg.equals("-verbose"))
                verbose.set(true);
        });

        instance = this;
        config = new ProximaConfig(verbose.get());
        moduleManager = new ModuleManager();

        if (config.TOKEN.equalsIgnoreCase(""))
        {
            Logger.error("Bot token is not set, please define it in the config (" + Constants.CONFIG_PATH + ")");
            System.exit(1);
        }

        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(config.TOKEN);
        builder.setGame(Game.watching("Everything"));
        try {
            bot = builder.buildBlocking().asBot();
        } catch (LoginException | InterruptedException exception){
            Logger.error(exception.getMessage());
            Logger.verboseStackTrace(exception);
        }

        moduleManager.loadModules();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("stop")) { //TODO console command system
                System.out.println("Shutting down...");
                System.exit(0);
            }
        }
        scanner.close();
    }

    public static void main(String[] args)
    {
        new Proxima(args);
    }

    public void shutdown() {
        config.save();
        moduleManager.disableModules();
        bot.getJDA().shutdown();
    }
}