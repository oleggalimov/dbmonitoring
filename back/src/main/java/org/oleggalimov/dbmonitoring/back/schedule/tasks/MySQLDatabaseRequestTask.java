package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.influxdb.dto.Point;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

public class MySQLDatabaseRequestTask extends AbstractDatabaseRequestTask {

    MySQLDatabaseRequestTask(DataBaseInstance instance) {
        super(instance);
    }

    @Override
    public Pair<String, List<Point>> call() throws Exception {
        return null;
    }
}
