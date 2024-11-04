package net.tnt.dto;

import jakarta.ws.rs.FormParam;

public class FileUploadForm {

    @FormParam("fileName")
    public String fileName;

    @FormParam("fileData")
    public byte[] fileData;
}

