/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.surface4pro.movielicious.Converters;

/**
 * Class for custom Movie objects.
 */
@Entity(tableName = "movie_table")
@TypeConverters({Converters.class})
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movie_id")
    private int movieId;
    private String title;
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    @ColumnInfo(name = "vote_count")
    private int voteCount;
    @ColumnInfo(name = "vote_average")
    private int voteAverage;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    private String description;
    private int popularity;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;
    private boolean video;
    @ColumnInfo(name = "original_language")
    private String originalLanguage;
    @ColumnInfo(name = "genre_ids")
    private int[] genreIds;
    @ColumnInfo(name = "only_for_adults")
    private boolean onlyForAdults;

    private int origin;

    /**
     * Movie constructor.
     *
     * @param id               Id of the movie.
     * @param title            Title of the movie.
     * @param originalTitle    Original Title of the movie.
     * @param voteCount        Number of votes the movie received.
     * @param voteAverage      Average rating the movie got.
     * @param releaseDate      Release date of the movie.
     * @param description      Description of the movie.
     * @param popularity       Popularity rank of the movie.
     * @param posterPath       Path to the poster image.
     * @param backdropPath     Path to the backdrop image.
     * @param video            Has video?
     * @param originalLanguage Original language of the movie.
     * @param genreIds         Array of int ids refereing to the genres.
     * @param onlyForAdults    Is only for adults?
     */
    public Movie(int id, String title, String originalTitle, int voteCount, int voteAverage, String releaseDate, String description, int popularity, String posterPath, String backdropPath, boolean video, String originalLanguage, int[] genreIds, boolean onlyForAdults, int origin) {
        this.movieId = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.description = description;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.video = video;
        this.originalLanguage = originalLanguage;
        this.genreIds = genreIds;
        this.onlyForAdults = onlyForAdults;
        this.origin = origin;
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

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
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

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public boolean isOnlyForAdults() {
        return onlyForAdults;
    }

    public void setOnlyForAdults(boolean onlyForAdults) {
        this.onlyForAdults = onlyForAdults;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }
}