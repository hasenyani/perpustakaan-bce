package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entity: satu objek Buku = satu baris di tabel "buku". stok = eksemplar yang tersedia. */
@Entity
@Table(name = "buku")
public class Buku {

    @Id
    @Column(name = "id_buku", length = 10)
    private String idBuku;

    @Column(name = "judul", length = 200, nullable = false)
    private String judul;

    @Column(name = "stok")
    private int stok;

    protected Buku() { }               // wajib ada untuk JPA

    public Buku(String idBuku, String judul, int stok) {
        this.idBuku = idBuku;
        this.judul = judul;
        this.stok = stok;
    }

    public String getIdBuku() { return idBuku; }
    public String getJudul()  { return judul; }
    public int getStok()      { return stok; }

    public void perbarui(String judul, int stok) {
        this.judul = judul;
        this.stok = stok;
    }

    /** Dipanggil saat buku dipinjam. */
    public void kurangiStok() {
        if (stok <= 0) {
            throw new IllegalStateException("Stok buku habis");
        }
        stok--;
    }

    /** Dipanggil saat buku dikembalikan. */
    public void tambahStok() {
        stok++;
    }
}
