package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.influxdb.dto.Point;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class AbstractDatabaseRequestTask implements Callable<Pair<String, List<Point>>> {
    protected DataBaseInstance instance;
    Logger LOGGER = LoggerFactory.getLogger(AbstractDatabaseRequestTask.class);

    AbstractDatabaseRequestTask(DataBaseInstance instance) {
        this.instance = instance;
    }

    public DataBaseInstance getInstance() {
        return instance;
    }

    public void setInstance(DataBaseInstance instance) {
        this.instance = instance;
    }
}
