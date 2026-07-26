package com.data.hostedpayments.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.data.hostedpayments.R;
import com.data.hostedpayments.model.UpiApp;

import java.util.List;

public class UpiAppAdapter extends RecyclerView.Adapter<UpiAppAdapter.ViewHolder> {

    private final List<UpiApp> appList;
    private int selectedPosition = -1;

    public UpiAppAdapter(List<UpiApp> appList) {
        this.appList = appList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_upi_app,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        UpiApp app = appList.get(position);

        holder.txtName.setText(app.getAppName());
        holder.imgIcon.setImageDrawable(app.getIcon());

        holder.radioButton.setChecked(position == selectedPosition);

        View.OnClickListener clickListener = v -> {

            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            if (previousPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousPosition);
            }

            notifyItemChanged(selectedPosition);
        };

        holder.itemView.setOnClickListener(clickListener);
        holder.radioButton.setOnClickListener(clickListener);

    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public UpiApp getSelectedApp() {

        if(selectedPosition == -1)
            return null;

        return appList.get(selectedPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView txtName;
        RadioButton radioButton;

        ViewHolder(View itemView) {

            super(itemView);

            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtName = itemView.findViewById(R.id.txtName);
            radioButton = itemView.findViewById(R.id.radioSelect);

        }
    }
}
