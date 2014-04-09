/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.io.IOException;
import java.util.List;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ehsavoie.moviebuddies.model.JsonItem;
import org.ehsavoie.moviebuddies.model.JsonLoader;
import org.ehsavoie.moviebuddies.model.LoadData;
import org.ehsavoie.moviebuddies.model.Movie;
import org.ehsavoie.moviebuddies.model.RateMovie;
import org.ehsavoie.moviebuddies.model.User;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
@WebServlet(name = "Rates", urlPatterns = {"/rates"}, asyncSupported = true)
public class RatesServlet extends HttpServlet {

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
        final String[] params = URLParser.parse("", request);
        final AsyncContext acontext = request.startAsync();
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
        List<JsonItem> items = JsonLoader.load(request.getInputStream());
        final AsyncContext acontext = request.startAsync();
        acontext.start(new RateMovie(acontext, items.get(0), getUsers(request), getMovies(request)));
    }

}
