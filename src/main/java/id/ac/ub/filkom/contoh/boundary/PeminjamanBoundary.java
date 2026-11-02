package id.ac.ub.filkom.contoh.boundary;

import id.ac.ub.filkom.contoh.control.AnggotaControl;
import id.ac.ub.filkom.contoh.control.BukuControl;
import id.ac.ub.filkom.contoh.control.PeminjamanControl;
import id.ac.ub.filkom.contoh.entity.Peminjaman;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.Locale;

/** Boundary: halaman peminjaman dan pengembalian buku. */
@Controller
public class PeminjamanBoundary {

    private final PeminjamanControl peminjamanControl;
    private final AnggotaControl anggotaControl;
    private final BukuControl bukuControl;

    public PeminjamanBoundary(PeminjamanControl peminjamanControl,
                              AnggotaControl anggotaControl,
                              BukuControl bukuControl) {
        this.peminjamanControl = peminjamanControl;
        this.anggotaControl = anggotaControl;
        this.bukuControl = bukuControl;
    }

    // GET http://localhost:8080/peminjaman
    @GetMapping("/peminjaman")
    public String halaman(Model model) {
        model.addAttribute("daftarAnggota", anggotaControl.lihatSemuaAnggota());
        model.addAttribute("bukuTersedia", bukuControl.bukuTersedia());
        model.addAttribute("pinjamanAktif", peminjamanControl.pinjamanAktif());
        model.addAttribute("riwayat", peminjamanControl.riwayat());
        model.addAttribute("lamaPinjam", Peminjaman.LAMA_PINJAM_HARI);
        model.addAttribute("dendaPerHari", Peminjaman.DENDA_PER_HARI);
        return "peminjaman";
    }

    @PostMapping("/peminjaman")
    public String pinjam(@RequestParam String idAnggota,
                         @RequestParam String idBuku,
                         RedirectAttributes redirectAttributes) {
        try {
            Peminjaman p = peminjamanControl.pinjam(idAnggota, idBuku);
            redirectAttributes.addFlashAttribute("pesan",
                    "Peminjaman berhasil. Jatuh tempo: " + p.getTglJatuhTempo() + ".");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("pesanError", ex.getMessage());
        }
        return "redirect:/peminjaman";
    }

    @PostMapping("/peminjaman/{idPeminjaman}/kembali")
    public String kembalikan(@PathVariable Long idPeminjaman, RedirectAttributes redirectAttributes) {
        try {
            Peminjaman p = peminjamanControl.kembalikan(idPeminjaman);
            String pesan = "Buku berhasil dikembalikan.";
            if (p.getDenda() > 0) {
                pesan += " Denda: " + rupiah(p.getDenda()) + ".";
            }
            redirectAttributes.addFlashAttribute("pesan", pesan);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("pesanError", ex.getMessage());
        }
        return "redirect:/peminjaman";
    }

    private String rupiah(long nominal) {
        return "Rp " + NumberFormat.getIntegerInstance(Locale.forLanguageTag("id-ID")).format(nominal);
    }
}
