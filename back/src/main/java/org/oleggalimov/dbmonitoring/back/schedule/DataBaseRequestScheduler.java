package org.oleggalimov.dbmonitoring.back.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataBaseRequestScheduler {
    @Scheduled(fixedRate = 60 * 1000L)
    private void schedule() {
        System.out.println("------------------------------------ТАЙМАУТ-----------------------");
    }
}
