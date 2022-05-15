# 淘汰算法/置换算法


# 算法简介

```
淘汰算法关键指标
    1. 命中率
    2. 使用场景, 对各种场景中命中率依然很高, 比如 突发热点缓存, 突发稀疏缓存
    3. 时间效率 O(1)
    4. 空间效率
    5. 实现复杂度

LRU: 淘汰最久没有使用算法
    优点: 
        1. 会随着缓存内容变化迅速变化, 场景适应性快
        2. 效率O(1)
        3. 实现简单
    缺点:
        1. 场景适应性快, 对于热点缓存命中率低
        2. 场景适应性快, 会存储一些不重要的缓存, 淘汰更重要的缓存

LRU-K: 通过增加访问次数, 来解决LRU缺点2的问题, 
    当访问次数达到K时, 才进入缓存池, 通常使用两个LRU实现

SLRU: 

LFU: 淘汰最不常用且最久没有使用的算法, 增加了使用频率, 使用频率相同时按照LRU
    解决LRU缺点1对于热点缓存命中率低的问题
    优点:
        1. 对热点数据命中率高
    缺点:
        1. 对于历史访问频率较高的缓存很难淘汰, 新鲜的缓存很难替换掉老的频率高的缓存
        2. 需要额外空间记录访问频率信息

W-LFU: 在缓存访问上增加访问窗口, 出窗口的缓存访问频率降低
    解决LFU缺点1很难淘汰高频历史缓存问题


CoutingBloomFilter: 计数布隆过滤器
    采用布隆过滤器降低降低计数的空间占用, LFU缺点2问题

CountMinSketch: 类似计数布隆过滤器
    功能相当于 CoutingBloomFilter + W-LFU
    采用 CoutingBloomFilter 解决计数空间问题
    采用计数次数达到阈值后, 整体计数减半, 来解决历史高频数据淘汰问题

TinyLFU: 使用CountMinSketch实现

```

# 算法详解

[参考](https://www.jianshu.com/p/18285ecffbfb)

```
FIFO, 随机淘汰, 
LRU, LFU, ARC, MRU, OPT, NRU(CLOCK算法)

FIFO, 随机淘汰 命中率全靠运气, 这里仅当参考
```

## LRU 最近最少使用算法, 

```
LRU

LRU-K
    只有访问次数是K的时候, 才会进入LRU
    org.song.algorithm.base._01datatype._03app.elimination.lru.LRU02_lruk
```

## LFU 最不经常使用算法, 最近最少访问

```
普通LFU
LFUDA, LFU-Aging
Window-LFU
Tiny-LFU
Window-Tiny-LFU
```


## ARC 自适应缓存替换算法

## MRU 最近最常使用算法, 和LRU相反

## OPT 最佳置换算法

```
淘汰的是最不可能使用的
```

## NRU(CLOCK算法) 最近未用算法

```
其所选择的被淘汰页面，将是以后永不使用的，或许是在最长(未来)时间内不再被访问的页面。采用最佳置换算法，通常可保证获得最低的缺页率。

https://blog.csdn.net/wanna_wsl/article/details/52105338?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.pc_relevant_default&utm_relevant_index=2
```