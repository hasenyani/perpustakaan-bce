-- Stok = jumlah eksemplar yang tersedia saat ini (sudah dikurangi yang sedang dipinjam)
INSERT INTO buku (id_buku, judul, stok) VALUES ('B001', 'Pemrograman Java', 2);
INSERT INTO buku (id_buku, judul, stok) VALUES ('B002', 'Rekayasa Perangkat Lunak', 0);
INSERT INTO buku (id_buku, judul, stok) VALUES ('B003', 'Basis Data', 2);
INSERT INTO buku (id_buku, judul, stok) VALUES ('B004', 'Analisis dan Perancangan Sistem', 4);

INSERT INTO anggota (id_anggota, nama, batas_pinjam) VALUES ('A001', 'Budi Santoso', 2);
INSERT INTO anggota (id_anggota, nama, batas_pinjam) VALUES ('A002', 'Sari Dewi', 1);
INSERT INTO anggota (id_anggota, nama, batas_pinjam) VALUES ('A003', 'Andi Pratama', 3);

-- Pinjaman aktif dan sudah lewat jatuh tempo (B001 oleh A001)
INSERT INTO peminjaman (id_anggota, id_buku, tgl_pinjam, tgl_jatuh_tempo, tgl_kembali, denda)
VALUES ('A001', 'B001', DATEADD('DAY', -10, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_DATE), NULL, 0);
-- Pinjaman aktif untuk B002 oleh A002 (stok B002 = 0)
INSERT INTO peminjaman (id_anggota, id_buku, tgl_pinjam, tgl_jatuh_tempo, tgl_kembali, denda)
VALUES ('A002', 'B002', DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 5, CURRENT_DATE), NULL, 0);
-- Riwayat: dikembalikan terlambat 2 hari, denda 2 x 1000
INSERT INTO peminjaman (id_anggota, id_buku, tgl_pinjam, tgl_jatuh_tempo, tgl_kembali, denda)
VALUES ('A002', 'B003', DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -13, CURRENT_DATE), DATEADD('DAY', -11, CURRENT_DATE), 2000);
