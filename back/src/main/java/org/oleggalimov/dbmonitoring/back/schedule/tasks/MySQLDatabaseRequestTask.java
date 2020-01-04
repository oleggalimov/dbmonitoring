package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.influxdb.dto.Point;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;

import java.util.Map;

public class MySQLDatabaseRequestTask extends AbstractDatabaseRequestTask {

    MySQLDatabaseRequestTask(DataBaseInstance instance) {
        super(instance);
    }

    @Override
    public Map<String, Point> call() {
        throw new UnsupportedOperationException("Method is unsupported!");
    }
}
