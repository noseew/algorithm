package org.song.algorithm.algorithmbase.from_ma.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.from_ma.RadixSort;


class RadixSortTest {
	
	int[] generateRandomArray() {
		Random r = new Random();

		int[] arr = new int[10000];

		for (int i = 0; i < arr.length; i++)
			arr[i] = r.nextInt(1000);

		return arr;
	}
	

	@Test
	void testSort() {
		int[] a = generateRandomArray();
		int[] result = RadixSort.sort(a);
		Arrays.sort(a);
		boolean same = true;
		
		for (int i = 0; i < a.length; i++) {
			if(result[i] != a[i]) same = false;
		}
		
		assertEquals(true, same);
		
	}

}
