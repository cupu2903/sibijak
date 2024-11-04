package net.tnt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigInteger;
import java.util.UUID;

@Entity
public class Program extends PanacheEntity {

    public String kodeProgram;
    public String uraian;
    public int sumberDana;
    public String jumlahAnggaran;
    public String idProgram;
    public String kodeRekening;
    public String namaSKPD;
    public String rkbmdLink;
    public String hpsLink;
    public String kakLink;
    public String hpsFilename;
    public String rkbmdFilename;
    public String kakFilename;

    @Column(columnDefinition = "TEXT")
    public String analysisResult;

    public String status;
}
