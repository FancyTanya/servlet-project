CREATE TABLE documents (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           created_at TIMESTAMP NOT NULL,
                           file_data BYTEA NOT NULL
);
