/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.List;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ehsavoie.moviebuddies.model.ComputeUserDistance;
import org.ehsavoie.moviebuddies.model.ComputeUserShared;
import org.ehsavoie.moviebuddies.model.LoadData;
import org.ehsavoie.moviebuddies.model.Movie;
import org.ehsavoie.moviebuddies.model.SearchAllUsers;
import org.ehsavoie.moviebuddies.model.SearchUsersById;
import org.ehsavoie.moviebuddies.model.SearchUsersByName;
import org.ehsavoie.moviebuddies.model.User;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
@WebServlet(name = "Users", urlPatterns = {"/users"}, asyncSupported = true)
public class SearchUsersServlet extends HttpServlet {
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
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        final List<User> users = getUsers(request);
        final String[] params = URLParser.parse("", request);
        final AsyncContext acontext = request.startAsync();
        switch (params[0]) {
            case "search": {
                final int limit;
                if (params.length > 2) {
                    limit = Integer.parseInt(params[2]);
                } else {
                    limit = -1;
                }
                acontext.start(new SearchUsersByName(acontext, params[1], getUsers(request), limit));
            }
            break;
            case "": {
                acontext.start(new SearchAllUsers(acontext));
            }
            break;
            case "share": {
                acontext.start(new ComputeUserShared(acontext, parseInt(params[1]), parseInt(params[2]), users));
            }
            break;
            case "distance": {
                acontext.start(new ComputeUserDistance(acontext, parseInt(params[1]), parseInt(params[2]), users));
            }
            break;
            default: {
                acontext.start(new SearchUsersById(acontext, parseInt(params[0]), users));
            }
            break;
        }
    }

    protected boolean isLimit(int count, int limit) {
        return limit > 0 && count >= limit;
    }

    protected List<Movie> getMovies(ServletRequest request) {
        return (List<Movie>) request.getServletContext().getAttribute(LoadData.LOADED_MOVIES);
    }

    protected List<User> getUsers(ServletRequest request) {
        return (List<User>) request.getServletContext().getAttribute(LoadData.LOADED_USERS);
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
