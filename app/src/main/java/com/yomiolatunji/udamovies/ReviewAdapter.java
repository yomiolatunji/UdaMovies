package com.yomiolatunji.udamovies;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yomiolatunji.udamovies.data.entity.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {


    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private List<Review> items;

    public ReviewAdapter(Activity hostActivity) {
        this.activity = hostActivity;
        layoutInflater = LayoutInflater.from(activity);
        items = new ArrayList<>();
        setHasStableIds(true);
    }

    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewHolder(
                layoutInflater.inflate(R.layout.item_review, parent, false));

    }

    @Override
    public void onBindViewHolder(final ReviewAdapter.ReviewHolder holder, int position) {
        final Review review = getItem(position);

        holder.review = review;
        holder.authorView.setText(review.getAuthor());
        holder.reviewView.setText(Html.fromHtml(review.getContent()));

    }


    private Review getItem(int position) {
        return items.get(position);
    }


    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addAndResort(List<Review> reviews) {
        deduplicateAndAdd(reviews);
        notifyDataSetChanged();
    }


    /**
     * De-dupe as the same item can be returned by multiple feeds
     */
    private void deduplicateAndAdd(List<Review> newItems) {
        final int count = getDataItemCount();
        for (Review newItem : newItems) {
            boolean add = true;
            for (int i = 0; i < count; i++) {
                Review existingItem = getItem(i);
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

    private void add(Review item) {
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


    class ReviewHolder extends RecyclerView.ViewHolder {

        public Review review;
        TextView reviewView;
        TextView authorView;
        View mView;

        public ReviewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            reviewView = (TextView) itemView.findViewById(R.id.review);
            authorView = (TextView) itemView.findViewById(R.id.author);
        }

    }


}
