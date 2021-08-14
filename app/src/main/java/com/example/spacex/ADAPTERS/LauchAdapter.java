package com.example.spacex.ADAPTERS;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spacex.MODELS.Launch;
import com.example.spacex.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class LauchAdapter extends RecyclerView.Adapter<LauchAdapter.myViewHolder> {

    private LayoutInflater inflator;
    List<Launch> data = Collections.emptyList();
    Context mContext;

    OnItemClick onItemClick;

    public LauchAdapter(Context context, List<Launch> data) {
        inflator = LayoutInflater.from(context);
        mContext = context;
        this.data = data;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.launch_view, parent, false);
        myViewHolder holder = new myViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        Launch current = data.get(position);
        holder.missionNameTV.setText(current.getMission());
        holder.days.setText(current.getDaysBetween());
        holder.dateAtTime.setText(current.getDate() + " at " + current.getTime());
        holder.roketName.setText(current.getRocketName() + "/ " + current.getRocketType());
//        Picasso.get().load(current.getImgUrl()).placeholder(R.drawable.image_search_24).into(holder.imageIV);
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
            holder.daysLable.setText("Days Since:");
        } else {
            holder.daysLable.setText("Days From Now:");
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView missionNameTV;
        TextView dateAtTime;
        TextView roketName;
        TextView daysLable, days;
        ImageView imageIV, statusIV;



        public myViewHolder(final View itemView) {
            super(itemView);
            missionNameTV = itemView.findViewById(R.id.missionNameTV);
            dateAtTime = itemView.findViewById(R.id.dateAtTime);
            roketName = itemView.findViewById(R.id.roketName);
            days = itemView.findViewById(R.id.days);
            daysLable = itemView.findViewById(R.id.daysLable);
            imageIV = itemView.findViewById(R.id.imageIV);
            statusIV = itemView.findViewById(R.id.statusIV);



            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                      Launch launch = data.get(pos);

                    onItemClick.getPosition(launch);


//                        if(transaction.getToken()!= null) {
//                            Gson gson = new Gson();
//                            String voucherObjectAsAString = gson.toJson(transaction);
//                            Intent i = new Intent(itemView.getContext(), TransactionDetailsActivity.class);
//                            i.putExtra("voucherObjectAsAString", voucherObjectAsAString);
//                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            mContext.startActivity(i);
//                        }

                }
//                    Toast.makeText(itemView.getContext(), "", Toast.LENGTH_SHORT).show();
            });
        }


    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void getPosition(Launch clickedlaunch);
    }
}
