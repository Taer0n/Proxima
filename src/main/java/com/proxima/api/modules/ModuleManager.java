/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   ModuleManager.java                                                       */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/11 15:42:04 by Loïc                                     */
/*   Updated: 2020/11/11 15:42:04 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima.api.modules;

import com.proxima.utils.log.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleManager {

    /**
     * Charge tous les jar de modules presents dans le dossier user.dir/modules/
     */
    public void loadModules() {
        Path modulesPath = Paths.get(System.getProperty("user.dir") + "\\modules");
        if (!Files.exists(modulesPath)) {
            try {
                Files.createDirectory(modulesPath);
            } catch (IOException e) {
                Logger.verboseStackTrace(e);
            }
        }
        File modulesDir = new File(modulesPath.toString());
        for (File file : modulesDir.listFiles()) {
            try {
                if (!file.getName().endsWith(".jar")) {
                    Logger.warning("Invalid file found in modules folder: " + file.getName());
                    continue;
                }

                JarFile jarFile = new JarFile(file);
                JarEntry entry = jarFile.getJarEntry("module.json");

                if (entry == null) {
                    Logger.warning("Jar file " + file.getName() + " doesn't contain module.json");
                    continue;
                }
                loadModule(file, (JSONObject) new JSONParser().parse(new InputStreamReader(jarFile.getInputStream(entry))));
            } catch (IOException | ParseException e) {
                Logger.verboseStackTrace(e);
                Logger.error(e.getMessage());
            }
        }
    }

    public void loadModule(File file, JSONObject jsonObject) {
        try {
            String mainClass = (String) jsonObject.get("main");
            URLClassLoader child = new URLClassLoader(
                    new URL[]{file.toURI().toURL()},
                    this.getClass().getClassLoader()
            );
            Class<?> clazz = Class.forName(mainClass, true, child);
            Object instance = clazz.newInstance();
            Method onEnable = clazz.getMethod("onEnable");
            onEnable.invoke(instance);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | MalformedURLException e) {
            Logger.verboseStackTrace(e);
            Logger.error("Could not load module " + file.getName() + ": invalid main in module.json ?");
        }
    }
}
