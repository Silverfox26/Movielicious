package com.example.surface4pro.movielicious.model;

public class Movie {

    private int id;
    private String title;
    private String originalTitle;
    private int voteCount;
    private int voteAverage;
    private String releaseDate;
    private String description;
    private int popularity;
    private String posterPath;
    private String backdropPath;
    private boolean video;
    private String originalLanguage;
    private int[] genreIds;
    private boolean onlyForAdults;

    public Movie(int id, String title, String originalTitle, int voteCount, int voteAverage, String releaseDate, String description, int popularity, String posterPath, String backdropPath, boolean video, String originalLanguage, int[] genreIds, boolean onlyForAdults) {
        this.id = id;
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
}
