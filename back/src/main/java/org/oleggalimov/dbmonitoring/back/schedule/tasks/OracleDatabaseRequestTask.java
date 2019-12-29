package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;

public class OracleDatabaseRequestTask extends AbstractDatabaseRequestTask {

    OracleDatabaseRequestTask(DataBaseInstance instance) {
        super(instance);
    }

    @Override
    public String call() throws Exception {
        return "Executing request to " + instance.getType() + " with params: " + this.instance.toString();
    }
}
