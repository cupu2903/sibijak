package net.tnt.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.tnt.entity.RAPBD;
import net.tnt.service.RAPBDService;

import java.util.List;

@Path("/rapbd")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RAPBDController {

    @Inject
    RAPBDService rapbdService;

    @GET
    public List<RAPBD> getAll() {
        return rapbdService.listAll();
    }

    @GET
    @Path("/{id}")
    public RAPBD getById(@PathParam("id") Long id) {
        return rapbdService.findById(id);
    }

    @POST
    public Response create(RAPBD rapbd) {
        rapbdService.save(rapbd);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, RAPBD rapbd) {
        rapbdService.update(id, rapbd);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        rapbdService.delete(id);
        return Response.noContent().build();
    }
}
