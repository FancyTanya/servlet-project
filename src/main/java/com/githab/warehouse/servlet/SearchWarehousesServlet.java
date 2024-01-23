package com.githab.warehouse.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.githab.warehouse.dao.WarehouseDAO;
import com.githab.warehouse.model.Warehouse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/warehouses")
public class SearchWarehousesServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(SearchWarehousesServlet.class);
    private WarehouseDAO dao;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.dao = (WarehouseDAO) getServletContext().getAttribute("dao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/searchWarehouses.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String addressLine1 = req.getParameter("addressLine1");
        String addressLine2 = req.getParameter("addressLine2");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String country = req.getParameter("country");
        int inventoryQuantity = Integer.parseInt(req.getParameter("inventoryQuantity"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        int offset = Integer.parseInt(req.getParameter("offset"));

        req.setAttribute("id", id);
        req.setAttribute("name", name);
        req.setAttribute("addressLine1", addressLine1);
        req.setAttribute("addressLine2", addressLine2);
        req.setAttribute("city", city);
        req.setAttribute("state", state);
        req.setAttribute("country", country);
        req.setAttribute("inventoryQuantity", inventoryQuantity);
        req.setAttribute("limit", limit);
        req.setAttribute("offset", offset);

        try {
            List<Warehouse> warehouses = dao.searchWarehouses(id, name, addressLine1, addressLine2, city, state, country, inventoryQuantity, limit, offset, "desc");

            req.setAttribute("warehouses", objectMapper.writeValueAsString(warehouses));
            req.getRequestDispatcher("/WEB-INF/view/result.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
