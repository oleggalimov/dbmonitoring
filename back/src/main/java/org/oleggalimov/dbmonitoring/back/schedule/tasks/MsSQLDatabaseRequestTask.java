package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.influxdb.dto.Point;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;

import java.util.Map;

public class MsSQLDatabaseRequestTask extends AbstractDatabaseRequestTask {

    MsSQLDatabaseRequestTask(DataBaseInstance instance) {
        super(instance);
    }

    @Override
    public Map<String, Point> call() throws Exception {
        return null;
    }
}
