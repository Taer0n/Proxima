/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   Logger.java                                                              */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/06 11:27:34 by Loïc                                     */
/*   Updated: 2020/11/06 11:27:34 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima.utils.log;

import com.proxima.Constants;
import com.proxima.Proxima;

import java.util.Date;

public class Logger {

    /**
     * Affiche un message sur la sortie standard
     * @param message Le message à afficher
     * @param logType Le type de log (voir {@link LogType})
     */
    public static void log(String message, LogType logType)
    {
        System.out.println('[' + Constants.DATE_FORMAT.format(new Date()) + "] [" + logType.displayName + ']' + message);
    }

    /**
     * Affiche un log informatif
     * @param message Le message à afficher
     */
    public static void info(String message)
    {
        log(message, LogType.INFO);
    }

    /**
     * Affiche un log d'erreur
     * @param message Le message à afficher
     */
    public static void error(String message)
    {
        log(message, LogType.ERROR);
    }

    /**
     * Affiche un log d'avertissement
     * @param message
     */
    public static void warning(String message)
    {
        log(message, LogType.WARNING);
    }

    /**
     * Affiche un log de debug
     * @param message Le message à afficher
     */
    public static void verbose(String message)
    {
        if (Proxima.getInstance().getConfig().VERBOSE)
            log(message, LogType.VERBOSE);
    }

    enum LogType {

        INFO("Info"),
        ERROR("Error"),
        WARNING("Warning"),
        VERBOSE("Verbose");

        private String displayName;

        LogType(String displayName){
            this.displayName = displayName;
        }
    }
}
