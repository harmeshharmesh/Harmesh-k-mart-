package com.myname.mymart.controller;

import com.myname.mymart.dao.ReviewDAOImpl;
import com.myname.mymart.listener.DataSourceListener;
import com.myname.mymart.service.ReviewService;
import com.myname.mymart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * GET  /api/v1/reviews?productId=1  -> list reviews + average rating
 * POST /api/v1/reviews              -> submit a review (only for delivered orders)
 * Not under AuthFilter's protected prefixes for GET (public), but POST checks session manually.
 */
@WebServlet("/api/v1/reviews")
public class ReviewServlet extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init() {
        reviewService = new ReviewService(new ReviewDAOImpl(this::getConnection));
    }

    private java.sql.Connection getConnection() {
        try {
            return DataSourceListener.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long productId = Long.parseLong(req.getParameter("productId"));
            JsonUtil.writeSuccess(resp, 200, Map.of(
                    "reviews", reviewService.getReviews(productId),
                    "averageRating", reviewService.getAverageRating(productId)));
        } catch (SQLException | NumberFormatException e) {
            JsonUtil.writeError(resp, 400, "VALIDATION_ERROR", "Valid productId is required");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            JsonUtil.writeError(resp, 401, "UNAUTHENTICATED", "Login required");
            return;
        }
        long userId = (long) session.getAttribute("userId");
        try {
            long productId = Long.parseLong(req.getParameter("productId"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");
            reviewService.submitReview(userId, productId, rating, comment);
            JsonUtil.writeSuccess(resp, 201, Map.of("submitted", true));
        } catch (IllegalArgumentException e) {
            JsonUtil.writeError(resp, 400, "VALIDATION_ERROR", e.getMessage());
        } catch (IllegalStateException e) {
            JsonUtil.writeError(resp, 403, "NOT_ELIGIBLE", e.getMessage());
        } catch (SQLException e) {
            JsonUtil.writeError(resp, 500, "SERVER_ERROR", "Could not submit review");
        }
    }
}
