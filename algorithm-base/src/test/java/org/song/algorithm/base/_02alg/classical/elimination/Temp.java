package org.song.algorithm.base._02alg.classical.elimination;

import java.util.*;

public class Temp {
    enum SEGMENT_ZONE {
        PROBATION,
        PROTECTION
    }
    
    static class LRUNode<T> {
        int _key = -1;
        T _value;
        int _conflict;	//在key出现冲突时的辅助hash值
        SEGMENT_ZONE _flag = SEGMENT_ZONE.PROBATION;	//用于标记在缓存中的位置
    }

    static class CountMinRow {

        private int countNum;
        private int[] _bits;

        public CountMinRow(int countNum) {
            this.countNum = countNum;
            _bits = new int[(countNum < 8 ? 8 : countNum) / 8];
        }

        public int get(int num) {
            int countIndex = num / 8;
            int countOffset = num % 8 * 4;
            return (_bits[countIndex] >> countOffset) & 0x7;
        }

        //增加计数
        void increment(int num) {
            int countIndex = num / 8;
            int countOffset = num % 8 * 4;

            int value = (_bits[countIndex] >> countOffset) & 0x7;

            if (value < 15) {
                _bits[countIndex] += (1 << countOffset);
            }
        }

        //保鲜算法，使计数值减半
        void reset() {
            for (int i = 0; i < _bits.length; i++) {
                _bits[i] = (_bits[i] >> 1) & 0x77777777;
            }
        }

    }
    
    static class CountMinSketch {
        private int countNum;
        private int _mask; // 掩码用于取低n位
        private int[] _seed; // 用于打散哈希值
        private CountMinRow[] _cmRows;
        static int COUNT_MIN_SKETCH_DEPTH = 4;

        public CountMinSketch(int countNum) {
            this.countNum = Integer.highestOneBit(countNum);
            this.countNum = Math.max(8, this.countNum);
            
            this._cmRows = new CountMinRow[this.countNum];
            for (int i = 0; i < 4; i++) {
                _cmRows[i] = new CountMinRow(this.countNum);
            }
            this._mask = countNum - 1;

            _seed = new int[COUNT_MIN_SKETCH_DEPTH];
            Random random = new Random();
            for (int i = 0; i < COUNT_MIN_SKETCH_DEPTH; i++) {
                _seed[i] = random.nextInt();
            }
        }

        //选择几个cmRow最小的一个作为有效值返回
        int getCountMin(int hash) {
            int min = 16, value = 0;
            for (int i = 0; i < _cmRows.length; i++) {
                value = _cmRows[i].get((hash ^ _seed[i]) & _mask);
                min = Math.min(value, min);
            }
            return min;
        }

        void Increment(int hash) {
            for (int i = 0; i < _cmRows.length; i++) {
                _cmRows[i].increment((hash ^ _seed[i]) & _mask);
            }
        }

    }

}
