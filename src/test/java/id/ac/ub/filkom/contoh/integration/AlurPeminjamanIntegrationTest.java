package id.ac.ub.filkom.contoh.integration;

import id.ac.ub.filkom.contoh.control.PeminjamanControl;
import id.ac.ub.filkom.contoh.entity.Peminjaman;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Uji alur nyata terhadap H2 + data.sql: pinjam lalu kembalikan. Di-rollback tiap test. */
@SpringBootTest
@Transactional
class AlurPeminjamanIntegrationTest {

    @Autowired
    private PeminjamanControl peminjamanControl;

    @Autowired
    private BukuRepository bukuRepository;

    @Test
    void pinjamLaluKembalikanMengubahStokDanMenghitungDenda() {
        int stokAwal = bukuRepository.findById("B003").orElseThrow().getStok();

        Peminjaman p = peminjamanControl.pinjam("A003", "B003", LocalDate.now());
        assertEquals(stokAwal - 1, bukuRepository.findById("B003").orElseThrow().getStok());

        peminjamanControl.kembalikan(p.getIdPeminjaman(), LocalDate.now().plusDays(10));
        assertEquals(stokAwal, bukuRepository.findById("B003").orElseThrow().getStok());
        assertEquals(3000, p.getDenda());
    }
}
