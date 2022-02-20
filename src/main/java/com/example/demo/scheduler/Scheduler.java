package com.example.demo.scheduler;

import com.example.demo.dataprovider.DataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final DataProvider dataProvider;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void schedule() {
        log.info("Scheduler started at {}", Instant.now());
        dataProvider.loadData();
    }

}
