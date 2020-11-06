/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   Proxima.java                                                             */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/05 14:05:27 by Loïc                                     */
/*   Updated: 2020/11/05 14:05:27 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima;

import com.proxima.config.ProximaConfig;
import com.proxima.utils.log.Logger;
import lombok.Getter;
import net.dv8tion.jda.bot.JDABot;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.json.simple.JSONObject;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.Objects;
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

        Objects.requireNonNull(config.TOKEN, "Bot token cannot be null");

        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(config.TOKEN);
        builder.setGame(Game.watching("Everything"));
        bot = builder.buildBlocking().asBot();

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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", "");
        System.out.println(jsonObject.toJSONString());
    }
}
