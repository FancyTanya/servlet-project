package com.githab.warehouse.servlet.crud;

import com.githab.warehouse.dao.WarehouseDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete_warehouse")
public class DeleteWarehouseServlet extends HttpServlet {
    private WarehouseDAO dao;

    public DeleteWarehouseServlet() {
    }

    @Override
    public void init() {
        this.dao = (WarehouseDAO) getServletContext().getAttribute("dao");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));

        dao.deleteById(id);
        resp.sendRedirect(req.getContextPath() + "/warehouses");
    }
}
