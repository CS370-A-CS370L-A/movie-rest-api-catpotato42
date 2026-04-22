package edu.movie.whitman.service;

import edu.movie.whitman.model.Movie;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    private final Map<Long, Movie> movieStore = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movieStore.values());
    }

    public Optional<Movie> getMovieById(Long id) {
        return Optional.ofNullable(movieStore.get(id));
    }

    public Movie createMovie(Movie movie) {
        Long id = idCounter.getAndIncrement();
        movie.setId(id);
        movieStore.put(id, movie);
        return movie;
    }

    public Optional<Movie> updateMovie(Long id, Movie updatedMovie) {
        if (!movieStore.containsKey(id)) {
            return Optional.empty();
        }
        updatedMovie.setId(id);
        movieStore.put(id, updatedMovie);
        return Optional.of(updatedMovie);
    }

    public boolean deleteMovie(Long id) {
        return movieStore.remove(id) != null;
    }
}
