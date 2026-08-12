package com.myname.mymart.controller;

import com.myname.mymart.dao.ProductDAOImpl;
import com.myname.mymart.listener.DataSourceListener;
import com.myname.mymart.model.Product;
import com.myname.mymart.service.ProductService;
import com.myname.mymart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

/**
 * GET  /api/v1/products              -> search/browse (F3)
 * GET  /api/v1/products?id=1         -> single product
 * POST /api/v1/products              -> seller creates listing (F2)
 * PUT-via-POST /api/v1/products?action=update -> seller edits listing
 * POST /api/v1/products?action=delete -> seller deletes listing
 */
@WebServlet("/api/v1/products")
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService(new ProductDAOImpl(this::getConnection));
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
            String idParam = req.getParameter("id");
            if (idParam != null) {
                Optional<Product> product = productService.get(Long.parseLong(idParam));
                if (product.isPresent()) {
                    JsonUtil.writeSuccess(resp, 200, product.get());
                } else {
                    JsonUtil.writeError(resp, 404, "NOT_FOUND", "Product not found");
                }
                return;
            }
            String keyword = req.getParameter("q");
            String category = req.getParameter("category");
            JsonUtil.writeSuccess(resp, 200, productService.search(keyword, category));
        } catch (SQLException e) {
            JsonUtil.writeError(resp, 500, "SERVER_ERROR", "Could not load products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null
                || !"SELLER".equals(session.getAttribute("role"))) {
            JsonUtil.writeError(resp, 403, "FORBIDDEN", "Seller login required");
            return;
        }
        long sellerId = (long) session.getAttribute("userId");
        String action = req.getParameter("action");

        try {
            if ("delete".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                boolean deleted = productService.deleteListing(id, sellerId);
                if (deleted) JsonUtil.writeSuccess(resp, 200, java.util.Map.of("deleted", true));
                else JsonUtil.writeError(resp, 404, "NOT_FOUND", "Listing not found or not owned by you");
                return;
            }

            if ("update".equals(action)) {
                Product p = new Product();
                p.setId(Long.parseLong(req.getParameter("id")));
                p.setSellerId(sellerId);
                p.setName(req.getParameter("name"));
                p.setDescription(req.getParameter("description"));
                p.setPrice(new BigDecimal(req.getParameter("price")));
                p.setStockQty(Integer.parseInt(req.getParameter("stockQty")));
                p.setCategory(req.getParameter("category"));
                p.setImageUrl(req.getParameter("imageUrl"));
                boolean updated = productService.updateListing(p);
                if (updated) JsonUtil.writeSuccess(resp, 200, p);
                else JsonUtil.writeError(resp, 404, "NOT_FOUND", "Listing not found or not owned by you");
                return;
            }

            // create
            Product created = productService.createListing(
                    sellerId,
                    req.getParameter("name"),
                    req.getParameter("description"),
                    new BigDecimal(req.getParameter("price")),
                    Integer.parseInt(req.getParameter("stockQty")),
                    req.getParameter("category"),
                    req.getParameter("imageUrl"));
            JsonUtil.writeSuccess(resp, 201, created);
        } catch (IllegalArgumentException e) {
            JsonUtil.writeError(resp, 400, "VALIDATION_ERROR", e.getMessage());
        } catch (SQLException e) {
            JsonUtil.writeError(resp, 500, "SERVER_ERROR", "Could not save listing");
        }
    }
}
