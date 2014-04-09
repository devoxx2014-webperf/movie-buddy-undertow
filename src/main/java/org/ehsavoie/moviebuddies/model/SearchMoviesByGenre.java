/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.AsyncContext;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchMoviesByGenre implements Runnable {

    private final AsyncContext acontext;
    private final Pattern genre;
    private final List<Movie> allMovies;
    private final int limit;

    public SearchMoviesByGenre(AsyncContext acontext, String genre, List<Movie> allMovies, int limit) {
        this.acontext = acontext;
        this.genre = Pattern.compile(genre.toLowerCase());
        this.allMovies = allMovies;
        this.limit = limit;
    }

    @Override
    public void run() {
        List<String> result = new LinkedList<>();
        int count = 0;
        for (Movie movie : allMovies) {
            if (isLimit(count, limit)) {
                break;
            }
            if (genre.matcher(movie.genre).find()) {
                count++;
                result.add(movie.toString());
            }
        }
        try {
           acontext.getResponse().getWriter().write("[" + String.join(", ", result) + "]");
           acontext.complete();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected boolean isLimit(int count, int limit) {
        return limit > 0 && count >= limit;
    }

}
