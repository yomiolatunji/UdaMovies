package com.yomiolatunji.udamovies;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {


    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private List<Movies> items;

    public MoviesAdapter(Activity hostActivity) {
        this.activity = hostActivity;
        layoutInflater = LayoutInflater.from(activity);
        items = new ArrayList<>();
        setHasStableIds(true);
    }

    @Override
    public MoviesAdapter.MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoviesHolder(
                layoutInflater.inflate(R.layout.item_movie, parent, false));

    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MoviesHolder holder, int position) {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(activity.getResources(), R.drawable.ic_local_movies, null);
        Movies movies = getItem(position);
        if (movies.getPosterPath() != null)
            Picasso.with(activity)
                    .load("http://image.tmdb.org/t/p/w185/" + movies.getPosterPath())
                    .placeholder(vectorDrawableCompat)
                    .error(vectorDrawableCompat)
                    .fit()
                    .into(holder.image);
        else
            holder.image.setImageResource(R.drawable.ic_local_movies);
        holder.movie = movies;
        holder.title.setText(movies.getTitle());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.ARG_MOVIE, holder.movie);
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                            holder.image, context.getString(R.string.transition_movies_avatar));

                }

                context.startActivity(intent, options.toBundle());

            }
        });
    }


    private Movies getItem(int position) {
        return items.get(position);
    }


    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addAndResort(List<Movies> movies) {
        deduplicateAndAdd(movies);
        notifyDataSetChanged();
    }


    /**
     * De-dupe as the same item can be returned by multiple feeds
     */
    private void deduplicateAndAdd(List<Movies> newItems) {
        final int count = getDataItemCount();
        for (Movies newItem : newItems) {
            boolean add = true;
            for (int i = 0; i < count; i++) {
                Movies existingItem = getItem(i);
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

    private void add(Movies item) {
        items.add(item);
    }


    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public int getItemCount() {
        return getDataItemCount();
    }


    public int getDataItemCount() {
        return items.size();
    }


    class MoviesHolder extends RecyclerView.ViewHolder {

        public Movies movie;
        ImageView image;
        TextView title;
        View mView;

        public MoviesHolder(View itemView) {
            super(itemView);
            mView = itemView;
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }

    }


}
