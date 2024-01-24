package com.githab.warehouse.restHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.githab.warehouse.dao.WarehouseDAO;
import com.githab.warehouse.model.Warehouse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestApiPutHandlerService implements RestApiHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WarehouseDAO dao;

    public RestApiPutHandlerService(WarehouseDAO dao) {
        this.dao = dao;
    }

    @Override
    public Optional<String> handleRestRequest(String requestPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest req) throws IOException {
        long updatedRows = 0;
        if (requestPath.matches("^/warehouses/\\d+$")) {
            String[] parts = requestPath.split("/");
            String warehouseIdParam = parts[2];
            int warehouseId = Integer.parseInt(warehouseIdParam);
            String bodyParams = req.getReader().lines().collect(Collectors.joining());

            Warehouse warehouse = objectMapper.readValue(bodyParams, Warehouse.class);
            warehouse.setId(warehouseId);

            //update Task without Executor
            updatedRows = dao.update(warehouse);
        }

        return updatedRows;
    }
}
