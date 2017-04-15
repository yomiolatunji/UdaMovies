package com.yomiolatunji.udamovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Oluwayomi on 4/14/2017.
 */

public class Movies implements Parcelable {
    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
    private int id;
    private String title;
    private String synopsis;
    private boolean adult;
    private String releaseDate;
    private String posterPath;
    private String backdropPath;
    private int voteCount;
    private float voteAverage;

    public Movies() {
    }

    public Movies(int id, String title, String synopsis, boolean adult, String releaseDate, String posterPath, String backdropPath, int voteCount, float voteAverage) {
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.adult = adult;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }

    protected Movies(Parcel in) {
        id = in.readInt();
        title = in.readString();
        synopsis = in.readString();
        adult = in.readByte() != 0;
        releaseDate = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        voteCount = in.readInt();
        voteAverage = in.readFloat();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", adult=" + adult +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", voteCount=" + voteCount +
                ", voteAverage=" + voteAverage +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeInt(voteCount);
        dest.writeFloat(voteAverage);
    }
}
