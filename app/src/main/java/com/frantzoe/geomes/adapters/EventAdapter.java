package com.frantzoe.geomes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frantzoe.geomes.models.Event;
import com.frantzoe.geomes.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Event event = events.get(position);
        holder.txtNumber.setText(event.getPhone());
        holder.txtLocation.setText(event.getLatitude() + "," + event.getLongitude());
        holder.txtDate.setText(event.getDate());
        //holder.imgThumbnail.setImageResource();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void removeEvent(int position) {
        events.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreEvent(Event event, int position) {
        events.add(position, event);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNumber;
        public TextView txtLocation;
        public TextView txtDate;
        //public ImageView imgThumbnail;
        public RelativeLayout rltListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtDate = itemView.findViewById(R.id.txtDate);
            //imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            rltListItem = itemView.findViewById(R.id.rltListItem);
        }
    }
}
