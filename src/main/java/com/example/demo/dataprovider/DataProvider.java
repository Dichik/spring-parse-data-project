package com.example.demo.dataprovider;

import antlr.StringUtils;
import com.example.demo.entity.Director;
import com.example.demo.entity.Genre;
import com.example.demo.repository.DirectorRepository;
import com.example.demo.repository.GenreRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataProvider {

    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;

    private List<String[]> allData;

    public void loadData() {
        try {
            String strFile = "src/main/java/com/example/demo/csv/imdb_top_1000.csv";
            CSVReader reader = new CSVReader(new FileReader(strFile));
            allData = reader.readAll();

            // fetchDirectorsData();
            // fetchGenresData();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private void fetchDirectorsData() {
        int index = getIndexOfTheField("Director");
        List<Director> directors = getDirectors(index);
        directorRepository.saveAll(directors);
    }

    private void fetchGenresData() {
        int index = getIndexOfTheField("Genre");
        Set<Genre> genres = getGenres(index);
        genreRepository.saveAll(genres);
        // connect all data with movie
    }

    private Set<Genre> getGenres(int index) {
        return allData.stream().flatMap(d -> Arrays.stream(d[index].split(",")))
                .map(dd -> new Genre(dd.trim()))
                .filter(genre -> !Objects.equals(genre.getName(), StringUtil.EMPTY_STRING))
                .collect(Collectors.toSet());
    }

    private List<Director> getDirectors(int index) {
        return allData.stream().map(d -> {
            String[] data = d[index].split(" ");
            return new Director(
                    data[0],
                    (data.length > 1) ? data[1] : "Unknown"
            );
        }).collect(Collectors.toList());
    }

    private Integer getIndexOfTheField(String fieldName) {
        return IntStream.range(0, allData.get(0).length)
                .filter(i -> Objects.equals(allData.get(0)[i], fieldName))
                .findFirst().orElse(-1);
        // TODO throw exception
    }

}