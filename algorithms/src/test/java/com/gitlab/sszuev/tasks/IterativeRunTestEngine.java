package com.gitlab.sszuev.tasks;

import java.util.List;

/**
 * Created by @ssz on 04.11.2021.
 */
public abstract class IterativeRunTestEngine extends RunTestEngine {

    public long getIterationNumber() {
        return -1;
    }

    @Override
    protected List<String> runTask(Algorithm task, List<String> args) {
        long iterationNumber;
        if (TestPropertiesSupport.USE_ASSERTIONS || (iterationNumber = getIterationNumber()) < 2) {
            return super.runTask(task, args);
        }
        List<String> res = null;
        for (int i = 0; i < iterationNumber; i++) {
            res = super.runTask(task, args);
        }
        return res;
    }
}
