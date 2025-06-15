package com.example.shuttletimesport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private final Context context;
    private final List<Booking> bookingList;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvNama.setText("Nama: " + booking.getNama());
        holder.tvTanggal.setText("Tanggal: " + booking.getTanggal());
        holder.tvJam.setText("Jam: " + booking.getJamMulai() + " - " + booking.getJamSelesai());
        holder.tvStatus.setText("Status: " + booking.getStatus());
        holder.tvLapangan.setText("Lapangan: " + booking.getNamaLapangan());
        holder.tvTotalHarga.setText("Total Harga: Rp " + String.format("%,.0f", booking.getTotalHarga()));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvTanggal, tvJam, tvStatus, tvLapangan, tvTotalHarga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLapangan = itemView.findViewById(R.id.tvLapangan);
            tvTotalHarga = itemView.findViewById(R.id.tvTotalHarga);
        }
    }
}
