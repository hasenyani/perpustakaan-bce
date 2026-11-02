package id.ac.ub.filkom.contoh.control;

import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.PeminjamanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BukuControlTest {

    @Mock
    private BukuRepository bukuRepository;

    @Mock
    private PeminjamanRepository peminjamanRepository;

    private BukuControl control;

    @BeforeEach
    void setUp() {
        control = new BukuControl(bukuRepository, peminjamanRepository);
    }

    @Test
    void menambahBukuNormalisasiData() {
        when(bukuRepository.existsById("B004")).thenReturn(false);
        when(bukuRepository.save(any(Buku.class))).thenAnswer(inv -> inv.getArgument(0));

        Buku saved = control.tambahBuku(" B004 ", " Clean Code ", 2);

        assertEquals("B004", saved.getIdBuku());
        assertEquals("Clean Code", saved.getJudul());
        verify(bukuRepository).save(saved);
    }

    @Test
    void menambahBukuMenolakStokNegatif() {
        assertThrows(IllegalArgumentException.class, () -> control.tambahBuku("B004", "Clean Code", -1));
    }

    @Test
    void mengubahBukuMenggantiJudulDanStok() {
        Buku buku = new Buku("B004", "Judul lama", 1);
        when(bukuRepository.findById("B004")).thenReturn(Optional.of(buku));
        when(bukuRepository.save(buku)).thenReturn(buku);

        Buku updated = control.ubahBuku("B004", "Judul baru", 5);

        assertEquals("Judul baru", updated.getJudul());
        assertEquals(5, updated.getStok());
    }

    @Test
    void menghapusBukuYangPunyaRiwayatDitolak() {
        when(bukuRepository.existsById("B001")).thenReturn(true);
        when(peminjamanRepository.existsByBuku_IdBuku("B001")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> control.hapusBuku("B001"));
        verify(bukuRepository, never()).deleteById("B001");
    }
}
