package com.githab.warehouse.restHandlers;

import com.githab.warehouse.dao.WarehouseDAO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class RestApiDeleteHandlerService implements RestApiHandler {
    private final WarehouseDAO dao;

    public RestApiDeleteHandlerService(WarehouseDAO dao) {
        this.dao = dao;
    }

    @Override
    public Optional<String> handleRestRequest(String requestPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest request) {
        long updatedRows = 0;
        if (requestPath.matches("^/warehouses/\\d+$")) {
            String[] parts = requestPath.split("/");
            String taskIdParam = parts[2];

            int taskId = Integer.parseInt(taskIdParam);
            updatedRows = dao.deleteById(taskId);
        }
        return updatedRows;
    }
}
