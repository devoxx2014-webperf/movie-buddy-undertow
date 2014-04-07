/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import javax.servlet.ServletException;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class StartMovieBuddy {

    public static final String MYAPP = "/moviebuddy";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ServletException {
        startServer(8080, "localhost", System.getProperty("user.home"));
    }

    public static void startServer(final int port, final String hostName, final String homeDir) throws ServletException {
        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(MovieBuddyServlet.class.getClassLoader())
                .setContextPath(MYAPP)
                .setDeploymentName("moviebuddy.war")
                .addListener(new ListenerInfo(LoadData.class))
                .addServlets(
                        Servlets.servlet("Movies", SearchMoviesServlet.class)
                        .addMapping("/movies/search")
                        .addMapping("/movies/search/*"),
                        Servlets.servlet("Users", SearchUsersServlet.class)
                        .addMapping("/users")
                        .addMapping("/users/*"));

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        PathHandler path = Handlers.path(Handlers.redirect(MYAPP))
                .addPrefixPath(MYAPP, manager.start());

        Undertow server = Undertow.builder()
                .addHttpListener(port, hostName)
                .setHandler(path)
                .build();
        server.start();
    }

}
