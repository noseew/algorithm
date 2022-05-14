package org.song.algorithm.base._01datatype._02high.bloom;

public class CountMinSketch {
    /*
    
    Count-min Sketch 是一个概率数据结构，用作数据流中事件的频率表。
    它使用散列函数将事件映射到频率，但与散列表不同，散列表仅使用子线性空间，但会因过多计算冲突导致的某些事件。

    Count-min Sketch 本质上与 Fan 等人在 1998 年引入的计数 Bloom filter 相同的数据结构. 
    但是，它们的使用方式各不相同，因此尺寸也有所不同：
    计数最小草图通常具有次线性单元数，与草图的所需近似质量有关，而计数 Bloom filter 的大小通常与其中的元素数集合。
     */
}
