package net.tnt.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import net.tnt.entity.FileUpload;
import net.tnt.entity.Program;

import java.util.List;

@RegisterForReflection
public class ProgramRespDto {

    public Program program;
    public List<FileUpload> fileUploadList;
    public String analysisResult;
}
