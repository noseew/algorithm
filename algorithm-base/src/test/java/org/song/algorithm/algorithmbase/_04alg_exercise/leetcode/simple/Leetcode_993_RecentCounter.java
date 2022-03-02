package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 933. 最近的请求次数
 *
 * 写一个 RecentCounter 类来计算特定时间范围内最近的请求。
 *
 * 请你实现 RecentCounter 类：
 *
 * RecentCounter() 初始化计数器，请求数为 0 。
 * int ping(int t) 在时间 t 添加一个新请求，其中 t 表示以毫秒为单位的某个时间，并返回过去 3000 毫秒内发生的所有请求数（包括新请求）。确切地说，返回在 [t-3000, t] 内发生的请求数。
 * 保证 每次对 ping 的调用都使用比之前更大的 t 值。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-recent-calls
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_993_RecentCounter {

    @Test
    public void test() throws InterruptedException {
        RecentCounter recentCounter = new RecentCounter();
//        for (int i = 0; i < 100; i++) {
//            int ping = recentCounter.ping(10);
//            System.out.println(LocalDateTime.now() + ": " + ping + ":" + i);
//        }

        //  [1],[100],[3001],[3002]]
        int ping = recentCounter.ping(1);
        System.out.println(LocalDateTime.now() + ": " + ping);
        TimeUnit.MILLISECONDS.sleep(1);
        ping = recentCounter.ping(100);
        System.out.println(LocalDateTime.now() + ": " + ping);
        TimeUnit.MILLISECONDS.sleep(1);
        ping = recentCounter.ping(3001);
        System.out.println(LocalDateTime.now() + ": " + ping);
        TimeUnit.MILLISECONDS.sleep(1);
        ping = recentCounter.ping(3002);
        System.out.println(LocalDateTime.now() + ": " + ping);

    }

    class RecentCounter {

        TreeMap<Long, Integer> counter = new TreeMap<>();

        public RecentCounter() {

        }

        /**
         * 未通过
         *
         */
        public int ping(int t) {
            long now = System.currentTimeMillis();

            Integer val = counter.get(now);
            if (val != null) {
                counter.put(now, ++val);
            } else {
                counter.put(now, 1);
            }

            Long beforeKey = counter.ceilingKey(now - t);
            AtomicInteger times = new AtomicInteger();
            if (beforeKey != null) {
                counter.forEach((k, v) -> {
                    if (k < beforeKey) {
                        return;
                    }
                    times.addAndGet(v);
                });
            }
            return times.get();
        }
    }
}
