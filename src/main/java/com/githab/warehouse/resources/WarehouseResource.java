package com.githab.warehouse.resources;

import com.githab.warehouse.domain.Warehouse;
import com.githab.warehouse.jdbc.dao.WarehouseDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/warehouse")
public class WarehouseResource {

    private final static Logger logger = LoggerFactory.getLogger(WarehouseResource.class);

    @Context
    private ResourceContext context;

    private final WarehouseDAO warehouseDAO = new WarehouseDAO();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWarehouses() {
        List<Warehouse> warehouseList = warehouseDAO.findAll();
        return Response.ok(warehouseList).build();
    }

    @GET
    @Path("{warehouseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWarehouseById(@PathParam("warehouseId") int warehouseId) {
        Optional<Warehouse> warehouseOptional = warehouseDAO.findById(warehouseId);
        if (warehouseOptional.isPresent()) {
            Warehouse warehouse = warehouseOptional.get();
            return Response.ok(warehouse).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addWarehouse(@QueryParam("id") int id,
                                 @QueryParam("name") String name,
                                 @QueryParam("addressLine1") String addressLine1,
                                 @QueryParam("addressLine2") String addressLine2,
                                 @QueryParam("city") String city,
                                 @QueryParam("state") String state,
                                 @QueryParam("country") String country,
                                 @QueryParam("inventoryQuantity") int inventoryQuantity,
                                 @Context UriInfo uriInfo) {

        Warehouse warehouse = new Warehouse(id, name, addressLine1, addressLine2, city, state, country, inventoryQuantity);
        long warehouseId = warehouseDAO.create(warehouse);

        URI location = UriBuilder.fromPath(uriInfo.getPath())
                .path("/{warehouseId}")
                .resolveTemplate("warehouseId", warehouseId)
                .build();

        return Response.created(location).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateWarehouse(@QueryParam("id") int id,
                                    @QueryParam("name") String name,
                                    @QueryParam("addressLine1") String addressLine1,
                                    @QueryParam("addressLine2") String addressLine2,
                                    @QueryParam("city") String city,
                                    @QueryParam("state") String state,
                                    @QueryParam("country") String country,
                                    @QueryParam("inventoryQuantity") int inventoryQuantity) {
        Warehouse warehouse = new Warehouse(id, name, addressLine1, addressLine2, city, state, country, inventoryQuantity);
        warehouseDAO.update(warehouse);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWarehouse(@PathParam("id") int id) {
        warehouseDAO.deleteById(id);
        return Response.ok().build();
    }
}
