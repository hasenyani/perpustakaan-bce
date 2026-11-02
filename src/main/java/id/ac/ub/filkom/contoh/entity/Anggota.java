package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entity: satu objek Anggota = satu baris di tabel "anggota". */
@Entity
@Table(name = "anggota")
public class Anggota {

    @Id
    @Column(name = "id_anggota", length = 10)
    private String idAnggota;

    @Column(name = "nama", length = 100, nullable = false)
    private String nama;

    @Column(name = "batas_pinjam")
    private int batasPinjam;

    protected Anggota() { }            // wajib ada untuk JPA

    public Anggota(String idAnggota, String nama, int batasPinjam) {
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.batasPinjam = batasPinjam;
    }

    public String getIdAnggota() { return idAnggota; }
    public String getNama()      { return nama; }
    public int getBatasPinjam()  { return batasPinjam; }

    public void perbarui(String nama, int batasPinjam) {
        this.nama = nama;
        this.batasPinjam = batasPinjam;
    }
}
