package lofod.reviewer;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    ArrayList<Card> data;
    File fileDir;
    MainActivity mainActivity;

    CardAdapter(ArrayList<Card> data, File fileDir, MainActivity mainActivity) {
        this.data = data;
        this.fileDir = fileDir;
        this.mainActivity = mainActivity;
    }

    public void setFileDir(File fileDir) {
        this.fileDir = fileDir;
    }

    public void setData(ArrayList<Card> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public Card getCard(int position) {
        return data.get(position);
    }

    public void addCard(Card card) {
        data.add(0, card);
        notifyDataSetChanged();
    }

    public void removeCard(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        final View itemView = holder.itemView;

        TextView itemName = itemView.findViewById(R.id.itemName);
        itemName.setText(data.get(position).getName());

        TextView manufacturerName = itemView.findViewById(R.id.manufacturerName);
        manufacturerName.setText(data.get(position).getManufacturer());

        TextView amountAndPrice = itemView.findViewById(R.id.amountAndPrice);
        String amoutAndPriceValue = "";
        // 0 - кг, 1 - л, 2 - шт
        switch (data.get(position).getAmountType()) {
            case 0:
                amoutAndPriceValue = data.get(position).getAmount() + " кг. | "
                        + data.get(position).getPrice() + " ₽";
                break;
            case 1:
                amoutAndPriceValue = data.get(position).getAmount() + " л. | "
                        + data.get(position).getPrice() + " ₽";
                break;
            case 2:
                amoutAndPriceValue = data.get(position).getAmount() + " шт. | "
                        + data.get(position).getPrice() + " ₽";
                break;
        }

        TextView date = itemView.findViewById(R.id.date);
        date.setText(data.get(position).getDate());

        ImageView itemPhoto = itemView.findViewById(R.id.itemPhoto);
        if (data.get(position).getPhoto().equals("default")) {
            itemPhoto.setImageBitmap(BitmapFactory.decodeResource(itemView.getResources(),
                    R.drawable.default_photo));
        } else {
            // TODO проверить, не надо ли изменить на .jpg
            File file = new File(fileDir, data.get(position).getPhoto() + ".png");
            Uri imgUri = Uri.fromFile(file);
            itemPhoto.setImageURI(imgUri);
        }

        RatingBar ratingBar = itemView.findViewById(R.id.ratingBar);
        ratingBar.setRating(data.get(position).getMark());

        itemView.setOnLongClickListener(v -> {
            mainActivity.onLongClick(holder.getAdapterPosition());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}

