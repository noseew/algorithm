package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_openAddressing_01;

public class HashMap_clash_test {

    @Test
    public void test_01_start() {
        HashMap_openAddressing_01<String, String> map = new HashMap_openAddressing_01<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        map.put("5", "5");
        map.put("6", "6");
        map.put("7", "7");
        map.put("8", "8");
        System.out.println(map.get("1"));
        System.out.println(map.remove("2"));
        System.out.println();
    }
}
