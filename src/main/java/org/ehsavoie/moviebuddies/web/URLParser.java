/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ehsavoie.moviebuddies.web;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class URLParser {
    public static String[] parse(String prefix, HttpServletRequest request) {
        String path = request.getPathInfo();
        if(path == null || path.length() <= prefix.length()) {
            return new String[]{""};
        }
        return path.substring(prefix.length() + 1).split("/");
    }
}
