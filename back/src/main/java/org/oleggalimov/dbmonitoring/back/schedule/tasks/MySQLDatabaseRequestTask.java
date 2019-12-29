package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;

public class MySQLDatabaseRequestTask extends AbstractDatabaseRequestTask {

    MySQLDatabaseRequestTask(DataBaseInstance instance) {
        super(instance);
    }

    @Override
    public String call() {
        throw new UnsupportedOperationException("Method is unsupported!");
    }
}
