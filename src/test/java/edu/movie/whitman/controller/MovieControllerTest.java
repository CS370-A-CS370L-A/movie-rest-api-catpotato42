package edu.movie.whitman.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.movie.whitman.model.Movie;
import edu.movie.whitman.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        movie1 = new Movie(1L, "Inception", "Christopher Nolan", 2010);
        movie2 = new Movie(2L, "Interstellar", "Christopher Nolan", 2014);
    }

    @Test
    void testGetAllMovies() throws Exception {
        Mockito.when(movieService.getAllMovies()).thenReturn(Arrays.asList(movie1, movie2));
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"));
    }

    @Test
    void testGetMovieByIdFound() throws Exception {
        Mockito.when(movieService.getMovieById(1L)).thenReturn(Optional.of(movie1));
        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    void testGetMovieByIdNotFound() throws Exception {
        Mockito.when(movieService.getMovieById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/movies/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie with ID 99 not found"));
    }

    @Test
    void testCreateMovie() throws Exception {
        Movie toCreate = new Movie(null, "Dunkirk", "Christopher Nolan", 2017);
        Movie created = new Movie(3L, "Dunkirk", "Christopher Nolan", 2017);
        Mockito.when(movieService.createMovie(any(Movie.class))).thenReturn(created);
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void testUpdateMovieFound() throws Exception {
        Movie updated = new Movie(1L, "Tenet", "Christopher Nolan", 2020);
        Mockito.when(movieService.updateMovie(eq(1L), any(Movie.class))).thenReturn(Optional.of(updated));
        mockMvc.perform(put("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tenet"));
    }

    @Test
    void testUpdateMovieNotFound() throws Exception {
        Mockito.when(movieService.updateMovie(eq(99L), any(Movie.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/movies/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie1)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie with ID 99 not found"));
    }

    @Test
    void testDeleteMovieFound() throws Exception {
        Mockito.when(movieService.deleteMovie(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/movies/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteMovieNotFound() throws Exception {
        Mockito.when(movieService.deleteMovie(99L)).thenReturn(false);
        mockMvc.perform(delete("/api/movies/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie with ID 99 not found"));
    }
}
