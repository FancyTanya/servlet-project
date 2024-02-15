package com.githab.warehouse.jdbc.dao;

import com.githab.warehouse.domain.InputDocument;
import com.githab.warehouse.jdbc.DatabaseManager;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentDao {

    private final static Logger logger = LoggerFactory.getLogger(DocumentDao.class);

    private static final String INSERT_DOCUMENT_SQL = "INSERT INTO documents (name, created_at, file_data) VALUES (?, ?, ?)";
    private static final String SELECT_DOCUMENT_BY_ID_SQL = "SELECT id, name, created_at, file_data FROM documents WHERE id = ?";
    private static final String SELECT_ALL_DOCUMENTS_SQL = "SELECT id, name, created_at, file_data FROM documents";
    private static final String UPDATE_DOCUMENT_SQL = "UPDATE documents SET name = ?, created_at = ?, file_data = ? WHERE id = ?";
    private static final String DELETE_DOCUMENT_SQL = "DELETE FROM documents WHERE id = ?";

    public DocumentDao() {
        DatabaseManager.setup();
    }

    public InputDocument getDocumentById(int id) {
        InputDocument document = null;

        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DOCUMENT_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Date createdAt = resultSet.getDate("created_at");
                byte[] fileData = resultSet.getBytes("file_data");
                document = new InputDocument();
                document.setName(name);
                document.setCreatedAt(createdAt);
                document.setFileData(fileData);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return document;
    }

    @SneakyThrows
    public void insertDocument(InputDocument document) {
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DOCUMENT_SQL)) {

            connection.setAutoCommit(false);

            preparedStatement.setString(1, document.getName());
            preparedStatement.setDate(2, new java.sql.Date(document.getCreatedAt().getTime()));
            preparedStatement.setBytes(3, document.getFileData());
            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException();
        }
    }

    public List<InputDocument> findAllDocuments() {
        List<InputDocument> documents = new ArrayList<>();
        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_DOCUMENTS_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Date createdAt = resultSet.getDate("created_at");
                byte[] fileData = resultSet.getBytes("file_data");
                InputDocument document = new InputDocument();
                document.setId(id);
                document.setName(name);
                document.setCreatedAt(createdAt);
                document.setFileData(fileData);
                documents.add(document);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return documents;
    }

    public boolean updateDocument(InputDocument document) {
        boolean rowUpdated = false;

        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DOCUMENT_SQL)) {

            preparedStatement.setString(1, document.getName());
            preparedStatement.setDate(2, new java.sql.Date(document.getCreatedAt().getTime()));
            preparedStatement.setBytes(3, document.getFileData());
            preparedStatement.setInt(4, document.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowUpdated;
    }

    public boolean deleteDocument(int id) {
        boolean isDeleted = false;

        try (Connection connection = DatabaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DOCUMENT_SQL)) {

            preparedStatement.setInt(1, id);
            isDeleted = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return isDeleted;
    }


}
