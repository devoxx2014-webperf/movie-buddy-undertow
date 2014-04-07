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
@WebServlet(name = "Movies", urlPatterns = {"/movies/search"})
public class SearchMoviesServlet extends MovieBuddyServlet {
    /*
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
        final List<Movie> movies = getMovies(request);
        String[] params = URLParser.parse("", request);
        final int limit;
        if (params.length > 2) {
            limit = Integer.parseInt(params[2]);
        } else {
            limit = -1;
        }
        switch (params[0]) {
            case "title": {
                Pattern pattern = Pattern.compile(params[1].toLowerCase());
                List<String> result = new LinkedList<>();
                result.add(("["));
                int count = 0;
                for (Movie movie : movies) {
                    if (isLimit(count, limit)) {
                        break;
                    }
                    if (pattern.matcher(movie.title).find()) {
                        count++;
                        result.add(movie.toString());
                    }
                }
                result.add(("]"));
                try {
                    response.getWriter().write(StringUtils.join(result, ","));
                } catch (IOException ex) {
                    throw new ServletException(ex);
                }
            }
            break;
            case "actors": {
                Pattern pattern = Pattern.compile(params[1].toLowerCase());
                List<String> result = new LinkedList<>();
                result.add(("["));
                int count = 0;
                for (Movie movie : movies) {
                    if (isLimit(count, limit)) {
                        break;
                    }
                    if (pattern.matcher(movie.actors).find()) {
                        count++;
                        result.add(movie.toString());
                    }
                }
                result.add(("]"));
                try {
                    response.getWriter().write(StringUtils.join(result, ","));
                } catch (IOException ex) {
                    throw new ServletException(ex);
                }
            }
            break;
            case "genre": {
                Pattern pattern = Pattern.compile(params[1].toLowerCase());
                List<String> result = new LinkedList<>();
                result.add(("["));
                int count = 0;
                for (Movie movie : movies) {
                    if (isLimit(count, limit)) {
                        break;
                    }
                    if (pattern.matcher(movie.genre).find()) {
                        count++;
                        result.add(movie.toString());
                    }
                }
                result.add(("]"));
                try {
                    response.getWriter().write(StringUtils.join(result, ","));
                } catch (IOException ex) {
                    throw new ServletException(ex);
                }
            }
            break;
            default: {
                try (InputStream in = new BufferedInputStream(SearchMoviesServlet.class.getClassLoader().getResourceAsStream("movies.json"))) {
                    byte[] buffer = new byte[8];
                    int length = 8;
                    while ((length = in.read(buffer, 0, length)) > 0) {
                        response.getOutputStream().write(buffer, 0, length);
                    }
                } catch (IOException ioex) {
                    throw new ServletException(ioex);
                }
            }
        }
    }

}
