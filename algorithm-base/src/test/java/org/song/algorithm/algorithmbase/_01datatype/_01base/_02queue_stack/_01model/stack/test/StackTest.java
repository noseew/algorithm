package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.stack.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.stack.Stack_Array_01;
import org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.stack.Stack_Link_01;

import java.util.Random;
import java.util.Stack;

public class StackTest {

    private int maxVal = 100;
    private int maxSize = 50;

    private Random r = new Random();
    
    @Test
    public void stackLink_test01() {

        Stack_Link_01<Integer> stack = new Stack_Link_01<>();
        Stack<Integer> s = new Stack<>();

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            stack.push(val);
            s.push(val);
        }

        assert stack.length() == s.size();

        for (int i = 0; i < stack.length(); i++) {
            assert stack.pop() == s.pop();
        }
        
        
    }
    
    @Test
    public void stackArray_test01() {

        Stack_Array_01<Integer> stack = new Stack_Array_01<>();
        Stack<Integer> s = new Stack<>();

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            stack.push(val);
            s.push(val);
        }

        assert stack.length() == s.size();

        for (int i = 0; i < stack.length(); i++) {
            assert stack.pop() == s.pop();
        }
        
        
    }
}
