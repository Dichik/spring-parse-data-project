package com.example.demo.dataprovider;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import io.netty.util.internal.StringUtil;
import lombok.*;
import org.hibernate.id.UUIDGenerationStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.processing.Generated;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Setter @Getter
@Service
@RequiredArgsConstructor
public class DataManager {

    private List<String[]> allData;

    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final AwardRepository awardRepository;
    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;

    private final String DIRECTOR_FIELD_NAME = "Director";
    private final String GENRE_FIELD_NAME = "Genre";
    private final String ACTOR_FIELD_NAME = "(Star[0-9]*)";
    private final String UNKNOWN_FIELD = "Unknown";

    public Set<Director> fetchDirectorsData() {
        List<Integer> indexes = getIndexOfTheField(DIRECTOR_FIELD_NAME);
        return getDirectors(indexes);
    }

    public List<Rating> fetchRating() {
        return null;
    }

    public List<Movie> fetchMoviesData() {
        List<Movie> movies = new ArrayList<>();
        List<Rating> ratings = new ArrayList<>();
        for(int i = 1; i < allData.size(); ++ i) {
            Long directorId = -1L;
            String[] directorData = allData.get(i)[5].split(" ");
            String directorFirstName = directorData[0];
            String directorSecondName = directorData.length > 1 ? directorData[1] : UNKNOWN_FIELD;
            if(directorRepository.existsDirectorByFirstNameAndSecondName(directorFirstName, directorSecondName)) {
                directorId = directorRepository.findDirectorByFirstNameAndSecondName(directorFirstName, directorSecondName).get().getId();
            }
            Double gross = Double.valueOf(0);
            try {
                gross = Double.valueOf(allData.get(i)[11]);
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }

            Year year = Year.of(2000);
            try {
                year = Year.parse(allData.get(i)[1]);
            } catch (DateTimeParseException ex) {
                System.out.println(ex);
            }

            Long noOfVotes = Long.valueOf(0);
            try {
                noOfVotes = Long.valueOf(allData.get(i)[10]);
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            Rating rating = Rating.builder()
                    .id(UUID.randomUUID())
                    .score(Double.valueOf(allData.get(i)[4]))
                    .noOfVotes(noOfVotes)
                    .build();
            ratings.add(rating);

            Movie movie = Movie.builder()
                    .directorId(directorId)
                    .runtime(allData.get(i)[2])
                    .releasedYear(String.valueOf(year.getValue()))
                    .gross(gross)
                    .ratingId(rating.getId())
                    .title(allData.get(i)[0])
                    .build();
            movies.add(movie);
        }
        ratingRepository.saveAll(ratings);
        return movies;
    }

    public Set<Genre> fetchGenresData() {
        final List<Integer> indexes = getIndexOfTheField(GENRE_FIELD_NAME);
        return getGenres(indexes);
        // connect all data with movie
    }

    public Set<Actor> fetchActorsData() {
        final List<Integer> indexes = getIndexOfTheField(ACTOR_FIELD_NAME);
        return getActors(indexes);
        // rewrite getIndex to return list
    }


    public List<Award> fetchAwardsData() {
        List<Award> awards = new ArrayList<>();
        for(int i = 1; i < allData.size(); ++ i) {
            Long filmId = -1L;
            if(movieRepository.existsByTitle(allData.get(i)[5])) {
                Optional<Movie> movie = movieRepository.findByTitle(allData.get(i)[5]);
                if(movie.isPresent()) {
                    filmId = movie.get().getId();
                }
            }
            Long actorId = -1L;
            String[] data = allData.get(i)[4].split(" ");
            String firstName = data[0];
            String secondName = data.length > 1 ? data[1] : UNKNOWN_FIELD;
            if(actorRepository.existsByFirstNameAndSecondName(firstName, secondName)) {
                Optional<Actor> actor = actorRepository.findByFirstNameAndSecondName(firstName, secondName);
                if(actor.isPresent()) {
                    actorId = actor.get().getId();
                }
            }
            Award award = Award.builder()
                    .year(allData.get(i)[0])
                    .ceremony(Integer.valueOf(allData.get(i)[1]))
                    .awardName(allData.get(i)[2])
                    .winner(allData.get(i)[3] != null && !Objects.equals(allData.get(i)[3], ""))
                    .actorId(actorId)
                    .filmId(filmId)
                    .build();
            awards.add(award);
        }
        return awards;
    }

    private Set<Actor> getActors(final List<Integer> indexes) {
        return indexes.stream()
                .map(index -> allData.stream().map(d -> {
                    String[] data = d[index].split(" ");
                    return new Actor(
                            data[0],
                            (data.length > 1) ? data[1] : UNKNOWN_FIELD
                    );
                }).collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<Genre> getGenres(final List<Integer> indexes) {
        return indexes.stream().map(index -> allData.stream().flatMap(d -> Arrays.stream(d[index].split(",")))
                        .map(dd -> new Genre(dd.trim()))
                        .filter(genre -> !Objects.equals(genre.getName(), StringUtil.EMPTY_STRING))
                        .collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<Director> getDirectors(final List<Integer> indexes) {
        return indexes.stream()
                .map(index -> allData.stream().map(d -> {
                    String[] data = d[index].split(" ");
                    return new Director(
                            data[0],
                            (data.length > 1) ? data[1] : UNKNOWN_FIELD
                    );
                }).collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private List<Integer> getIndexOfTheField(String fieldName) {
        List<Integer> list = new ArrayList<>();
        int bound = allData.get(0).length;
        for (int i = 0; i < bound; i++) {
            if (Pattern.matches(fieldName, allData.get(0)[i])) {
                Integer integer = i;
                list.add(integer);
            }
        }
        return list;

        // TODO throw exception
    }

    public Set<ActorMovie> fetchActorMovieData() {
        List<Rating> ratings = new ArrayList<>();
        Set<ActorMovie> actorMovies = new HashSet<>();
        for(int i = allData.size() - 1000; i < allData.size(); ++ i) {
            Long noOfVotes = Long.valueOf(0);
            try {
                noOfVotes = Long.valueOf(allData.get(i)[3]);
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            Rating rating = Rating.builder()
                    .id(UUID.randomUUID())
                    .score(Double.valueOf(allData.get(i)[4]))
                    .noOfVotes(noOfVotes)
                    .build();
            ratings.add(rating);

            Long movieId = -1L;

            Long actorId = -1L;

            if(movieRepository.existsByTitle(allData.get(i)[1])) {
                Optional<Movie> movie = movieRepository.findByTitle(allData.get(i)[1]);
                if(movie.isPresent()) {
                    movieId = movie.get().getId();
                }
            }
            String[] data = allData.get(i)[0].split(" ");
            String firstName = data[0];
            String secondName = data.length > 1 ? data[1] : UNKNOWN_FIELD;
            if(actorRepository.existsByFirstNameAndSecondName(firstName, secondName)) {
                Optional<Actor> actor = actorRepository.findByFirstNameAndSecondName(firstName, secondName);
                if(actor.isPresent()) {
                    actorId = actor.get().getId();
                }
            }

            ActorMovie actorMovie = ActorMovie.builder()
                    .movieId(movieId)
                    .actorId(actorId)
                    .ratingId(rating.getId())
                    .build();
            actorMovies.add(actorMovie);
        }
        return actorMovies;
    }
}
