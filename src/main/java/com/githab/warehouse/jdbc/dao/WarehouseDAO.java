package com.githab.warehouse.jdbc.dao;


import com.githab.warehouse.domain.Warehouse;
import com.githab.warehouse.jdbc.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.githab.warehouse.jdbc.DatabaseManager.getDataSource;

public class WarehouseDAO {
    private final static Logger logger = LoggerFactory.getLogger(WarehouseDAO.class);

    public WarehouseDAO() {
        DatabaseManager.setup();
    }

    public List<Warehouse> searchWarehouses(int id, String name, String addressLine1, String addressLine2, String city, String state, String country, int inventoryQuantity, int limit, int offset, String sortBy) {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM warehouse WHERE id LIKE ? AND name LIKE ? AND address_line_1 LIKE ? AND address_line_2 LIKE ? AND city LIKE ? AND state LIKE ? AND country LIKE ? AND inventory_quantity >= ? ORDER BY " + sortBy + " LIMIT ? OFFSET ?";

        try (PreparedStatement statement = getDataSource().getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, "%" + name + "%");
            statement.setString(3, "%" + addressLine1 + "%");
            statement.setString(4, "%" + addressLine2 + "%");
            statement.setString(5, "%" + city + "%");
            statement.setString(6, "%" + state + "%");
            statement.setString(7, "%" + country + "%");
            statement.setInt(8, inventoryQuantity);
            statement.setInt(9, limit);
            statement.setInt(10, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Warehouse warehouse = new Warehouse();
                warehouses.add(warehouse);
                logger.info("Search warehouses ...");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        logger.info("Search result is: {} warehouses found", warehouses.size());
        return warehouses;
    }


    public int create(Warehouse warehouse) {
        String sql = "INSERT INTO warehouse (id, name, address_line_1, address_line_2, city, state, country, inventory_quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int createdId = 0;

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            statement.setInt(1, warehouse.getId());
            statement.setString(2, warehouse.getName());
            statement.setString(3, warehouse.getAddressLine1());
            statement.setString(4, warehouse.getAddressLine2());
            statement.setString(5, warehouse.getCity());
            statement.setString(6, warehouse.getState());
            statement.setString(7, warehouse.getCountry());
            statement.setInt(8, warehouse.getInventoryQuantity());

            int createdRows = statement.executeUpdate();

            if (createdRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    createdId = generatedKeys.getInt(1);
                    logger.info("Warehouse created with ID: {}", createdId);
                } else {
                    logger.error("Failed to retrieve the generated ID for Warehouse: {}", warehouse.getName());
                }
            } else {
                logger.error("Failed to create Warehouse: {}", warehouse.getName());
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("There is an exception by saving Warehouse: {}", warehouse.getName());

        }
        return createdId;
    }


    public long update(Warehouse warehouse) {
        String sql = "UPDATE warehouse SET name=?, address_line_1=?, address_line_2=?, city=?, state=?, country=?, inventory_quantity=? " +
                "WHERE id=?";
        long updatedRows = 0;

        try (PreparedStatement statement = getDataSource().getConnection().prepareStatement(sql)) {
            statement.setString(1, warehouse.getName());
            statement.setString(2, warehouse.getAddressLine1());
            statement.setString(3, warehouse.getAddressLine2());
            statement.setString(4, warehouse.getCity());
            statement.setString(5, warehouse.getState());
            statement.setString(6, warehouse.getCountry());
            statement.setInt(7, warehouse.getInventoryQuantity());
            statement.setInt(8, warehouse.getId());

            updatedRows = statement.executeUpdate();
            logger.info("Update warehouse by name: {}", warehouse.getName());
        } catch (SQLException e) {
            logger.error("There is an exception by update Warehouse by name: {}", warehouse.getName());
        }
        return updatedRows;
    }

    public long deleteById(int warehouseId) {
        String sql = "DELETE FROM warehouse WHERE id=?";
        long deletedRows = 0;

        try (PreparedStatement statement = getDataSource().getConnection().prepareStatement(sql)) {
            statement.setInt(1, warehouseId);
            deletedRows = statement.executeUpdate();
            logger.info("{} row(s) deleted from Warehouse with ID: {}", deletedRows, warehouseId);
        } catch (SQLException e) {
            logger.error("Delete Warehouse by ID: {} was caused by {}", warehouseId, e.getCause().toString());
        }

        return deletedRows;
    }

    public Optional<Warehouse> findById(int warehouseId) {
        String sql = "SELECT * FROM warehouse WHERE id=?";
        Warehouse warehouse = null;

        try (PreparedStatement statement = getDataSource().getConnection().prepareStatement(sql)) {
            statement.setInt(1, warehouseId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                warehouse = mapResultSetToWarehouse(resultSet);
            }

            logger.info("Find Warehouse by ID : {}", warehouseId);
        } catch (SQLException e) {
            logger.error("Find Warehouse by ID was caused by: {}", e.getCause().toString());
        }

        return Optional.ofNullable(warehouse);
    }

    public List<Warehouse> findAll() {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM warehouse";

        try (PreparedStatement statement = getDataSource().getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Warehouse warehouse = new Warehouse();
                    warehouse.setId(resultSet.getInt("id"));
                    warehouse.setName(resultSet.getString("name"));
                    warehouse.setAddressLine1(resultSet.getString("address_line_1"));
                    warehouse.setAddressLine2(resultSet.getString("address_line_2"));
                    warehouse.setCity(resultSet.getString("city"));
                    warehouse.setState(resultSet.getString("state"));
                    warehouse.setCountry(resultSet.getString("country"));
                    warehouse.setInventoryQuantity(resultSet.getInt("inventory_quantity"));

                    warehouses.add(warehouse);
                }
            }
            logger.info("Find all warehouses, amount: {}", warehouses.size());
        } catch (SQLException e) {
            logger.error("Find all warehouses was caused by {}", e.getCause().toString());
        }
        return warehouses;
    }


    private Warehouse mapResultSetToWarehouse(ResultSet resultSet) throws SQLException {
        return new Warehouse(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("address_line_1"),
                resultSet.getString("address_line_2"),
                resultSet.getString("city"),
                resultSet.getString("state"),
                resultSet.getString("country"),
                resultSet.getInt("inventory_quantity")
        );
    }
}
