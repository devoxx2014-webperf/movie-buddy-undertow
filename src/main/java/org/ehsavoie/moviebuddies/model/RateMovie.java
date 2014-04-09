/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import static org.ehsavoie.moviebuddies.model.Movie.findMovieById;
import static org.ehsavoie.moviebuddies.web.StartMovieBuddy.MYAPP;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class RateMovie implements Runnable {
    private final AsyncContext ac;
    private final JsonItem item;
    private final List<User> allUsers;
    private final List<Movie> allMovies;

    public RateMovie(AsyncContext ac, JsonItem item, List<User> allUsers, List<Movie> allMovies) {
        this.ac = ac;
        this.item = item;
        this.allUsers = allUsers;
        this.allMovies = allMovies;
    }

    @Override
    public void run() {

        User user = User.findUserById(item.getInt("userId"), allUsers);
        Movie movie = findMovieById(item.getInt("movieId"), allMovies);
        if (user.rates == null) {
            user.rates = new HashMap<>();
        }
        user.rates.put(movie, item.getInt("rate"));
        try {
            ((HttpServletResponse)ac.getResponse()).sendRedirect(MYAPP + "/rates/" + user.id);
            ac.complete();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
