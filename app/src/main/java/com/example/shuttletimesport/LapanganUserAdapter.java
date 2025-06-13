package com.example.shuttletimesport;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LapanganUserAdapter extends RecyclerView.Adapter<LapanganUserAdapter.ViewHolder> {

    private Context context;
    private List<Lapangan> lapanganList;

    public LapanganUserAdapter(Context context, List<Lapangan> lapanganList) {
        this.context = context;
        this.lapanganList = lapanganList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lapangan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lapangan lapangan = lapanganList.get(position);

        holder.tvNama.setText(lapangan.getNama());
        holder.tvJenis.setText("Jenis: " + lapangan.getJenis());
        holder.tvHarga.setText("Harga: Rp " + lapangan.getHarga() + " / jam");
        holder.tvFasilitas.setText("Fasilitas: " + lapangan.getFasilitas());

        Glide.with(context)
                .load("http://10.0.2.2/shuttletime_api/uploads/" + lapangan.getGambar())
                .into(holder.imgLapangan);

        holder.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailLapanganCustomerActivity.class);
            intent.putExtra("lapangan", lapangan); // kirim objek Lapangan langsung
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lapanganList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvJenis, tvHarga, tvFasilitas;
        ImageView imgLapangan;
        Button btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvJenis = itemView.findViewById(R.id.tvJenis);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvFasilitas = itemView.findViewById(R.id.tvFasilitasDetail);
            imgLapangan = itemView.findViewById(R.id.imgLapangan);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
