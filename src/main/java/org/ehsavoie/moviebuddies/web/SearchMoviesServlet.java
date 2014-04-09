/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import org.ehsavoie.moviebuddies.model.SearchMoviesByGenre;
import org.ehsavoie.moviebuddies.model.SearchAllMovies;
import org.ehsavoie.moviebuddies.model.User;
import org.ehsavoie.moviebuddies.model.Movie;
import org.ehsavoie.moviebuddies.model.LoadData;
import org.ehsavoie.moviebuddies.model.SearchMoviesByTitle;
import java.io.IOException;
import java.util.List;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
@WebServlet(name = "Movies", urlPatterns = {"/movies/search"}, asyncSupported = true)
public class SearchMoviesServlet extends HttpServlet {
    /*
     POST /rates: enregistre un vote d'un utilisateur pour un film. Le body est formatté: {userId: u, movieId: m, rate: r}. renvoie vers /rates/:userId
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
        final List<Movie> movies = getMovies(request);
        final String[] params = URLParser.parse("", request);
        final int limit;
        if (params.length > 2) {
            limit = Integer.parseInt(params[2]);
        } else {
            limit = -1;
        }
        final AsyncContext acontext = request.startAsync();
        switch (params[0]) {
            case "title": {
                acontext.start(new SearchMoviesByTitle(acontext, params[1], getMovies(request), limit));
            }
            break;
            case "actors": {
                acontext.start(new SearchMoviesByGenre(acontext, params[1], getMovies(request), limit));
            }
            break;
            case "genre": {
                 acontext.start(new SearchMoviesByGenre(acontext, params[1], getMovies(request), limit));
            }
            break;
            default: {
                acontext.start(new SearchAllMovies(acontext));
            }
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
