package com.example.shuttletimesport;

import com.google.gson.annotations.SerializedName;

public class Booking {
    private int id;
    private String nama;
    private String tanggal;

    @SerializedName("jam_mulai")
    private String jamMulai;

    @SerializedName("jam_selesai")
    private String jamSelesai;

    @SerializedName("id_lapangan")
    private int idLapangan;

    private String status;

    @SerializedName("nama_lapangan")
    private String namaLapangan;

    @SerializedName("total_harga")
    private double totalHarga;

    // ✅ Constructor kosong (diperlukan untuk parsing manual JSON)
    public Booking() {
    }

    // Constructor lengkap (jika ingin digunakan)
    public Booking(int id, String nama, String tanggal, String jamMulai, String jamSelesai,
                   int idLapangan, String status, String namaLapangan, double totalHarga) {
        this.id = id;
        this.nama = nama;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.idLapangan = idLapangan;
        this.status = status;
        this.namaLapangan = namaLapangan;
        this.totalHarga = totalHarga;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }

    public String getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(String jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public int getIdLapangan() {
        return idLapangan;
    }

    public void setIdLapangan(int idLapangan) {
        this.idLapangan = idLapangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNamaLapangan() {
        return namaLapangan;
    }

    public void setNamaLapangan(String namaLapangan) {
        this.namaLapangan = namaLapangan;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }
}
