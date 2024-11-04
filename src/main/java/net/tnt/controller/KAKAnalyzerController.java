package net.tnt.controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.tnt.analysis.PriceAnalysis;
import net.tnt.dto.PriceAnalysisDto;
import net.tnt.entity.Program;

import java.util.List;

@Path("/price-analysis")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KAKAnalyzerController {

    @Inject
    PriceAnalysis priceAnalysis;

//    @GET
//    public List<Program> getAll() {
//        return priceAnalysis.listAll();
//    }
//
//    @GET
//    @Path("/{id}")
//    public Program getById(@PathParam("id") Long id) {
//        return priceAnalysis.findById(id);
//    }

    @POST
    public Response create(PriceAnalysisDto priceAnalysisDto) {
        String analysisResult = priceAnalysis.analyzePrice(priceAnalysisDto.rawText, priceAnalysisDto.docToAnalyze);
        return Response.ok()
                .entity("{\"analysisResult\": \"" + analysisResult + "\"}")
                .build();
    }
//
//    @PUT
//    @Path("/{id}")
//    public Response update(@PathParam("id") Long id, Program program) {
//        priceAnalysis.update(id, program);
//        return Response.ok().build();
//    }
//
//    @DELETE
//    @Path("/{id}")
//    public Response delete(@PathParam("id") Long id) {
//        priceAnalysis.delete(id);
//        return Response.noContent().build();
//    }
}
