package org.oleggalimov.dbmonitoring.back.schedule;

import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.schedule.tasks.DatabaseRequestTaskFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class DataBaseRequestScheduler {
    private final CopyOnWriteArraySet<DataBaseInstance> instanceSet;
    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseRequestTaskFactory.class);

    public DataBaseRequestScheduler(CopyOnWriteArraySet<DataBaseInstance> instanceSet) {
        this.instanceSet = instanceSet;
    }

    @Scheduled(fixedRate = 60 * 1000L)
    private void schedule() {
        ExecutorService service = Executors.newCachedThreadPool();
        LOGGER.debug("Start another task at {}", new Date());

        Map<String, Future> futures = new HashMap<>();
        LOGGER.debug("Start creation futures at {}, instances size: {}", new Date(), instanceSet.size());
        instanceSet.stream()
                .map(DatabaseRequestTaskFactory::getTask)
                .filter(Objects::nonNull)
                .forEach(task -> futures.put(task.getInstance().getId(), service.submit(task)));
        LOGGER.debug("End creation futures at {}, total: {}", new Date(), futures.size());

        LOGGER.debug("Start processing data at {}", new Date());
        futures.keySet().parallelStream()
                .map(key -> {
                    try {
                        return futures.get(key).get(50L, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        LOGGER.debug("Error collecting metrics with instance {}", key);
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(System.out::println);
        LOGGER.debug("End processing data at {}", new Date());

        LOGGER.debug("End another task at {}", new Date());

    }
}
