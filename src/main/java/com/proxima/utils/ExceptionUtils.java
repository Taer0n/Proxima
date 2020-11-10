/* ************************************************************************** */
/*                                                                            */
/*                                                                            */
/*   ExceptionUtils.java                                                      */
/*                                                                            */
/*   By: Loïc <lbertran@student.42lyon.fr>                                    */
/*                                                                            */
/*   Created: 2020/11/10 12:41:14 by Loïc                                     */
/*   Updated: 2020/11/10 12:41:14 by Loïc                                     */
/*                                                                            */
/* ************************************************************************** */
package com.proxima.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    /**
     * From org.apache.commons.lang3.exception
     * @param throwable
     * @return
     */
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
