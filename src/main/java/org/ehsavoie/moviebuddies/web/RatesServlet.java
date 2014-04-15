/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.util.Headers;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ehsavoie.moviebuddies.model.JsonItem;
import org.ehsavoie.moviebuddies.model.JsonLoader;
import org.ehsavoie.moviebuddies.model.RateService;
import static org.ehsavoie.moviebuddies.web.StartMovieBuddy.MYAPP;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
@WebServlet(name = "Rates", urlPatterns = {"/rates"}, asyncSupported = true)
public class RatesServlet extends HttpServlet {

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
        String result = RateService.INSTANCE.findRateByUser(Integer.parseInt(params[0]));
        if (result == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.setContentType("application/json");
            response.getWriter().write(result);
        }
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
        String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + MYAPP + RateService.INSTANCE.rateMovie(items.get(0));
        response.setStatus(301);
        response.setHeader(Headers.LOCATION_STRING, returnUrl);
    }

}
