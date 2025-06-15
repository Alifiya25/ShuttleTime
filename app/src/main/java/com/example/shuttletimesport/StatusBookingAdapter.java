package com.example.shuttletimesport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusBookingAdapter extends RecyclerView.Adapter<StatusBookingAdapter.StatusViewHolder> {

    private List<Booking> bookingList;
    private boolean isAdmin;

    public StatusBookingAdapter(List<Booking> bookingList, boolean isAdmin) {
        this.bookingList = bookingList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        Context context = holder.itemView.getContext();

        holder.tvNama.setText("Nama: " + booking.getNama());
        holder.tvStatus.setText(booking.getStatus()); // tag status
        holder.tvLapangan.setText("Lapangan: " + booking.getNamaLapangan());
        holder.tvTanggal.setText("Tanggal: " + booking.getTanggal());
        holder.tvJam.setText("Jam: " + booking.getJamMulai() + " - " + booking.getJamSelesai());
        holder.tvTotalHarga.setText("Total Harga: Rp " + String.format("%,.0f", booking.getTotalHarga()));

        if (isAdmin) {
            holder.spinnerStatus.setVisibility(View.VISIBLE);
            holder.btnUbahStatus.setVisibility(View.VISIBLE);

            // Daftar status dari DB
            String[] statusArray = {"menunggu", "terkonfirmasi", "dibatalkan", "selesai"};
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_spinner_item,
                    statusArray
            );
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinnerStatus.setAdapter(spinnerAdapter);

            // Set pilihan sesuai status saat ini
            int selectedIndex = spinnerAdapter.getPosition(booking.getStatus());
            if (selectedIndex >= 0) {
                holder.spinnerStatus.setSelection(selectedIndex);
            }

            // Klik tombol ubah status
            holder.btnUbahStatus.setOnClickListener(v -> {
                String statusBaru = holder.spinnerStatus.getSelectedItem().toString();

                // Cek apakah status berubah
                if (!statusBaru.equals(booking.getStatus())) {
                    int idBooking = booking.getId();

                    updateStatusBooking(context, idBooking, statusBaru, () -> {
                        booking.setStatus(statusBaru); // update status lokal
                        notifyItemChanged(holder.getAdapterPosition());
                    });
                } else {
                    Toast.makeText(context, "Status tidak berubah", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            holder.spinnerStatus.setVisibility(View.GONE);
            holder.btnUbahStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    // ViewHolder
    static class StatusViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvStatus, tvLapangan, tvTanggal, tvJam, tvTotalHarga;
        Spinner spinnerStatus;
        MaterialButton btnUbahStatus;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLapangan = itemView.findViewById(R.id.tvLapangan);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvTotalHarga = itemView.findViewById(R.id.tvTotalHarga);
            spinnerStatus = itemView.findViewById(R.id.spinnerStatus);
            btnUbahStatus = itemView.findViewById(R.id.btnUbahStatus);
        }
    }

    // Fungsi update status ke server
    private void updateStatusBooking(Context context, int idBooking, String statusBaru, Runnable onSuccess) {
        String url = "http://10.0.2.2/shuttletime_api/update_status.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(context, "Status berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    onSuccess.run(); // callback untuk update UI
                },
                error -> {
                    Toast.makeText(context, "Gagal memperbarui status", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idBooking));
                params.put("status", statusBaru);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

}
