package com.example.demo.dataprovider;

import antlr.StringUtils;
import com.example.demo.entity.Director;
import com.example.demo.entity.Genre;
import com.example.demo.repository.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataProvider {

    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final AwardRepository awardRepository;
    private final MovieRepository movieRepository;
    private final ActorMovieRepository actorMovieRepository;

    private final DataManager dataManager;

    public void loadData() {
        parseIMDBRatingCSVFile();
        parseAwardsCSVFile();
        parseActorsMovieData();
    }

    private void parseAwardsCSVFile() {
        try {
            String strFile = "src/main/java/com/example/demo/csv/awards.csv";
            CSVReader reader = new CSVReader(new FileReader(strFile));
            dataManager.setAllData(reader.readAll());

            awardRepository.saveAll(dataManager.fetchAwardsData());
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    // TODO don't add data for not actor or actress

    private void parseIMDBRatingCSVFile() {
        try {
            String strFile = "src/main/java/com/example/demo/csv/imdb_top_1000.csv";
            CSVReader reader = new CSVReader(new FileReader(strFile));
            dataManager.setAllData(reader.readAll());

            directorRepository.saveAll(dataManager.fetchDirectorsData());
            genreRepository.saveAll(dataManager.fetchGenresData());
            actorRepository.saveAll(dataManager.fetchActorsData());

            movieRepository.saveAll(dataManager.fetchMoviesData());
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private void parseActorsMovieData() {
        try {
            String strFile = "src/main/java/com/example/demo/csv/actorfilms.csv";
            CSVReader reader = new CSVReader(new FileReader(strFile));
            dataManager.setAllData(reader.readAll());

            actorMovieRepository.saveAll(dataManager.fetchActorMovieData());
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

}