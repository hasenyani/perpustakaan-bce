package id.ac.ub.filkom.contoh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Entity: satu transaksi peminjaman (Anggota meminjam Buku).
 * tglKembali == null berarti buku belum dikembalikan.
 * Aturan tenggat dan denda disimpan di sini karena merupakan aturan domain.
 */
@Entity
@Table(name = "peminjaman")
public class Peminjaman {

    public static final int LAMA_PINJAM_HARI = 7;
    public static final long DENDA_PER_HARI = 1000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peminjaman")
    private Long idPeminjaman;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_anggota", nullable = false)
    private Anggota anggota;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_buku", nullable = false)
    private Buku buku;

    @Column(name = "tgl_pinjam", nullable = false)
    private LocalDate tglPinjam;

    @Column(name = "tgl_jatuh_tempo", nullable = false)
    private LocalDate tglJatuhTempo;

    @Column(name = "tgl_kembali")
    private LocalDate tglKembali;

    @Column(name = "denda")
    private long denda;

    protected Peminjaman() { }         // wajib ada untuk JPA

    public Peminjaman(Anggota anggota, Buku buku, LocalDate tglPinjam) {
        this.anggota = anggota;
        this.buku = buku;
        this.tglPinjam = tglPinjam;
        this.tglJatuhTempo = tglPinjam.plusDays(LAMA_PINJAM_HARI);
    }

    public Long getIdPeminjaman()      { return idPeminjaman; }
    public Anggota getAnggota()        { return anggota; }
    public Buku getBuku()              { return buku; }
    public LocalDate getTglPinjam()    { return tglPinjam; }
    public LocalDate getTglJatuhTempo(){ return tglJatuhTempo; }
    public LocalDate getTglKembali()   { return tglKembali; }
    public long getDenda()             { return denda; }

    public boolean isAktif() {
        return tglKembali == null;
    }

    public boolean isTerlambat() {
        return isAktif() && LocalDate.now().isAfter(tglJatuhTempo);
    }

    /** Denda yang sudah final (jika dikembalikan) atau perkiraan denda hari ini (jika belum). */
    public long getEstimasiDenda() {
        return isAktif() ? hitungDenda(tglJatuhTempo, LocalDate.now()) : denda;
    }

    /** Tandai buku dikembalikan dan simpan dendanya. */
    public void kembalikan(LocalDate tanggal) {
        if (!isAktif()) {
            throw new IllegalStateException("Buku sudah dikembalikan");
        }
        this.tglKembali = tanggal;
        this.denda = hitungDenda(tglJatuhTempo, tanggal);
    }

    /** Denda = jumlah hari terlambat x tarif per hari. Tidak terlambat = 0. */
    public static long hitungDenda(LocalDate jatuhTempo, LocalDate tanggalKembali) {
        long hariTerlambat = ChronoUnit.DAYS.between(jatuhTempo, tanggalKembali);
        return hariTerlambat > 0 ? hariTerlambat * DENDA_PER_HARI : 0;
    }
}
