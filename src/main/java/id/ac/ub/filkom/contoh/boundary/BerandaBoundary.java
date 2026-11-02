package id.ac.ub.filkom.contoh.boundary;

import id.ac.ub.filkom.contoh.control.AnggotaControl;
import id.ac.ub.filkom.contoh.control.BukuControl;
import id.ac.ub.filkom.contoh.control.PeminjamanControl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Boundary: halaman beranda berisi ringkasan perpustakaan. */
@Controller
public class BerandaBoundary {

    private final BukuControl bukuControl;
    private final AnggotaControl anggotaControl;
    private final PeminjamanControl peminjamanControl;

    public BerandaBoundary(BukuControl bukuControl,
                           AnggotaControl anggotaControl,
                           PeminjamanControl peminjamanControl) {
        this.bukuControl = bukuControl;
        this.anggotaControl = anggotaControl;
        this.peminjamanControl = peminjamanControl;
    }

    // GET http://localhost:8080/
    @GetMapping("/")
    public String beranda(Model model) {
        model.addAttribute("jumlahJudul", bukuControl.jumlahJudul());
        model.addAttribute("jumlahStok", bukuControl.jumlahStok());
        model.addAttribute("jumlahAnggota", anggotaControl.jumlahAnggota());
        model.addAttribute("jumlahAktif", peminjamanControl.jumlahAktif());
        model.addAttribute("jumlahTerlambat", peminjamanControl.jumlahTerlambat());
        return "beranda";
    }
}
