/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   LoadedModule.java                                                        */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/11 16:56:16 by Loïc                                     */
/*   Updated: 2020/11/11 16:56:16 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima.api.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@Getter @AllArgsConstructor
public class LoadedModule {

    private String name;
    private String version;
    private File moduleFile;
    private Class mainClass;
    private Object moduleInstance;
}
