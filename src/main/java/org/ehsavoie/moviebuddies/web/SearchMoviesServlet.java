/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ehsavoie.moviebuddies.model.MovieService;
import org.ehsavoie.moviebuddies.model.SearchAllMovies;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
@WebServlet(name = "Movies", urlPatterns = {"/movies"}, asyncSupported = true)
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
        final String[] params = URLParser.parse("", request);
        final int limit = params.length > 3 ? Integer.parseInt(params[3]) : Integer.MAX_VALUE;
        switch (params[0]) {
            case "search":
                final String criteria = params.length > 1 ? params[1] : "";
                switch (criteria) {
                    case "title": {
                        response.getWriter().write(MovieService.INSTANCE.searchMovieByTitle(params[2], limit));
                    }
                    break;
                    case "actors": {
                        response.getWriter().write(MovieService.INSTANCE.searchMovieByActor(params[2], limit));
                    }
                    break;
                    case "genre": {
                        response.getWriter().write(MovieService.INSTANCE.searchMovieByGenre(params[2], limit));
                    }
                    break;
                    default: {
                        final AsyncContext acontext = request.startAsync();
                        acontext.start(new SearchAllMovies(acontext));
                    }
                    break;
                }
                break;
            default: {
                final AsyncContext acontext = request.startAsync();
                acontext.start(new SearchAllMovies(acontext));
            }
            break;
        }
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
