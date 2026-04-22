package edu.movie.whitman.service;

import edu.movie.whitman.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
public class MovieServiceTest {
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
    }

    @Test
    void testCreateAndGetMovie() {
        Movie movie = new Movie(null, "Inception", "Christopher Nolan", 2010);
        Movie created = movieService.createMovie(movie);
        assertNotNull(created.getId());
        Optional<Movie> found = movieService.getMovieById(created.getId());
        assertTrue(found.isPresent());
        assertEquals("Inception", found.get().getTitle());
    }

    @Test
    void testUpdateMovie() {
        Movie movie = new Movie(null, "Inception", "Christopher Nolan", 2010);
        Movie created = movieService.createMovie(movie);
        Movie updated = new Movie(null, "Interstellar", "Christopher Nolan", 2014);
        Optional<Movie> result = movieService.updateMovie(created.getId(), updated);
        assertTrue(result.isPresent());
        assertEquals("Interstellar", result.get().getTitle());
        assertEquals(2014, result.get().getReleaseYear());
    }

    @Test
    void testDeleteMovie() {
        Movie movie = new Movie(null, "Inception", "Christopher Nolan", 2010);
        Movie created = movieService.createMovie(movie);
        boolean deleted = movieService.deleteMovie(created.getId());
        assertTrue(deleted);
        assertFalse(movieService.getMovieById(created.getId()).isPresent());
    }

    @Test
    void testGetAllMovies() {
        movieService.createMovie(new Movie(null, "A", "Dir1", 2000));
        movieService.createMovie(new Movie(null, "B", "Dir2", 2001));
        assertEquals(2, movieService.getAllMovies().size());
    }

    @Test
    void testUpdateNonexistentMovie() {
        Movie updated = new Movie(null, "Ghost", "Nobody", 1999);
        assertFalse(movieService.updateMovie(999L, updated).isPresent());
    }

    @Test
    void testDeleteNonexistentMovie() {
        assertFalse(movieService.deleteMovie(999L));
    }
}
