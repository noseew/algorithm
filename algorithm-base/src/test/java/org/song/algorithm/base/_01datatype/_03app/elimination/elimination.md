# 淘汰算法/置换算法

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
    org.song.algorithm.base._01datatype._03app.elimination.lru.LRUKTest02
```

## LFU 最不经常使用算法, 最近最少访问

```
普通LFU
LFUDA, LFU-Aging
Window-LFU
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