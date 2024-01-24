package com.githab.warehouse.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;

@WebServlet("/warehouses/result")
public class ResultWarehousesServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(ResultWarehousesServlet.class);
    private WarehouseDAO dao;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.dao = (WarehouseDAO) getServletContext().getAttribute("dao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/result.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String warehousesJson = req.getAttribute("warehouses").toString();

        List<Warehouse> warehouses = objectMapper.readValue(warehousesJson, new TypeReference<List<Warehouse>>() {
        });

    }
}
