package net.tnt.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Entity
public class RAPBD extends PanacheEntity {

    public String kodeRekening;
    public String uraian;

    @Embedded
    public Total total;

    @ElementCollection
    public List<RAPBDSubItem> subItems;

    @Embeddable
    public static class Total {
        public String sebelumPerubahan;
        public String setelahPerubahan;
        public String berubah;
        public String persen;
    }

    @Embeddable
    public static class RAPBDSubItem {
        public String kodeRekening;
        public String uraian;
        public Total total;
    }
}
