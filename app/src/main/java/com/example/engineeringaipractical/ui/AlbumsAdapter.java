package com.example.engineeringaipractical.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engineeringaipractical.R;
import com.example.engineeringaipractical.entities.Hit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {

    private ArrayList<Hit> albums = new ArrayList<>();
    private MainActivity listener;

    public AlbumsAdapter(MainActivity activity) {
        this.listener = activity;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.albums_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.bind(albums.get(position));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.albumsTitle)
        AppCompatTextView albumTitle;
        @BindView(R.id.albumsCreatedAt)
        AppCompatTextView albumCreatedAt;
        @BindView(R.id.albumSwitch)
        SwitchCompat albumSwitch;

        AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Hit hit) {
            albumTitle.setText(hit.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = format.parse(hit.getCreatedAt());
                String dateTime = format1.format(date);
                albumCreatedAt.setText(dateTime);
            } catch (ParseException e) {
                albumCreatedAt.setText(hit.getCreatedAt());
                e.printStackTrace();
            }
//            albumCreatedAt.setText(hit.getCreatedAt());
            albumSwitch.setChecked(hit.isChecked());
            albumSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    hit.setChecked(isChecked);
//                    listener.onCheckedChanged(buttonView, hit, getAdapterPosition());
                    Hit hit1 = albums.get(getAdapterPosition());
                    hit1.setChecked(isChecked);
                    albums.set(getAdapterPosition(), hit1);
                    getCount(albums);
                }
            });

        }
    }

    public void getCount(ArrayList<Hit> hits) {
        if (listener != null) {
            int count = 0;
            for (int i = 0; i < hits.size(); i++) {
                if (hits.get(i).isChecked()) {
                    count++;
                }
            }
            listener.setTitle(count == 0 ? listener.getString(R.string.app_name) : "Selected Items " + count);
        }
    }


    void addNewAlbums(List<Hit> albums) {
        this.albums.clear();
        this.albums.addAll(albums);
        notifyDataSetChanged();
    }

    void addMoreAlbums(List<Hit> albums) {
        this.albums.addAll(albums);
        notifyDataSetChanged();
    }

}
