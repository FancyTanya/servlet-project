package com.githab.warehouse.listener;

import com.githab.warehouse.dao.PooledDataSource;
import com.githab.warehouse.dao.WarehouseDAO;
import com.githab.warehouse.restHandlers.*;
import com.githab.warehouse.servlet.crud.AddWarehouseServlet;
import com.githab.warehouse.servlet.crud.DeleteWarehouseServlet;
import com.githab.warehouse.servlet.crud.UpdateWarehouseServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;

@WebListener
public class ApplicationContextListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        PooledDataSource dataSource = null;

        ServletContext servletContext = sce.getServletContext();

        try {
            dataSource = new PooledDataSource();
            WarehouseDAO dao = new WarehouseDAO(dataSource);
            servletContext.setAttribute("dao", dao);
            ServletContextListener.super.contextInitialized(sce);

            RestApiHandler restApiGetHandlerService = new RestApiGetHandlerService(dao);
            RestApiHandler restApiPostHandlerService = new RestApiPostHandlerService(dao);
            RestApiHandler restApiPutHandlerService = new RestApiPutHandlerService(dao);
            RestApiHandler restApiDeleteHandlerService = new RestApiDeleteHandlerService(dao);
            AddWarehouseServlet addWarehouseServlet = new AddWarehouseServlet();
            DeleteWarehouseServlet deleteWarehouseServlet = new DeleteWarehouseServlet();
            UpdateWarehouseServlet updateWarehouseServlet = new UpdateWarehouseServlet();

            servletContext.setAttribute("dao", dao);
            servletContext.setAttribute("restApiGetHandlerService", restApiGetHandlerService);
            servletContext.setAttribute("restApiPostHandlerService", restApiPostHandlerService);
            servletContext.setAttribute("restApiPutHandlerService", restApiPutHandlerService);
            servletContext.setAttribute("restApiDeleteHandlerService", restApiDeleteHandlerService);
            servletContext.setAttribute("addWarehouseServlet", addWarehouseServlet);
            servletContext.setAttribute("deleteWarehouseServlet", deleteWarehouseServlet);
            servletContext.setAttribute("updateWarehouseServlet", updateWarehouseServlet);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
