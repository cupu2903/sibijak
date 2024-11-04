package net.tnt.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.tnt.analysis.PriceAnalysis;
import net.tnt.dto.ProgramRespDto;
import net.tnt.entity.FileUpload;
import net.tnt.entity.Program;
import net.tnt.service.FileUploadService;
import net.tnt.service.ProgramService;
import net.tnt.service.TextractService;

import java.util.ArrayList;
import java.util.List;

@Path("/programs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProgramController {

    @Inject
    ProgramService programService;
    @Inject
    FileUploadService fileUploadService;
    @Inject
    TextractService textractService;

    @Inject
    PriceAnalysis priceAnalysis;

    @GET
    public List<Program> getAll() {
        return programService.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        ProgramRespDto programRespDto = new ProgramRespDto();
        List<FileUpload> fileUploadList = new ArrayList<>();

        Program program = programService.findById(id);
        if (program.status.equals("IN_PROGRESS")) {
            if (program.hpsFilename != null && !program.hpsFilename.isEmpty()) {
                FileUpload hps = fileUploadService.findByFilename(program.hpsFilename);
                if (hps.status.equals("IN_PROGRESS")) {
                    String jobResults = textractService.getJobResults(hps.jobId);
                    if (jobResults != null) {
                        hps.status = "SUCCEEDED";
                        hps.extractText = jobResults;
                        fileUploadService.update(hps.id, hps);
                    }
                }
                fileUploadList.add(hps);

            }
            if (program.rkbmdFilename != null && !program.rkbmdFilename.isEmpty()) {
                FileUpload rkbmd = fileUploadService.findByFilename(program.rkbmdFilename);
                if (rkbmd.status.equals("IN_PROGRESS")) {
                    String jobResults = textractService.getJobResults(rkbmd.jobId);
                    if (jobResults != null) {
                        rkbmd.status = "SUCCEEDED";
                        rkbmd.extractText = jobResults;
                        fileUploadService.update(rkbmd.id, rkbmd);
                    }
                }
                fileUploadList.add(rkbmd);

            }
            if (program.kakFilename != null && !program.kakFilename.isEmpty()) {
                FileUpload kak = fileUploadService.findByFilename(program.kakFilename);
                if (kak.status.equals("IN_PROGRESS")) {
                    String jobResults = textractService.getJobResults(kak.jobId);
                    if (jobResults != null) {
                        kak.status = "SUCCEEDED";
                        kak.extractText = jobResults;
                        fileUploadService.update(kak.id, kak);
                    }
                }
                programRespDto.analysisResult = priceAnalysis.analyzePrice(program.kakFilename, kak.extractText);
                fileUploadList.add(kak);
            }
            program.analysisResult = programRespDto.analysisResult;
            program.status = "COMPLETED";
            programService.update(program.id, program);
        }

        programRespDto.program = program;
        programRespDto.fileUploadList = fileUploadList;
        programRespDto.analysisResult = program.analysisResult;
        return Response.ok(programRespDto).build();
    }

    @POST
    public Response create(Program program) {
        programService.save(program);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Program program) {
        programService.update(id, program);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        programService.delete(id);
        return Response.noContent().build();
    }
}
