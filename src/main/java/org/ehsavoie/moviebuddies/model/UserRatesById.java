/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import static org.ehsavoie.moviebuddies.model.User.findUserById;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class UserRatesById implements Runnable {

    private final AsyncContext acontext;
    private final int userId;
    private final List<User> allUsers;

    public UserRatesById(AsyncContext acontext, int userId, List<User> allUsers) {
        this.acontext = acontext;
        this.allUsers = allUsers;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            User user = findUserById(userId, allUsers);
            if (user == null) {
                ((HttpServletResponse) acontext.getResponse()).sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                int size = user.rates == null ? 2 : user.rates.size() + 2;
                List<String> result = new LinkedList<>();
                result.add(("{"));
                if (user.rates != null) {
                    for (Entry<Movie, Integer> rate : user.rates.entrySet()) {
                        result.add("'" + rate.getKey().id +"':" + rate.getValue());
                    }
                }
                result.add(("}"));
                acontext.getResponse().getWriter().write(user.toString());
            }
            acontext.complete();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
