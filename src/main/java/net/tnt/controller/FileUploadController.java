package net.tnt.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.tnt.entity.FileUpload;
import net.tnt.service.FileUploadService;

import java.util.List;
import java.util.UUID;

@Path("/file-uploads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FileUploadController {

    @Inject
    FileUploadService fileUploadService;

    @GET
    public List<FileUpload> getAll() {
        return fileUploadService.listAll();
    }

    @GET
    @Path("/{id}")
    public FileUpload getById(@PathParam("id") UUID id) {
        return fileUploadService.findById(id);
    }

    @POST
    public Response create(FileUpload fileUpload) {
        fileUploadService.save(fileUpload);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, FileUpload fileUpload) {
        fileUploadService.update(id, fileUpload);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        fileUploadService.delete(id);
        return Response.noContent().build();
    }
}
