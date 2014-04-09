/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import java.io.IOException;
import java.util.List;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import static org.ehsavoie.moviebuddies.model.User.findUserById;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchUsersById implements Runnable {

    private final AsyncContext acontext;
    private final int userId;
    private final List<User> allUsers;

    public SearchUsersById(AsyncContext acontext, int userId, List<User> allUsers) {
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
                acontext.getResponse().getWriter().write(user.toString());
            }
            acontext.complete();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
