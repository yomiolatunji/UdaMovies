package com.yomiolatunji.udamovies;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yomiolatunji.udamovies.data.entity.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {


    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private List<Trailer> items;

    public TrailerAdapter(Activity hostActivity) {
        this.activity = hostActivity;
        layoutInflater = LayoutInflater.from(activity);
        items = new ArrayList<>();
        setHasStableIds(true);
    }

    @Override
    public TrailerAdapter.TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailerHolder(
                layoutInflater.inflate(R.layout.item_trailer, parent, false));

    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.TrailerHolder holder, int position) {
        final Trailer trailer = getItem(position);

        holder.trailer = trailer;
        holder.title.setText(trailer.getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }


            }
        });
    }

    private Trailer getItem(int position) {
        return items.get(position);
    }


    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addAndResort(List<Trailer> trailers) {
        deduplicateAndAdd(trailers);
        notifyDataSetChanged();
    }


    /**
     * De-dupe as the same item can be returned by multiple feeds
     */
    private void deduplicateAndAdd(List<Trailer> newItems) {
        final int count = getDataItemCount();
        for (Trailer newItem : newItems) {
            boolean add = true;
            for (int i = 0; i < count; i++) {
                Trailer existingItem = getItem(i);
                if (existingItem.equals(newItem)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                add(newItem);
            }
        }
    }

    private void add(Trailer item) {
        items.add(item);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return getDataItemCount();
    }


    public int getDataItemCount() {
        return items.size();
    }


    class TrailerHolder extends RecyclerView.ViewHolder {

        public Trailer trailer;
        TextView title;
        View mView;

        public TrailerHolder(View itemView) {
            super(itemView);
            mView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
        }

    }


}
