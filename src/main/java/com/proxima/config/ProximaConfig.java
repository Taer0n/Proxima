/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   ProximaConfig.java                                                       */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/06 11:27:36 by Loïc                                     */
/*   Updated: 2020/11/12 16:45:02 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima.config;

import com.proxima.Constants;
import com.proxima.utils.log.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class ProximaConfig {

    public boolean VERBOSE;
    public String TOKEN;

    public ProximaConfig(boolean verbose) {
        //La config n'existe pas sur le disque donc on la cree
        if (!Files.exists(Constants.CONFIG_PATH)) {
            try {
                Files.createDirectories(Constants.CONFIG_PATH);
                createConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        load();
        //VERBOSE = verbose;
        VERBOSE = true; //temp
    }

    /**
     * Charge la config au lancement du bot
     */
    public void load() {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(new FileInputStream(Constants.CONFIG_PATH.toString())));
            Arrays.stream(getClass().getFields())
                    .filter(field -> !field.getName().equals("VERBOSE"))
                    .forEach(field -> {
                        try {
                            field.set(this, jsonObject.get(field.getName().toLowerCase()));
                            Logger.info("(Config) " + field.getName().toLowerCase() + '=' + jsonObject.get(field.getName().toLowerCase()));
                        } catch (IllegalAccessException e) {
                            Logger.error("Failed to load config option " + field.getName() + ": field not found in config !");
                            e.printStackTrace();
                        }
                    });
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sauvegarde la config (TODO: boolean async)
     */
    public void save() {
        JSONObject jsonObject = new JSONObject();
        Arrays.stream(getClass().getFields())
                .filter(field -> !field.getName().equals("VERBOSE"))
                .forEach(field -> {
                    try {
                        jsonObject.put(field.getName().toLowerCase(), field.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        Logger.info("Config saved successfully");
        Logger.verbose(jsonObject.toJSONString());
    }

    /**
     * Copie la config du dossier resources du jar vers le disque lors de la premiere utilisation
     */
    private void createConfig() {
        try {
            Files.copy(getClass().getClassLoader().getResourceAsStream("ProximaConfig.json"),
                    Constants.CONFIG_PATH,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
