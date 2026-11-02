package id.ac.ub.filkom.contoh.control;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.PeminjamanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Control: logika bisnis pengelolaan anggota (use case Kelola Anggota). */
@Service
public class AnggotaControl {

    private final AnggotaRepository anggotaRepository;
    private final PeminjamanRepository peminjamanRepository;

    public AnggotaControl(AnggotaRepository anggotaRepository, PeminjamanRepository peminjamanRepository) {
        this.anggotaRepository = anggotaRepository;
        this.peminjamanRepository = peminjamanRepository;
    }

    public List<Anggota> lihatSemuaAnggota() {
        return anggotaRepository.findAll();
    }

    public Anggota tambahAnggota(String idAnggota, String nama, int batasPinjam) {
        validasi(idAnggota, nama, batasPinjam);
        if (anggotaRepository.existsById(idAnggota.trim())) {
            throw new IllegalArgumentException("ID anggota sudah digunakan");
        }
        return anggotaRepository.save(new Anggota(idAnggota.trim(), nama.trim(), batasPinjam));
    }

    public Anggota ubahAnggota(String idAnggota, String nama, int batasPinjam) {
        validasi(idAnggota, nama, batasPinjam);
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() -> new IllegalArgumentException("Anggota tidak ditemukan"));
        anggota.perbarui(nama.trim(), batasPinjam);
        return anggotaRepository.save(anggota);
    }

    public void hapusAnggota(String idAnggota) {
        if (!anggotaRepository.existsById(idAnggota)) {
            throw new IllegalArgumentException("Anggota tidak ditemukan");
        }
        if (peminjamanRepository.existsByAnggota_IdAnggota(idAnggota)) {
            throw new IllegalArgumentException("Anggota tidak bisa dihapus karena memiliki riwayat peminjaman");
        }
        anggotaRepository.deleteById(idAnggota);
    }

    public long jumlahAnggota() {
        return anggotaRepository.count();
    }

    private void validasi(String idAnggota, String nama, int batasPinjam) {
        if (idAnggota == null || idAnggota.isBlank()) {
            throw new IllegalArgumentException("ID anggota wajib diisi");
        }
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama wajib diisi");
        }
        if (batasPinjam < 1) {
            throw new IllegalArgumentException("Batas pinjam minimal 1");
        }
    }
}
