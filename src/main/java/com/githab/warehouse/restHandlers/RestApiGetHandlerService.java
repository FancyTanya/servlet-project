package com.githab.warehouse.restHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.githab.warehouse.dao.WarehouseDAO;
import com.githab.warehouse.model.Warehouse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public class RestApiGetHandlerService implements RestApiHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WarehouseDAO dao;

    public RestApiGetHandlerService(WarehouseDAO dao) {
        this.dao = dao;
    }

    @Override
    public Optional<String> handleRestRequest(String requestPath) throws JsonProcessingException {
        if (requestPath.matches("^/warehouses/\\d+$")) {
            String warehouseIdParam = parseID(requestPath);
            int warehouseId = Integer.parseInt(warehouseIdParam);
            Warehouse warehouse = dao.findById(warehouseId);
            final String jsonTask = objectMapper.writeValueAsString(warehouse);
            return Optional.ofNullable(jsonTask);

        } else if (requestPath.matches("^/warehouses/$")) {
            final List<Warehouse> allTasks = dao.findAll();
            return Optional.ofNullable(objectMapper.writeValueAsString(allTasks));

        }
        return Optional.empty();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest request) {
        throw new UnsupportedOperationException();
    }

    private String parseID(String requestPath) {
        String[] parts = requestPath.split("/");
        return parts[2];
    }
}
