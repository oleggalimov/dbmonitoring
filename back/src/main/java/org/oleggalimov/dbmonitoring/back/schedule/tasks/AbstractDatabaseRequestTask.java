package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;

import java.util.concurrent.Callable;

public abstract class AbstractDatabaseRequestTask implements Callable<String> {
    protected DataBaseInstance instance;

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
