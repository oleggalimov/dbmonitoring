package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;

public class PostgresDatabaseRequestTask extends AbstractDatabaseRequestTask {

    PostgresDatabaseRequestTask(DataBaseInstance instance) {
        super(instance);
    }

    @Override
    public String call() throws Exception {
        return "Executing request to " + instance.getType() + " with params: " + this.instance.toString();
    }
}
