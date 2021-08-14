package com.example.spacex.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spacex.MODELS.Launch;
import com.example.spacex.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LaunchAdapter extends RecyclerView.Adapter<LaunchAdapter.myViewHolder> {

    private LayoutInflater inflater;
    List<Launch> data;
    Context mContext;

    OnItemClick onItemClick;

    public LaunchAdapter(Context context, List<Launch> data) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.data = data;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.launch_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        Launch current = data.get(position);
        holder.missionNameTV.setText(current.getMission());
        holder.days.setText(current.getDaysBetween());
        holder.dateAtTime.setText(current.getDate() + " at " + current.getTime());
        holder.rocketName.setText(current.getRocketName() + "/ " + current.getRocketType());
        Picasso.get().load(current.getImgUrl()).fit().centerCrop()
                .placeholder(R.drawable.image_search_24)
                .error(R.drawable.red_button_background)
                .into(holder.imageIV);
        if (current.getSuccessful()) {
            holder.statusIV.setImageResource(R.drawable.success_24);
        } else {
            holder.statusIV.setImageResource(R.drawable.error_center_x);
        }

        if (Integer.parseInt(current.getDaysBetween()) < 0) {
            holder.daysLabel.setText("Days Since:");
        } else {
            holder.daysLabel.setText("Days From Now:");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView missionNameTV;
        TextView dateAtTime;
        TextView rocketName;
        TextView daysLabel, days;
        ImageView imageIV, statusIV;

        public myViewHolder(final View itemView) {
            super(itemView);
            missionNameTV = itemView.findViewById(R.id.missionNameTV);
            dateAtTime = itemView.findViewById(R.id.dateAtTime);
            rocketName = itemView.findViewById(R.id.roketName);
            days = itemView.findViewById(R.id.days);
            daysLabel = itemView.findViewById(R.id.daysLable);
            imageIV = itemView.findViewById(R.id.imageIV);
            statusIV = itemView.findViewById(R.id.statusIV);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Launch launch = data.get(pos);

                    onItemClick.getPosition(launch);
                }
            });
        }

    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void getPosition(Launch launch);
    }
}
