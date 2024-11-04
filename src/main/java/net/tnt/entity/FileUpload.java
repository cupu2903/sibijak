package net.tnt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class FileUpload extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false)
    public String jobId;

    @Column(nullable = false)
    public String status;

    @Column(nullable = false)
    public String filename;
    @Column(columnDefinition = "TEXT")
    public String extractText;

    @Column(nullable = false)
    public LocalDateTime createdAt;

}
