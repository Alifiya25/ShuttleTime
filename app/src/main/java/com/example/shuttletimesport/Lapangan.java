package com.example.shuttletimesport;

import java.io.Serializable;

public class Lapangan implements Serializable {
    private int id;
    private String nama;
    private String jenis;
    private int harga;
    private String fasilitas;
    private String gambar;

    public Lapangan(int id, String nama, String jenis, int harga, String fasilitas, String gambar) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.harga = harga;
        this.fasilitas = fasilitas;
        this.gambar = gambar;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getJenis() { return jenis; }
    public int getHarga() { return harga; }
    public String getFasilitas() { return fasilitas; }
    public String getGambar() { return gambar; }
}
