package org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._01string._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._01string._01model.StringBase;

public class StringTest {

    @Test
    public void testString01() {
        StringBase str = StringBase.ofChars('A', 'B', 'C', 'D', 'E', 'F');

        System.out.println(str);

        assert str.length() == 6;
        
        assert StringBase.ofChars('A', 'B').eq(str.subString(0, 2));

        assert str.append(StringBase.ofChars('G')).eq(StringBase.ofChars('A', 'B', 'C', 'D', 'E', 'F', 'G'));

        assert str.insert(1, StringBase.ofChars('A', 'B')).eq(StringBase.ofChars('A', 'A', 'B', 'B', 'C', 'D', 'E', 'F'));

        assert str.remove(0, 3).eq(StringBase.ofChars('D', 'E', 'F'));

        assert str.charAt('C') == 2;
        
        assert str.indexOf(2) == 'C';

    }
}
