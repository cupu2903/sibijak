package net.tnt.entity;

import com.azure.search.documents.indexes.SearchableField;
import com.azure.search.documents.indexes.SimpleField;

import java.time.Year;

public class ProgramRelatedDocument {

    @SimpleField(isKey = true, isFilterable = true)
    private String id; // Unique identifier for each document

    @SearchableField(isFilterable = true, isSortable = true)
    private String filename; // Name of the file in storage

    @SearchableField(isFilterable = true, isSortable = true)
    private String programName; // Program name related to the document

    @SimpleField(isFilterable = true, isSortable = true)
    private int year; // Year of the document

    @SearchableField(isFilterable = true, isSortable = true)
    private String region; // Region associated with the document

    @SearchableField(isFilterable = true, isSortable = true)
    private String documentType; // Type of document (e.g., "SSH", "Historical", "MarketPrice")

    @SearchableField(isFilterable = true, isSortable = true)
    private String content; // Main content or text extracted from the document

    // Constructors
    public ProgramRelatedDocument() {
    }

    public ProgramRelatedDocument(String id, String filename, String programName, int year, String region, String documentType, String content) {
        this.id = id;
        this.filename = filename;
        this.programName = programName;
        this.year = year;
        this.region = region;
        this.documentType = documentType;
        this.content = content;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ProgramRelatedDocument{" +
                "id='" + id + '\'' +
                ", filename='" + filename + '\'' +
                ", programName='" + programName + '\'' +
                ", year=" + year +
                ", region='" + region + '\'' +
                ", documentType='" + documentType + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
