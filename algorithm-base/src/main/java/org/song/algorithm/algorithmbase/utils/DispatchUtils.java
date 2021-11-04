package org.song.algorithm.algorithmbase.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DispatchUtils {

    private Map<String, Reporter> tasks;

    private DispatchUtils() {

    }

    public static DispatchUtils getInstance() {
        DispatchUtils dispatchUtils = new DispatchUtils();
        dispatchUtils.tasks = new HashMap<>();
        return dispatchUtils;
    }

    public void add(Reporter reporter) {
        tasks.put(reporter.getName(), reporter);
    }

    public Reporter get(String name) {
        return tasks.get(name);
    }

    public void increment(String name, int val) {
        Reporter reporter = tasks.computeIfAbsent(name, e -> new Reporter(name, new AtomicInteger(0)));
        reporter.getCount().addAndGet(val);
    }

    public String toPrettyString() {
        List<Reporter> reporterList = tasks.values().stream().sorted(Comparator.comparing(e -> e.getCount().get())).collect(Collectors.toList());
        long total = reporterList.stream().mapToInt(e -> e.getCount().get()).sum();
        StringBuilder sb = new StringBuilder();
        for (Reporter reporter : reporterList) {
            sb.append(reporter.getName())
                    .append(":")
                    .append(reporter.getCount().get())
                    .append(" ")
                    .append(BigDecimal.valueOf(reporter.getCount().get()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(total), 4, BigDecimal.ROUND_HALF_UP))
                    .append("\r\n");
        }
        return sb.toString();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Reporter {
        private String name;
        private AtomicInteger count = new AtomicInteger();
    }

}
