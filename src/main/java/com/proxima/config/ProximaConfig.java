/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   ProximaConfig.java                                                       */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/06 11:27:36 by Loïc                                     */
/*   Updated: 2020/11/06 13:26:48 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima.config;

import com.proxima.Constants;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class ProximaConfig extends HashMap<String, String> {

    public boolean VERBOSE;
    public String TOKEN = get("token");

    public ProximaConfig(boolean verbose)
    {
        File file = new File(Constants.CONFIG_PATH.toUri());
        if (!file.exists())
            createConfig(); //La config n'existe pas sur le disque donc on la cree

        VERBOSE = verbose;
    }

    /**
     * Charge la config au lancement du bot
     */
    public void load()
    {
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(Constants.CONFIG_PATH.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sauvegarde la config (TODO: boolean async)
     */
    public void save()
    {

    }

    /**
     * Copie la config du dossier resources du jar vers le disque lors de la premiere utilisation
     */
    private void createConfig()
    {
        try {
            Files.copy(getClass().getResourceAsStream("src/main/resources/ProximaConfig.json"), Constants.CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
