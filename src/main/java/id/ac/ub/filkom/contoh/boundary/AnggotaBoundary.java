package id.ac.ub.filkom.contoh.boundary;

import id.ac.ub.filkom.contoh.control.AnggotaControl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Boundary: halaman kelola anggota. */
@Controller
public class AnggotaBoundary {

    private final AnggotaControl anggotaControl;

    public AnggotaBoundary(AnggotaControl anggotaControl) {
        this.anggotaControl = anggotaControl;
    }

    // GET http://localhost:8080/anggota
    @GetMapping("/anggota")
    public String lihatAnggota(Model model) {
        model.addAttribute("daftarAnggota", anggotaControl.lihatSemuaAnggota());
        return "anggota";
    }

    @PostMapping("/anggota")
    public String tambahAnggota(@RequestParam String idAnggota,
                                @RequestParam String nama,
                                @RequestParam int batasPinjam,
                                RedirectAttributes redirectAttributes) {
        try {
            anggotaControl.tambahAnggota(idAnggota, nama, batasPinjam);
            redirectAttributes.addFlashAttribute("pesan", "Anggota berhasil ditambahkan.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("pesanError", ex.getMessage());
        }
        return "redirect:/anggota";
    }

    @PostMapping("/anggota/{idAnggota}/ubah")
    public String ubahAnggota(@PathVariable String idAnggota,
                              @RequestParam String nama,
                              @RequestParam int batasPinjam,
                              RedirectAttributes redirectAttributes) {
        try {
            anggotaControl.ubahAnggota(idAnggota, nama, batasPinjam);
            redirectAttributes.addFlashAttribute("pesan", "Data anggota berhasil diperbarui.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("pesanError", ex.getMessage());
        }
        return "redirect:/anggota";
    }

    @PostMapping("/anggota/{idAnggota}/hapus")
    public String hapusAnggota(@PathVariable String idAnggota, RedirectAttributes redirectAttributes) {
        try {
            anggotaControl.hapusAnggota(idAnggota);
            redirectAttributes.addFlashAttribute("pesan", "Anggota berhasil dihapus.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("pesanError", ex.getMessage());
        }
        return "redirect:/anggota";
    }
}
