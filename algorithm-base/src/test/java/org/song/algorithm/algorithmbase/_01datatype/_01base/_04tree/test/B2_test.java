package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree04_B_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree04_B_base2;

public class B2_test {
    @Test
    public void test_start1() {
        Tree04_B_base2<String> st = new Tree04_B_base2<String>();

        st.put("128.112.136.12");
        st.put("128.112.136.11");
        st.put("128.112.128.15");
        st.put("130.132.143.21");
        st.put("209.052.165.60");
        st.put("17.112.152.32");
        st.put("207.171.182.16");
        st.put("66.135.192.87");
        st.put("64.236.16.20");
        st.put("216.239.41.99");
        st.put("199.239.136.200");
        st.put("207.126.99.140");
        st.put("143.166.224.230");
        st.put("66.35.250.151");
        st.put("199.181.135.201");
        st.put("63.111.66.11");
        st.put("216.109.118.65");


        System.out.println("128.112.136.12:  " + st.get("128.112.136.12"));
        System.out.println("216.109.118.65: " + st.get("216.109.118.65"));
        System.out.println();

        System.out.println("size:    " + st.size());
        System.out.println("height:  " + st.height());
        System.out.println(st);
        System.out.println();
    }
}
