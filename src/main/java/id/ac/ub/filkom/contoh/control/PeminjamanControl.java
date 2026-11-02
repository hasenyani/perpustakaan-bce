package id.ac.ub.filkom.contoh.control;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.Peminjaman;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.PeminjamanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Control: logika bisnis transaksi (use case Pinjam Buku dan Kembalikan Buku).
 * Method yang menerima tanggal dipisah supaya mudah diuji tanpa bergantung pada jam sistem.
 */
@Service
public class PeminjamanControl {

    private final PeminjamanRepository peminjamanRepository;
    private final BukuRepository bukuRepository;
    private final AnggotaRepository anggotaRepository;

    public PeminjamanControl(PeminjamanRepository peminjamanRepository,
                             BukuRepository bukuRepository,
                             AnggotaRepository anggotaRepository) {
        this.peminjamanRepository = peminjamanRepository;
        this.bukuRepository = bukuRepository;
        this.anggotaRepository = anggotaRepository;
    }

    public Peminjaman pinjam(String idAnggota, String idBuku) {
        return pinjam(idAnggota, idBuku, LocalDate.now());
    }

    @Transactional
    public Peminjaman pinjam(String idAnggota, String idBuku, LocalDate tglPinjam) {
        if (idAnggota == null || idAnggota.isBlank()) {
            throw new IllegalArgumentException("Anggota wajib dipilih");
        }
        if (idBuku == null || idBuku.isBlank()) {
            throw new IllegalArgumentException("Buku wajib dipilih");
        }
        String idA = idAnggota.trim();
        String idB = idBuku.trim();

        Anggota anggota = anggotaRepository.findById(idA)
                .orElseThrow(() -> new IllegalArgumentException("Anggota tidak ditemukan"));
        Buku buku = bukuRepository.findById(idB)
                .orElseThrow(() -> new IllegalArgumentException("Buku tidak ditemukan"));

        if (buku.getStok() <= 0) {
            throw new IllegalArgumentException("Stok buku habis");
        }
        long aktif = peminjamanRepository.countByAnggota_IdAnggotaAndTglKembaliIsNull(idA);
        if (aktif >= anggota.getBatasPinjam()) {
            throw new IllegalArgumentException(
                    "Anggota sudah mencapai batas pinjam (" + anggota.getBatasPinjam() + " buku)");
        }
        if (peminjamanRepository.existsByAnggota_IdAnggotaAndBuku_IdBukuAndTglKembaliIsNull(idA, idB)) {
            throw new IllegalArgumentException("Anggota masih meminjam buku ini");
        }

        buku.kurangiStok();
        bukuRepository.save(buku);
        return peminjamanRepository.save(new Peminjaman(anggota, buku, tglPinjam));
    }

    public Peminjaman kembalikan(Long idPeminjaman) {
        return kembalikan(idPeminjaman, LocalDate.now());
    }

    @Transactional
    public Peminjaman kembalikan(Long idPeminjaman, LocalDate tglKembali) {
        Peminjaman peminjaman = peminjamanRepository.findById(idPeminjaman)
                .orElseThrow(() -> new IllegalArgumentException("Data peminjaman tidak ditemukan"));
        if (!peminjaman.isAktif()) {
            throw new IllegalArgumentException("Buku sudah dikembalikan");
        }

        peminjaman.kembalikan(tglKembali);
        Buku buku = peminjaman.getBuku();
        buku.tambahStok();
        bukuRepository.save(buku);
        return peminjamanRepository.save(peminjaman);
    }

    public List<Peminjaman> pinjamanAktif() {
        return peminjamanRepository.findByTglKembaliIsNullOrderByTglJatuhTempoAsc();
    }

    public List<Peminjaman> riwayat() {
        return peminjamanRepository.findByTglKembaliIsNotNullOrderByTglKembaliDesc();
    }

    public long jumlahAktif() {
        return pinjamanAktif().size();
    }

    public long jumlahTerlambat() {
        return pinjamanAktif().stream().filter(Peminjaman::isTerlambat).count();
    }
}
