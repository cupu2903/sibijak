package net.tnt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import net.tnt.entity.RAPBD;

import java.util.List;

@ApplicationScoped
public class RAPBDService {

    public List<RAPBD> listAll() {
        return RAPBD.listAll();
    }

    public RAPBD findById(Long id) {
        return RAPBD.findById(id);
    }

    @Transactional
    public void save(RAPBD rapbd) {
        rapbd.persist();
    }

    @Transactional
    public void update(Long id, RAPBD rapbd) {
        RAPBD existingRAPBD = RAPBD.findById(id);
        if (existingRAPBD != null) {
            if (rapbd.kodeRekening != null && !rapbd.kodeRekening.isEmpty()) {
                existingRAPBD.kodeRekening = rapbd.kodeRekening;
            }
            if (rapbd.uraian != null && !rapbd.uraian.isEmpty()) {
                existingRAPBD.uraian = rapbd.uraian;
            }
            if (rapbd.total != null) {
                if (rapbd.total.sebelumPerubahan != null && !rapbd.total.sebelumPerubahan.isEmpty()) {
                    existingRAPBD.total.sebelumPerubahan = rapbd.total.sebelumPerubahan;
                }
                if (rapbd.total.setelahPerubahan != null && !rapbd.total.setelahPerubahan.isEmpty()) {
                    existingRAPBD.total.setelahPerubahan = rapbd.total.setelahPerubahan;
                }
                if (rapbd.total.berubah != null && !rapbd.total.berubah.isEmpty()) {
                    existingRAPBD.total.berubah = rapbd.total.berubah;
                }
                if (rapbd.total.persen != null && !rapbd.total.persen.isEmpty()) {
                    existingRAPBD.total.persen = rapbd.total.persen;
                }
            }
            if (rapbd.subItems != null && !rapbd.subItems.isEmpty()) {
                existingRAPBD.subItems.clear();
                existingRAPBD.subItems.addAll(rapbd.subItems);
            }
            existingRAPBD.persist();
        }
    }

    @Transactional
    public void delete(Long id) {
        RAPBD.deleteById(id);
    }
}
