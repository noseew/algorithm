package org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._01string._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._01string._01model.StringBase;
import org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._01string._01model.StringBaseKMP01;

import java.util.Arrays;

public class StringKMPTest {

    @Test
    public void testString01() {
        StringBaseKMP01 main = StringBaseKMP01.ofChars('A', 'A', 'B', 'A', 'A', 'D', 'E', 'F');
        StringBaseKMP01 sub = StringBaseKMP01.ofChars('A', 'A', 'B');
        System.out.println(main.contains(sub));

        StringBase main2 = StringBase.ofChars('A', 'A', 'B', 'A', 'A', 'D', 'E', 'F');
        StringBase sub2 = StringBase.ofChars('A', 'A', 'B');
        System.out.println(main2.contains(sub2));
    }

}
