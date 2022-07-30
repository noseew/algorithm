package org.song.algorithm.base.utils;

import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StopWatchUtils {


    public static void test(String title, Runnable task) {
        if (task == null) return;
        title = (title == null) ? "" : ("【" + title + "】");
        System.out.println(title);
        System.out.println("开始：" + LocalDateTime.now());
        long begin = SystemClock.now();
        task.run();
        long end = SystemClock.now();
        System.out.println("结束：" + LocalDateTime.now());
        double delta = (end - begin) / 1000.0;
        System.out.println("耗时：" + delta + "秒");
        System.out.println("-------------------------------------");
    }

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
