# Movielicious
Udacity Popular Movies Android App Stage 1

Made as part of Udacity's [Android Developer Nanodegree Program](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801).
This app displays the Most Popular and Top Rated Movies and the user can view the movie details (rating, release date, description etc.).

## API Key
The movie information is fetched using [The Movie Database (TMDb)](https://www.themoviedb.org/documentation/api) API.
In order to make this app work, you have to enter your own API key into `gradle.properties` file.

```gradle.properties
MOVIE_DB_API_KEY="Your Api Key"
```

In order to request an [API Key](https://www.themoviedb.org/documentation/api), you will need to create an account.

Libraries
---------
* [Picasso](https://github.com/square/picasso)
