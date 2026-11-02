package id.ac.ub.filkom.contoh.boundary;

import id.ac.ub.filkom.contoh.control.AnggotaControl;
import id.ac.ub.filkom.contoh.control.BukuControl;
import id.ac.ub.filkom.contoh.control.PeminjamanControl;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.PeminjamanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PeminjamanBoundary.class)
@Import({PeminjamanControl.class, AnggotaControl.class, BukuControl.class})
class PeminjamanBoundaryTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PeminjamanRepository peminjamanRepository;
    @MockBean
    private BukuRepository bukuRepository;
    @MockBean
    private AnggotaRepository anggotaRepository;

    @Test
    void halamanPeminjamanBerhasilDirender() throws Exception {
        mockMvc.perform(get("/peminjaman"))
                .andExpect(status().isOk())
                .andExpect(view().name("peminjaman"));
    }

    @Test
    void pinjamAnggotaTidakAdaKembaliDenganPesanError() throws Exception {
        mockMvc.perform(post("/peminjaman")
                        .param("idAnggota", "A999")
                        .param("idBuku", "B001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peminjaman"))
                .andExpect(flash().attributeExists("pesanError"));
    }
}
