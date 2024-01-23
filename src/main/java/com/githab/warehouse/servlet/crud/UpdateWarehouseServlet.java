package com.githab.warehouse.servlet.crud;

import com.githab.warehouse.dao.WarehouseDAO;
import com.githab.warehouse.model.Warehouse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/update_warehouse")
public class UpdateWarehouseServlet extends HttpServlet {
    private WarehouseDAO dao;

    @Override
    public void init() {
        dao = (WarehouseDAO) getServletContext().getAttribute("dao");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String addressLine1 = req.getParameter("addressLine1");
        String addressLine2 = req.getParameter("addressLine2");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String country = req.getParameter("country");
        int inventoryQuantity = Integer.parseInt(req.getParameter("inventoryQuantity"));

        final Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        warehouse.setName(name);
        warehouse.setAddressLine1(addressLine1);
        warehouse.setAddressLine2(addressLine2);
        warehouse.setCity(city);
        warehouse.setState(state);
        warehouse.setCountry(country);
        warehouse.setInventoryQuantity(inventoryQuantity);

        dao.update(warehouse);
        resp.sendRedirect(req.getContextPath() + "/warehouses");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String id = req.getParameter("id");
        final Warehouse task = dao.findById(Integer.parseInt(id));

        req.setAttribute("warehouse", task);
        req.getRequestDispatcher("/WEB-INF/view/result.jsp")
                .forward(req, resp);
    }
}
