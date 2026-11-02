package id.ac.ub.filkom.contoh.control;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.Peminjaman;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.PeminjamanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeminjamanControlTest {

    private static final LocalDate HARI_INI = LocalDate.of(2026, 1, 1);

    @Mock
    private PeminjamanRepository peminjamanRepository;
    @Mock
    private BukuRepository bukuRepository;
    @Mock
    private AnggotaRepository anggotaRepository;

    private PeminjamanControl control;

    @BeforeEach
    void setUp() {
        control = new PeminjamanControl(peminjamanRepository, bukuRepository, anggotaRepository);
    }

    @Test
    void pinjamBerhasilMengurangiStokDanMengisiJatuhTempo() {
        Anggota anggota = new Anggota("A001", "Budi", 2);
        Buku buku = new Buku("B001", "Java", 3);
        when(anggotaRepository.findById("A001")).thenReturn(Optional.of(anggota));
        when(bukuRepository.findById("B001")).thenReturn(Optional.of(buku));
        when(peminjamanRepository.countByAnggota_IdAnggotaAndTglKembaliIsNull("A001")).thenReturn(0L);
        when(peminjamanRepository.save(any(Peminjaman.class))).thenAnswer(inv -> inv.getArgument(0));

        Peminjaman p = control.pinjam("A001", "B001", HARI_INI);

        assertEquals(2, buku.getStok());
        assertEquals(HARI_INI.plusDays(7), p.getTglJatuhTempo());
    }

    @Test
    void pinjamDitolakJikaStokHabis() {
        when(anggotaRepository.findById("A001")).thenReturn(Optional.of(new Anggota("A001", "Budi", 2)));
        when(bukuRepository.findById("B002")).thenReturn(Optional.of(new Buku("B002", "RPL", 0)));

        assertThrows(IllegalArgumentException.class, () -> control.pinjam("A001", "B002", HARI_INI));
    }

    @Test
    void pinjamDitolakJikaBatasPinjamTercapai() {
        when(anggotaRepository.findById("A002")).thenReturn(Optional.of(new Anggota("A002", "Sari", 1)));
        when(bukuRepository.findById("B001")).thenReturn(Optional.of(new Buku("B001", "Java", 3)));
        when(peminjamanRepository.countByAnggota_IdAnggotaAndTglKembaliIsNull("A002")).thenReturn(1L);

        assertThrows(IllegalArgumentException.class, () -> control.pinjam("A002", "B001", HARI_INI));
    }

    @Test
    void kembaliTepatWaktuTanpaDendaDanStokBertambah() {
        Buku buku = new Buku("B001", "Java", 2);
        Peminjaman p = new Peminjaman(new Anggota("A001", "Budi", 2), buku, HARI_INI);
        when(peminjamanRepository.findById(1L)).thenReturn(Optional.of(p));
        when(peminjamanRepository.save(p)).thenReturn(p);

        Peminjaman hasil = control.kembalikan(1L, HARI_INI.plusDays(7));

        assertEquals(0, hasil.getDenda());
        assertEquals(3, buku.getStok());
        assertFalse(hasil.isAktif());
    }

    @Test
    void kembaliTerlambatTigaHariDendaTigaRibu() {
        Peminjaman p = new Peminjaman(new Anggota("A001", "Budi", 2), new Buku("B001", "Java", 2), HARI_INI);
        when(peminjamanRepository.findById(1L)).thenReturn(Optional.of(p));
        when(peminjamanRepository.save(p)).thenReturn(p);

        Peminjaman hasil = control.kembalikan(1L, HARI_INI.plusDays(10));

        assertEquals(3000, hasil.getDenda());
    }

    @Test
    void kembaliDuaKaliDitolak() {
        Peminjaman p = new Peminjaman(new Anggota("A001", "Budi", 2), new Buku("B001", "Java", 2), HARI_INI);
        p.kembalikan(HARI_INI.plusDays(2));
        when(peminjamanRepository.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(IllegalArgumentException.class, () -> control.kembalikan(1L, HARI_INI.plusDays(3)));
    }
}
