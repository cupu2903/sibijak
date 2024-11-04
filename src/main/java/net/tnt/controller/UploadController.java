package net.tnt.controller;

import io.vertx.core.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.tnt.service.S3FileService;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Path("/upload")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class UploadController {

    @Inject
    S3FileService s3FileService;

    @Inject
    EventBus eventBus;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormParam("fileName") String fileName,
            @FormParam("fileData") InputStream fileDataStream) {

        java.nio.file.Path tempFile = null;
        try {
            tempFile = Files.createTempFile("upload-", fileName);
            long length = Files.copy(fileDataStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Uploaded file: " + fileName);
            System.out.println("File data length: " + length + " bytes");
            String bucketName = "sibijak";
            String s3Key = fileName;
            String etag = s3FileService.uploadFile(bucketName, tempFile, s3Key);

            eventBus.send("extract-file", s3Key);

            return Response.ok(etag).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("File upload failed: " + e.getMessage()).build();
        }
    }
}
