package org.song.algorithm.algorithmbase.utils;

import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StopWatchUtils {

    public static void warnup(Runnable runnable) {
        runnable.run();
    }

    public static void run(StopWatch stopWatch, String name, Runnable runnable) {
        stopWatch.start(name);
        runnable.run();
        stopWatch.stop();
    }

    public static String calculate(StopWatch stopWatch) {
        Map<String, List<StopWatch.TaskInfo>> collect = Arrays.stream(stopWatch.getTaskInfo()).collect(Collectors.groupingBy(StopWatch.TaskInfo::getTaskName));
        StringBuilder sb = new StringBuilder();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        collect.forEach((k, v) -> {
            Long sum = v.stream().map(StopWatch.TaskInfo::getTimeMillis).reduce(Long::sum).get();
            BigDecimal ratio = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(totalTimeMillis), 4, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100)).setScale(2);
            sb.append(k).append("\t").append(sum).append("\t").append(ratio).append("%").append("\r\n");
        });
        return sb.toString();
    }


}
