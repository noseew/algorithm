package org.song.algorithm.base._01datatype._03app.elimination.lfu;

/*
tiny
tiny 就是解决缓存计数需要额外空间的问题, 
缓存计数, 缓存计数对象可以可缓存node对象放在一起, 但是仍然需要额外的字段占用空间, 尤其是缓存数量庞大的时候, 和内存空间有限的时候
    如果效率 O(1) 至少需要一个 key-val 方式
    如果效率 O(n) 或者 O(logn) 则需要 val 方式, 有序或半有序链表
这里的key或者val, 一个对应缓存key, 一个对应次数int, 具体看实现方式

tiny就是采用位图的方式存储key-val, 使用 CountingBloomFilter, 计数空间大大降低了


 */
public class LFU04_TinyLFU {
    
    /*
    论文《TinyLFU: A Highly Ecient Cache Admission Policy》阅读笔记
    
    https://segmentfault.com/a/1190000016091569
    
    
     */
}
