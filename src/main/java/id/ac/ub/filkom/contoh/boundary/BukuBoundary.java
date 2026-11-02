package id.ac.ub.filkom.contoh.boundary;

import id.ac.ub.filkom.contoh.control.BukuControl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Boundary: menerima request dari halaman buku, memanggil control, memilih template. */
@Controller
public class BukuBoundary {

    private final BukuControl bukuControl;

    public BukuBoundary(BukuControl bukuControl) {
        this.bukuControl = bukuControl;
    }

    // GET http://localhost:8080/buku?kata=java
    @GetMapping("/buku")
    public String daftarBuku(@RequestParam(required = false) String kata, Model model) {
        model.addAttribute("daftarBuku", bukuControl.cariBuku(kata));
        model.addAttribute("kata", kata);
        model.addAttribute("jumlahStok", bukuControl.jumlahStok());
        return "buku";
    }

    @PostMapping("/buku")
    public String tambahBuku(@RequestParam String idBuku,
                             @RequestParam String judul,
                             @RequestParam int stok,
                             RedirectAttributes redirectAttributes) {
        try {
            bukuControl.tambahBuku(idBuku, judul, stok);
            redirectAttributes.addFlashAttribute("pesan", "Buku berhasil ditambahkan.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("pesanError", ex.getMessage());
        }
        return "redirect:/buku";
    }

    @PostMapping("/buku/{idBuku}/ubah")
    public String ubahBuku(@PathVariable String idBuku,
                           @RequestParam String judul,
                           @RequestParam int stok,
                           RedirectAttributes redirectAttributes) {
        try {
            bukuControl.ubahBuku(idBuku, judul, stok);
            redirectAttributes.addFlashAttribute("pesan", "Data buku berhasil diperbarui.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("pesanError", ex.getMessage());
        }
        return "redirect:/buku";
    }

    @PostMapping("/buku/{idBuku}/hapus")
    public String hapusBuku(@PathVariable String idBuku, RedirectAttributes redirectAttributes) {
        try {
            bukuControl.hapusBuku(idBuku);
            redirectAttributes.addFlashAttribute("pesan", "Buku berhasil dihapus.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("pesanError", ex.getMessage());
        }
        return "redirect:/buku";
    }
}
