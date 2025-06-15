package com.example.shuttletimesport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class KelolaJadwalAdapter extends RecyclerView.Adapter<KelolaJadwalAdapter.ViewHolder> {

    private List<LapanganWithJadwal> lapanganList;
    private Context context;

    public KelolaJadwalAdapter(List<LapanganWithJadwal> lapanganList) {
        this.lapanganList = lapanganList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaLapangan;
        TextView tvJadwal;
        Chip chipStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaLapangan = itemView.findViewById(R.id.tvNamaLapangan);
            tvJadwal = itemView.findViewById(R.id.tvJadwal);
            chipStatus = itemView.findViewById(R.id.chipStatus);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_lapangan_jadwal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LapanganWithJadwal item = lapanganList.get(position);

        // Set nama lapangan
        holder.tvNamaLapangan.setText(item.getNamaLapangan());

        // Format jadwal bookings
        StringBuilder jadwalText = new StringBuilder();
        List<Booking> jadwalList = item.getJadwalList();

        if (jadwalList == null || jadwalList.isEmpty()) {
            jadwalText.append("Tidak ada booking");
            setChipStatus(holder.chipStatus, "Tersedia", R.color.green_light, R.color.green_dark);
        } else {
            for (Booking booking : jadwalList) {
                jadwalText.append(String.format(Locale.getDefault(),
                        "%s - %s (%s)\n",
                        booking.getJamMulai(),
                        booking.getJamSelesai(),
                        booking.getNamaPemesan()));
            }

            int jumlahBooking = jadwalList.size();
            if (jumlahBooking >= 14) {
                setChipStatus(holder.chipStatus, "Penuh", R.color.red_light, R.color.red_dark);
            } else {
                setChipStatus(holder.chipStatus, "Sebagian Terisi", R.color.yellow_light, R.color.yellow_dark);
            }
        }

        holder.tvJadwal.setText(jadwalText.toString().trim());
    }

    private void setChipStatus(Chip chip, String text, int bgColorResId, int textColorResId) {
        chip.setText(text);
        chip.setChipBackgroundColorResource(bgColorResId);
        chip.setTextColor(context.getResources().getColor(textColorResId));
    }

    @Override
    public int getItemCount() {
        return lapanganList.size();
    }

    // ===== Model Kelas =====

    public static class LapanganWithJadwal {
        private String namaLapangan;
        private List<Booking> jadwalList;

        public LapanganWithJadwal(String namaLapangan, List<Booking> jadwalList) {
            this.namaLapangan = namaLapangan;
            this.jadwalList = jadwalList;
        }

        public String getNamaLapangan() {
            return namaLapangan;
        }

        public void setNamaLapangan(String namaLapangan) {
            this.namaLapangan = namaLapangan;
        }

        public List<Booking> getJadwalList() {
            return jadwalList;
        }

        public void setJadwalList(List<Booking> jadwalList) {
            this.jadwalList = jadwalList;
        }
    }

    public static class Booking {
        private String tanggal;
        private String jamMulai;
        private String jamSelesai;
        private String namaPemesan;

        public Booking(String tanggal, String jamMulai, String jamSelesai, String namaPemesan) {
            this.tanggal = tanggal;
            this.jamMulai = jamMulai;
            this.jamSelesai = jamSelesai;
            this.namaPemesan = namaPemesan;
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

        public String getNamaPemesan() {
            return namaPemesan;
        }

        public void setNamaPemesan(String namaPemesan) {
            this.namaPemesan = namaPemesan;
        }
    }
}
