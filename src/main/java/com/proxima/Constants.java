/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   Constants.java                                                           */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/06 11:38:18 by Loïc                                     */
/*   Updated: 2020/11/06 13:26:42 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constants {

    public static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Path CONFIG_PATH = Paths.get(System.getProperty("user.home") + "\\Proxima\\ProximaConfig.json");
}
