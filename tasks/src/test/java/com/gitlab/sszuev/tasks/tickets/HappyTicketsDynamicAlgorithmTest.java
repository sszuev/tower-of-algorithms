package com.gitlab.sszuev.tasks.tickets;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 08.08.2021.
 */
public class HappyTicketsDynamicAlgorithmTest extends RunTestEngine {
    public static Stream<Data> listData() throws Exception {
        return listData("/tickets");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new HappyTicketsDynamicAlgorithm();
    }
}
