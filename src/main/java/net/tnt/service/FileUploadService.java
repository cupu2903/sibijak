package net.tnt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import net.tnt.entity.FileUpload;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FileUploadService {

    public List<FileUpload> listAll() {
        return FileUpload.listAll();
    }

    public FileUpload findById(UUID id) {
        return FileUpload.findById(id);
    }

    public FileUpload findByFilename(String filename) {
        return FileUpload.find("filename", filename).firstResult();
    }

    @Transactional
    public void save(FileUpload fileUpload) {
        fileUpload.createdAt = LocalDateTime.now();  // Set createdAt on creation
        fileUpload.persist();
    }

    @Transactional
    public void update(UUID id, FileUpload fileUpload) {
        FileUpload existingFileUpload = FileUpload.findById(id);
        if (existingFileUpload != null) {
            if (fileUpload.jobId != null && !fileUpload.jobId.isEmpty()) {
                existingFileUpload.jobId = fileUpload.jobId;
            }
            if (fileUpload.status != null && !fileUpload.status.isEmpty()) {
                existingFileUpload.status = fileUpload.status;
            }

            if (fileUpload.extractText != null && !fileUpload.extractText.isEmpty()) {
                existingFileUpload.extractText = fileUpload.extractText;
            }
            existingFileUpload.persist();
        }
    }

    @Transactional
    public void delete(UUID id) {
        FileUpload.deleteById(id);
    }
}
