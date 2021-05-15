package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 401. 二进制手表
 * 二进制手表顶部有 4 个 LED 代表 小时（0-11），底部的 6 个 LED 代表 分钟（0-59）。每个 LED 代表一个 0 或 1，最低位在右侧。
 * <p>
 * 输入：turnedOn = 1
 * 输出：["0:01","0:02","0:04","0:08","0:16","0:32","1:00","2:00","4:00","8:00"]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/binary-watch
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_401_readBinaryWatch {

    @Test
    public void test() {

        System.out.println(readBinaryWatch(1).toArray());

    }

    /**
     * 未完成
     */
    public List<String> readBinaryWatch(int turnedOn) {
        List<String> strings = new ArrayList<>();
        if (turnedOn > 11) {
            return strings;
        }
        return strings;



    }

    private boolean isValidMinute(int a) {
        return a <= 59;
    }

    private boolean isValidHour(int a) {
        return a <= 23;
    }


}
