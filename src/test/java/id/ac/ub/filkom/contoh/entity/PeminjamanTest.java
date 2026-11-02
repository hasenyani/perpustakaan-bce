package id.ac.ub.filkom.contoh.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeminjamanTest {

    private Peminjaman buat(LocalDate tglPinjam) {
        return new Peminjaman(new Anggota("A001", "Budi", 2), new Buku("B001", "Java", 1), tglPinjam);
    }

    @Test
    void jatuhTempoTujuhHariSetelahPinjam() {
        Peminjaman p = buat(LocalDate.of(2026, 1, 1));
        assertEquals(LocalDate.of(2026, 1, 8), p.getTglJatuhTempo());
        assertTrue(p.isAktif());
    }

    @Test
    void dikembalikanTepatWaktuTanpaDenda() {
        Peminjaman p = buat(LocalDate.of(2026, 1, 1));
        p.kembalikan(LocalDate.of(2026, 1, 8));
        assertFalse(p.isAktif());
        assertEquals(0, p.getDenda());
    }

    @Test
    void terlambatTigaHariDendaTigaRibu() {
        Peminjaman p = buat(LocalDate.of(2026, 1, 1));
        p.kembalikan(LocalDate.of(2026, 1, 11));
        assertEquals(3000, p.getDenda());
    }

    @Test
    void kembalikanDuaKaliDitolak() {
        Peminjaman p = buat(LocalDate.of(2026, 1, 1));
        p.kembalikan(LocalDate.of(2026, 1, 5));
        assertThrows(IllegalStateException.class, () -> p.kembalikan(LocalDate.of(2026, 1, 6)));
    }
}
