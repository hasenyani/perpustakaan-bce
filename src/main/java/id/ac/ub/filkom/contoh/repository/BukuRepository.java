package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.Buku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Repository: akses tabel buku. Implementasi dibuat otomatis oleh Spring. */
public interface BukuRepository extends JpaRepository<Buku, String> {

    // SELECT * FROM buku WHERE LOWER(judul) LIKE %kata%
    List<Buku> findByJudulContainingIgnoreCase(String kata);

    // SELECT * FROM buku WHERE stok > ?
    List<Buku> findByStokGreaterThan(int stok);
}
