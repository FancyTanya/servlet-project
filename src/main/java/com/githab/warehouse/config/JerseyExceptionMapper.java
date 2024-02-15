package com.githab.warehouse.config;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.sql.SQLException;

@Provider
public class JerseyExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception.getCause() instanceof WebApplicationException) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (exception.getCause() instanceof SQLException) {
            SQLException sqlException = (SQLException) exception.getCause();
            String message = "SQL Exception: " + sqlException.getMessage();

            int errorCode = sqlException.getErrorCode();
            if (errorCode == 1062) {
                message = "This entry already exists.";
                return Response.status(Response.Status.CONFLICT)
                        .entity(message)
                        .build();
            } else {
                // other types of sql exceptions
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(message)
                        .build();
            }
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("There is an exception: " + exception.getMessage())
                .build();
    }
}
