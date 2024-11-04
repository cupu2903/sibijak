package net.tnt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import net.tnt.entity.Program;

import java.util.List;

@ApplicationScoped
public class ProgramService {

    public List<Program> listAll() {
        return Program.listAll();
    }

    public Program findById(Long id) {
        return Program.findById(id);
    }

    @Transactional
    public void save(Program program) {
        program.status = "IN_PROGRESS";
        program.persist();
    }

    @Transactional
    public void update(Long id, Program program) {
        Program existingProgram = Program.findById(id);
        if (existingProgram != null) {
            if (program.kodeProgram != null && !program.kodeProgram.isEmpty()) {
                existingProgram.kodeProgram = program.kodeProgram;
            }
            if (program.uraian != null && !program.uraian.isEmpty()) {
                existingProgram.uraian = program.uraian;
            }
            if (program.sumberDana > 0) {
                existingProgram.sumberDana = program.sumberDana;
            }
            if (program.jumlahAnggaran != null && !program.jumlahAnggaran.isEmpty()) {
                existingProgram.jumlahAnggaran = program.jumlahAnggaran;
            }
            if (program.idProgram != null && !program.idProgram.isEmpty()) {
                existingProgram.idProgram = program.idProgram;
            }
            if (program.kodeRekening != null && !program.kodeRekening.isEmpty()) {
                existingProgram.kodeRekening = program.kodeRekening;
            }
            if (program.namaSKPD != null && !program.namaSKPD.isEmpty()) {
                existingProgram.namaSKPD = program.namaSKPD;
            }
            if (program.rkbmdLink != null && !program.rkbmdLink.isEmpty()) {
                existingProgram.rkbmdLink = program.rkbmdLink;
            }
            if (program.hpsLink != null && !program.hpsLink.isEmpty()) {
                existingProgram.hpsLink = program.hpsLink;
            }
            if (program.kakLink != null && !program.kakLink.isEmpty()) {
                existingProgram.kakLink = program.kakLink;
            }
            if (program.analysisResult != null && !program.analysisResult.isEmpty()) {
                existingProgram.analysisResult = program.analysisResult;
            }
            if (program.status != null && !program.status.isEmpty()) {
                existingProgram.status = program.status;
            }
            existingProgram.persist();
        }
    }

    @Transactional
    public void delete(Long id) {
        Program.deleteById(id);
    }
}
