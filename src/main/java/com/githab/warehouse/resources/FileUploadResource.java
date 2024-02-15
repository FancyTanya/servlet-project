package com.githab.warehouse.resources;

import com.githab.warehouse.domain.InputDocument;
import com.githab.warehouse.jdbc.dao.DocumentDao;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Path("/files")
public class FileUploadResource {
    private final static Logger logger = LoggerFactory.getLogger(FileUploadResource.class);

    private final DocumentDao documentDao = new DocumentDao();

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFiles(MultiPart multiPart, @Context UriInfo uriInfo) {
        List<InputDocument> documents = new ArrayList<>();

        for (BodyPart part : multiPart.getBodyParts()) {
            byte[] bytes = readAttachmentFromBodyPart(part);
            FormDataContentDisposition metaData = (FormDataContentDisposition) part.getContentDisposition();

            InputDocument inputDocument = new InputDocument();
            inputDocument.setName(metaData.getName());
            inputDocument.setFileData(bytes);

            documentDao.insertDocument(inputDocument);
            documents.add(inputDocument);
        }

        return Response.ok(documents).build();
    }


    @GET
    @Path("{documentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDocument(@PathParam("documentId") int documentId) {
        InputDocument document = documentDao.getDocumentById(documentId);
        return Response.ok(document).build();
    }

    private byte[] readAttachmentFromBodyPart(BodyPart part) {
        try (InputStream is = part.getEntityAs(InputStream.class)) {
            return readBytes(is);
        } catch (IOException ex) {
            logger.error("There is an exception, during read Input Stream");
            throw new WebApplicationException("There is an exception, during read Input Stream");
        }
    }

    public byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }
}
