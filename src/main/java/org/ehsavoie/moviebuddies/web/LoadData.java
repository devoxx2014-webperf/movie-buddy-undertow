/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class LoadData implements ServletContextListener {
    public static final String LOADED_MOVIES = "MOVIES_DB";
    public static final String LOADED_USERS = "USERS_DB";
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (InputStream in = new BufferedInputStream(LoadData.class.getClassLoader().getResourceAsStream("movies.json"))) {
            List<JsonItem> items = JsonLoader.load(in);
            List<Movie> movies = new ArrayList<>(items.size());
            for(JsonItem item : items) {
                movies.add(Movie.parse(item));
            }
            sce.getServletContext().setAttribute(LOADED_MOVIES, movies);
        } catch(IOException ioex){
            throw new RuntimeException(ioex);
        }

        try (InputStream in = new BufferedInputStream(LoadData.class.getClassLoader().getResourceAsStream("users.json"))) {
            List<JsonItem> items = JsonLoader.load(in);
            List<User> users = new ArrayList<>(items.size());
            for(JsonItem item : items) {
                users.add(User.parse(item));
            }
            sce.getServletContext().setAttribute(LOADED_USERS,users);
        }catch(IOException ioex){
            throw new RuntimeException(ioex);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
