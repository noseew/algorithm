package org.song.algorithm.algorithmbase.utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    
    
    
    private ThreadUtils() {
        
    }
    
    public static void sleepRandom(TimeUnit timeUnit, long time) {
        try {
            timeUnit.sleep(ThreadLocalRandom.current().nextLong(time));
        } catch (InterruptedException e) {
        }
    }
    
    public static void sleep(TimeUnit timeUnit, long time) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
        }
    }
}
