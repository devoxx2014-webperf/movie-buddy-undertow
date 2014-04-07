/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
@WebServlet(name = "Users", urlPatterns = {"/users"})
public class SearchUsersServlet extends MovieBuddyServlet {
    /*
     GET /users: retourne le fichier users.json
     GET /users/:id: retourne l'utilisateur en JSON tel qu'il est dans le fichier ou la chaine vide: devrait retourner 404 dans ce cas
     GET /users/search/:name/:limit: retourne une liste d'utilisateurs qui satisfont name avec un maximum de limit
     POST /rates: enregistre un vote d'un utilisateur pour un film. Le body est formatté: {userId: u, movieId: m, rate: r}.
     GET /rates/:userid: retourne les votes de userid
     GET /users/share/:userid1/:userid2: retourne la liste de films communs entre userid1 et userid2
     GET /users/distance/:userid1/:userid2: calcul de la distance entre userid1 et userid2. Remarque: le format de la réponse est de type chaine, je ne suis pas sur que cela soit confirme avec JSON

     */

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        final List<User> users = getUsers(request);
        final String[] params = URLParser.parse("", request);
        switch (params[0]) {
            case "search": {
                final Pattern pattern = Pattern.compile(params[1].toLowerCase());
                final int limit = Integer.parseInt(params[2]);
                List<String> result = new LinkedList<>();
                result.add(("["));
                int count = 0;
                for (User user : users) {
                    if (isLimit(count, limit)) {
                        break;
                    }
                    if (pattern.matcher(user.name).find()) {
                        count++;
                        result.add(user.toString());
                    }
                }
                result.add(("]"));
                response.getWriter().write(StringUtils.join(result, ","));
            }
            break;
            case "": {
                try (InputStream in = new BufferedInputStream(SearchUsersServlet.class.getClassLoader().getResourceAsStream("users.json"))) {
                    byte[] buffer = new byte[8];
                    int length = 8;
                    while ((length = in.read(buffer, 0, length)) > 0) {
                        response.getOutputStream().write(buffer, 0, length);
                    }
                } catch (IOException ioex) {
                    throw new ServletException(ioex);
                }
            }
            break;
            default: {
                try {
                    User user = User.findUserById(Integer.parseInt(params[0]), users);
                    if (user == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } else {
                        response.getWriter().write(user.toString());

                    }
                } catch (IOException ex) {
                    throw new ServletException(ex);
                }
            }
            break;
        }
    }

}
