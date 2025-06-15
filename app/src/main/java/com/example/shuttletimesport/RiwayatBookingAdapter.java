package com.example.shuttletimesport;

import android.content.Context;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RiwayatBookingAdapter extends RecyclerView.Adapter<RiwayatBookingAdapter.ViewHolder> {

    private Context context;
    private List<Booking> bookingList;

    public RiwayatBookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvTanggal, tvJam, tvLapangan, tvStatus, tvHarga; // ubah nama variabel sesuai id di XML

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvLapangan = itemView.findViewById(R.id.tvLapangan);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvHarga = itemView.findViewById(R.id.tvHarga); // sesuaikan dengan XML
        }
    }

    @NonNull
    @Override
    public RiwayatBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatBookingAdapter.ViewHolder holder, int position) {
        Booking b = bookingList.get(position);

        holder.tvNama.setText(b.getNama());
        holder.tvTanggal.setText("Tanggal: " + b.getTanggal());
        holder.tvJam.setText("Jam: " + b.getJamMulai() + " - " + b.getJamSelesai());
        holder.tvLapangan.setText("Lapangan: " + b.getNamaLapangan());
        holder.tvStatus.setText("Status: " + b.getStatus());

        // Format harga jadi "Rp 50.000"
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String hargaFormatted = formatter.format(b.getTotalHarga());
        holder.tvHarga.setText("Total: " + hargaFormatted); // pakai tvHarga sesuai XML
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
}
