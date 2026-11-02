package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.Anggota;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository: akses tabel anggota. */
public interface AnggotaRepository extends JpaRepository<Anggota, String> {
}
