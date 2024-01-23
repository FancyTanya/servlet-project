package com.githab.warehouse.dao;


import com.githab.warehouse.model.Warehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO {
    private final PooledDataSource dataSource;
    private final static Logger logger = LoggerFactory.getLogger(WarehouseDAO.class);

    public WarehouseDAO(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }


    public List<Warehouse> searchWarehouses(int id, String name, String addressLine1, String addressLine2, String city, String state, String country, int inventoryQuantity, int limit, int offset, String sortBy) throws SQLException {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM warehouse WHERE id LIKE ? AND name LIKE ? AND address_line_1 LIKE ? AND address_line_2 LIKE ? AND city LIKE ? AND state LIKE ? AND country LIKE ? AND inventory_quantity >= ? ORDER BY " + sortBy + " LIMIT ? OFFSET ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
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
            throw new RuntimeException(e);
        }

        logger.info("Search result is: {} warehouses found", warehouses.size());
        return warehouses;
    }

    public long create(Warehouse warehouse) {
        String sql = "INSERT INTO warehouse (name, address_line_1, address_line_2, city, state, country, inventory_quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        long createdRows = 0;

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, warehouse.getName());
            statement.setString(2, warehouse.getAddressLine1());
            statement.setString(3, warehouse.getAddressLine2());
            statement.setString(4, warehouse.getCity());
            statement.setString(5, warehouse.getState());
            statement.setString(6, warehouse.getCountry());
            statement.setInt(7, warehouse.getInventoryQuantity());

            createdRows = statement.executeUpdate();
            logger.info("Save warehouse: {}", warehouse);
        } catch (SQLException e) {
            logger.error("There is an exception by saving Warehouse: {}", warehouse.getName());
            e.printStackTrace();
        }
        return createdRows;
    }

    public long update(Warehouse warehouse) {
        String sql = "UPDATE warehouse SET name=?, address_line_1=?, address_line_2=?, city=?, state=?, country=?, inventory_quantity=? " +
                "WHERE id=?";
        long updatedRows = 0;

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
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
            e.printStackTrace();
        }
        return updatedRows;
    }

    public long deleteById(int warehouseId) {
        String sql = "DELETE FROM warehouse WHERE id=?";
        long deletedRows = 0;

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setInt(1, warehouseId);
            deletedRows = statement.executeUpdate();
            logger.info("{} row(s) deleted from Warehouse with ID: {}", deletedRows, warehouseId);
        } catch (SQLException e) {
            logger.error("Delete Warehouse by ID: {} was caused by {}", warehouseId, e.getCause().toString());
            e.printStackTrace();
        }

        return deletedRows;
    }

    public Warehouse findById(int warehouseId) {
        String sql = "SELECT * FROM warehouse WHERE id=?";
        Warehouse warehouse = null;

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setInt(1, warehouseId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                warehouse = mapResultSetToWarehouse(resultSet);
            }

            logger.info("Find Warehouse by ID : {}", warehouseId);
        } catch (SQLException e) {
            logger.error("Find Warehouse by ID was caused by: {}", e.getCause().toString());
            e.printStackTrace();
        }

        return warehouse;
    }

    public List<Warehouse> findAll() {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM warehouse";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Warehouse warehouse = new Warehouse();
                    warehouse.setId(resultSet.getInt("id"));
                    warehouse.setName(resultSet.getString("name"));
                    warehouse.setAddressLine1(resultSet.getString("addressLine1"));
                    warehouse.setAddressLine2(resultSet.getString("addressLine2"));
                    warehouse.setCity(resultSet.getString("city"));
                    warehouse.setState(resultSet.getString("state"));
                    warehouse.setCountry(resultSet.getString("country"));
                    warehouse.setInventoryQuantity(resultSet.getInt("inventoryQuantity"));

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
