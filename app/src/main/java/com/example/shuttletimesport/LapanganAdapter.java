package com.example.shuttletimesport;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LapanganAdapter extends RecyclerView.Adapter<LapanganAdapter.ViewHolder> {

    private final Context context;
    private final List<Lapangan> lapanganList;

    public interface OnItemClickListener {
        void onItemClick(Lapangan lapangan);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LapanganAdapter(Context context, List<Lapangan> lapanganList) {
        this.context = context;
        this.lapanganList = lapanganList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = new CardView(context);
        cardView.setRadius(20);
        cardView.setCardElevation(8);
        cardView.setUseCompatPadding(true);
        cardView.setPreventCornerOverlap(true);
        cardView.setCardBackgroundColor(Color.WHITE);

        LinearLayout rootLayout = new LinearLayout(context);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        rootLayout.setPadding(24, 24, 24, 24);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        rootLayout.setGravity(Gravity.CENTER_VERTICAL);

        CircleImageView imageView = new CircleImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        imageView.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);

        View spacer = new View(context);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(24, LinearLayout.LayoutParams.MATCH_PARENT));

        LinearLayout textLayout = new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView tvNama = new TextView(context);
        tvNama.setTextSize(18);
        tvNama.setTypeface(null, Typeface.BOLD);
        tvNama.setTextColor(Color.parseColor("#212121"));

        TextView tvJenis = new TextView(context);
        tvJenis.setTextSize(15);
        tvJenis.setTextColor(Color.parseColor("#616161"));

        TextView tvHarga = new TextView(context);
        tvHarga.setTextSize(15);
        tvHarga.setTypeface(null, Typeface.ITALIC);
        tvHarga.setTextColor(Color.parseColor("#388E3C"));

        TextView tvFasilitas = new TextView(context);
        tvFasilitas.setTextSize(14);
        tvFasilitas.setTextColor(Color.parseColor("#757575"));

        textLayout.addView(tvNama);
        textLayout.addView(tvJenis);
        textLayout.addView(tvHarga);
        textLayout.addView(tvFasilitas);

        rootLayout.addView(imageView);
        rootLayout.addView(spacer);
        rootLayout.addView(textLayout);

        cardView.addView(rootLayout);

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16);
        cardView.setLayoutParams(params);

        return new ViewHolder(cardView, tvNama, tvJenis, tvHarga, tvFasilitas, imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lapangan lapangan = lapanganList.get(position);

        holder.tvNama.setText(lapangan.getNama());
        holder.tvJenis.setText("Jenis: " + lapangan.getJenis());
        holder.tvHarga.setText("Rp " + lapangan.getHarga() + " / jam");
        holder.tvFasilitas.setText("Fasilitas: " + lapangan.getFasilitas());

        Glide.with(context)
                .load("http://10.0.2.2/shuttletime_api/uploads/" + lapangan.getGambar())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(lapangan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lapanganList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvJenis, tvHarga, tvFasilitas;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView, TextView tvNama, TextView tvJenis, TextView tvHarga,
                          TextView tvFasilitas, CircleImageView imageView) {
            super(itemView);
            this.tvNama = tvNama;
            this.tvJenis = tvJenis;
            this.tvHarga = tvHarga;
            this.tvFasilitas = tvFasilitas;
            this.imageView = imageView;
        }
    }
}
