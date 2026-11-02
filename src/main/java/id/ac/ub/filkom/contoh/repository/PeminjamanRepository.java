package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.Peminjaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Repository: akses tabel peminjaman. */
public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {

    // Belum dikembalikan, yang paling mendekati jatuh tempo di atas
    List<Peminjaman> findByTglKembaliIsNullOrderByTglJatuhTempoAsc();

    // Sudah dikembalikan, yang terbaru di atas
    List<Peminjaman> findByTglKembaliIsNotNullOrderByTglKembaliDesc();

    // Jumlah pinjaman aktif satu anggota (untuk cek batas pinjam)
    long countByAnggota_IdAnggotaAndTglKembaliIsNull(String idAnggota);

    // Anggota masih memegang buku yang sama?
    boolean existsByAnggota_IdAnggotaAndBuku_IdBukuAndTglKembaliIsNull(String idAnggota, String idBuku);

    // Dipakai untuk menolak hapus buku/anggota yang punya riwayat
    boolean existsByBuku_IdBuku(String idBuku);

    boolean existsByAnggota_IdAnggota(String idAnggota);
}
