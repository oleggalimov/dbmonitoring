package org.oleggalimov.dbmonitoring.back.schedule;

import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class DataBaseRequestScheduler {
    private final CopyOnWriteArraySet<DataBaseInstance> instanceSet;

    public DataBaseRequestScheduler(CopyOnWriteArraySet<DataBaseInstance> instanceSet) {
        this.instanceSet = instanceSet;
    }

    @Scheduled(fixedRate = 60 * 1000L)
    private void schedule() {
        System.out.println("------------------------------------Scheduled task start-----------------------");
        instanceSet.stream()
                .map(instance -> {
                    switch (instance.getType()) {
                        case ORACLE:
                            return "Oracle";
                        case MSSQL:
                            return "MSSQL";
                        case MYSQL:
                            return "MySql";
                        case POSTGRES:
                            return "Postgres";
                        default:
                            return "Undefined";
                    }
                })
                .forEach(System.out::println);
        System.out.println("------------------------------------Scheduled task end-----------------------");

    }
}
