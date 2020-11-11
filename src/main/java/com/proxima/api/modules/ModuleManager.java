/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   ModuleManager.java                                                       */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/11 15:42:04 by Loïc                                     */
/*   Updated: 2020/11/11 17:03:23 by Loïc                                     */
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
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleManager {

    private List<LoadedModule> loadedModules = new ArrayList<>();

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

    /**
     * Charge un module
     * @param file Le fichier du module
     * @param jsonObject Le contenu du fichier 'module.json' du module en question parsé en JSON
     */
    public void loadModule(File file, JSONObject jsonObject) {
        try {
            String mainClass = (String) jsonObject.get("main");
            String moduleName = (String) jsonObject.get("name");
            String moduleVersion = (String) jsonObject.get("version");
            if (moduleName == null || mainClass == null || moduleVersion == null) {
                Logger.error("Could not load module " + file.getName() + ": invalid module.json");
                return;
            }
            URLClassLoader child = new URLClassLoader(
                    new URL[]{file.toURI().toURL()},
                    this.getClass().getClassLoader()
            );
            Class<?> clazz = Class.forName(mainClass, true, child);
            Object instance = clazz.newInstance();
            Method onEnable = clazz.getMethod("onEnable");
            onEnable.invoke(instance);
            loadedModules.add(new LoadedModule(moduleName, moduleVersion, file, clazz, instance));
            Logger.info("Successfully loaded module " + moduleName + " (" + file.getName() + ')');
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | MalformedURLException e) {
            Logger.verboseStackTrace(e);
            Logger.error("Could not load module " + file.getName() + ": invalid main in module.json ?");
        }
    }

    /**
     * Desactive tous les modules
     */
    public void disableModules() {
        loadedModules.stream().forEach(loadedModule -> disableModule(loadedModule.getName()));
    }

    /**
     * Desactive un module
     * @param moduleName Le nom du module a desactiver
     */
    public void disableModule(String moduleName) {
        try {
            LoadedModule loadedModule = getModule(moduleName);
            if (loadedModule == null) {
                Logger.error("Tried to disable non-existent module " + moduleName);
                return;
            }
            Object instance = loadedModule.getModuleInstance();
            Method onDisable = loadedModule.getMainClass().getMethod("onDisable");
            onDisable.invoke(instance);
            loadedModules.remove(loadedModule);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Logger.verboseStackTrace(e);
            Logger.error("Could not disable module " + moduleName + ": " + e.getMessage());
        }
    }

    /**
     * Recupere un module selon son nom
     * @param name Le nom du module recherché
     * @return Le module trouvé ou null
     */
    public LoadedModule getModule(String name)
    {
        return loadedModules.stream().filter(loadedModule -> loadedModule.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}