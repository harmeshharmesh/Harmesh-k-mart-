package com.myname.mymart.controller;

import com.myname.mymart.dao.UserDAOImpl;
import com.myname.mymart.listener.DataSourceListener;
import com.myname.mymart.model.User;
import com.myname.mymart.service.UserService;
import com.myname.mymart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/api/v1/login")
public class LoginServlet extends HttpServlet {

    private static final int SESSION_TIMEOUT_SECONDS = 30 * 60; // 30 minutes

    private UserService userService;

    @Override
    public void init() {
        userService = new UserService(new UserDAOImpl(this::getConnection));
    }

    private java.sql.Connection getConnection() {
        try {
            return DataSourceListener.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Optional<User> userOpt = userService.authenticate(email, password);
            if (userOpt.isEmpty()) {
                JsonUtil.writeError(resp, 401, "INVALID_CREDENTIALS", "Email or password is incorrect");
                return;
            }
            User user = userOpt.get();

            // Regenerate session ID on login (architecture rule #3) to prevent session fixation.
            HttpSession session = req.getSession(true);
            req.changeSessionId();
            session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole());
            session.setAttribute("name", user.getName());

            JsonUtil.writeSuccess(resp, 200, java.util.Map.of(
                    "id", user.getId(), "name", user.getName(), "role", user.getRole()));
        } catch (SQLException e) {
            JsonUtil.writeError(resp, 500, "SERVER_ERROR", "Login failed");
        }
    }
}
