package com.github.client.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.client.R;
import com.github.client.api.model.ItemDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 2/19/17.
 * Adapter for displaying detail Items in a list
 */
public class ItemAdapter extends ArrayAdapter<ItemDetail> {

    private Context context;
    private List<ItemDetail> items;

    public ItemAdapter(Context context, List<ItemDetail> objects) {
        super(context, R.layout.dialog_row, objects);
        this.context = context;
        this.items = new ArrayList<>();
        for (ItemDetail item : objects) {
            if (!item.isEmpty()) { // Keep only the available values
                this.items.add(item);
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.dialog_row, parent, false);
            holder = new ViewHolder();
            holder.textViewLabel = (TextView) row.findViewById(R.id.label);
            holder.textViewValue = (TextView) row.findViewById(R.id.value);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.textViewLabel.setText(items.get(position).getLabel());
        holder.textViewValue.setText(items.get(position).getValue());
        return row;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    static class ViewHolder {
        TextView textViewLabel;
        TextView textViewValue;
    }
}
