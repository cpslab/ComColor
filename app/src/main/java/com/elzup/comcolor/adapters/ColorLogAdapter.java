package com.elzup.comcolor.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elzup.comcolor.R;
import com.elzup.comcolor.models.ColorLogObject;

import java.util.List;

public class ColorLogAdapter extends RecyclerView.Adapter<ColorLogAdapter.ViewHolder> {

    private List<ColorLogObject> mDataset;

    public ColorLogAdapter(List<ColorLogObject> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_log_color, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ColorLogObject colorLog = mDataset.get(position);
        holder.colorCodeTextView.setText(colorLog.getColorCodeText());
        holder.itemView.setBackgroundColor(colorLog.getColor());
    }

    @Override
    public int getItemCount() {
        return this.mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView colorCodeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            colorCodeTextView = (TextView) itemView.findViewById(R.id.colorcode_text);
        }
    }
}
