/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchAllMovies implements Runnable {

    private final AsyncContext acontext;

    public SearchAllMovies(final AsyncContext acontext) {
        this.acontext = acontext;
    }

    @Override
    public void run() {
        HttpServletResponse response = (HttpServletResponse) acontext.getResponse();
        try (InputStream in = new BufferedInputStream(SearchAllMovies.class.getClassLoader().getResourceAsStream("movies.json"))) {
            byte[] buffer = new byte[8];
            int length = 8;
            while ((length = in.read(buffer, 0, length)) > 0) {
                response.getOutputStream().write(buffer, 0, length);
            }
            acontext.complete();
        } catch (IOException ioex) {
            throw new RuntimeException(ioex);
        }
    }
}
