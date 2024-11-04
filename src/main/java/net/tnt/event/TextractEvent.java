package net.tnt.event;

import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import net.tnt.entity.FileUpload;
import net.tnt.service.FileUploadService;
import net.tnt.service.TextractService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TextractEvent {

    @Inject
    TextractService textractService;

    @Inject
    FileUploadService fileUploadService;

    @ConsumeEvent(value = "extract-file", blocking = true)
    public void processEvent(String docName) {
        String jobId = textractService.startDocAnalysisS3(docName);
        if (jobId != null) {
            FileUpload fileUpload = new FileUpload();
            fileUpload.status = "IN_PROGRESS";
            fileUpload.jobId = jobId;
            fileUpload.filename = docName;
            fileUploadService.save(fileUpload);
        }

    }
}