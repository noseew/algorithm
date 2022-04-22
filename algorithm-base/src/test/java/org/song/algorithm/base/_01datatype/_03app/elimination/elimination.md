# 淘汰算法

[参考](https://www.jianshu.com/p/18285ecffbfb)

```
FIFO, 随机淘汰, 
LRU, LFU, ARC, MRU, OPT, NRU(CLOCK算法)

FIFO, 随机淘汰 命中率全靠运气, 这里仅当参考
```

## LRU 最近最少使用算法

```
LRU
LRU-K

```


## ARC 自适应缓存替换算法

## MRU 最近最常使用算法, 和LRU相反

## OPT 最佳置换算法

## NRU(CLOCK算法) 最近未用算法

```
其所选择的被淘汰页面，将是以后永不使用的，或许是在最长(未来)时间内不再被访问的页面。采用最佳置换算法，通常可保证获得最低的缺页率。

https://blog.csdn.net/wanna_wsl/article/details/52105338?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.pc_relevant_default&utm_relevant_index=2
```

## LFU 最不经常使用算法

```

    https://blog.csdn.net/weixin_38569499/article/details/113768134
    特点
    3.1 优点
    一般情况下, LFU效率要优于LRU, 能够避免周期性或者偶发性的操作导致缓存命中率下降的问题
    3.2 缺点
    复杂度较高: 需要额外维护一个队列或双向链表, 复杂度较高
    对新缓存不友好: 新加入的缓存容易被清理掉, 即使可能会被经常访问
    缓存污染: 一旦缓存的访问模式发生变化, 访问记录的历史存量, 会导致缓存污染. 
    内存开销: 需要对每一项缓存数据维护一个访问次数, 内存成本较大. 
    处理器开销: 需要对访问次数排序, 会增加一定的处理器开销
    
    
    https://blog.csdn.net/weixin_38569499/article/details/113771551
    LFUDA(LFU with dynamic aging), 是LFU算法对于缓存污染问题的优化算法. 它为缓存引用次数增加了"时间因子"的概念, 用来适应缓存使用场景的变化. 
    LFUDA并没有真的为每次引用记录一个时间, 这样开销太大的. LFUDA是通过引入了引用次数的"老化"机制来引入"时间因子"的. LFU会动态持有一个平均引用次数, 数值是当前所有缓存引用次数的平均值. 当平均引用次数超过了一个预定义的门限时, 会给每个引用减少引用次数. 为每个应用减少访问次数的策略是灵活的, 可扩展的, 可以使用减少固定值, 减少百分比等各种合适的策略. 
    参考论文:  A Web Proxy Cache Coherency and Replacement Approach 
    优缺点
    3.1 优点
    和LFU算法相比, LFU-Aging一定程度上解决了缓存污染的问题. 当访问缓存的场景发生变化时, LFU-Aging可以更快适应, 缓存的命中率更高
    3.2 缺点
    复杂度: LFU-Aging在LFU的基础上增加平均引用次数判断和处理, 实现上更复杂. 
    处理器开销: 平均引用次数达到门限时, 需要遍历队列或链表, 执行引用次数老化策略, 这会带来一定的开销
    
    
    https://blog.csdn.net/weixin_38569499/article/details/113268370
    window-LFU算法描述
    Window-LFU被用来一定程度上解决LFU上述两步不可避免的问题. 
    Window是用来描述算法保存的历史请求数量的窗口大小的. Window-LFU并不记录所有数据的访问历史, 而只是记录过去一段时间内的访问历史. 即当请求次数达到或超过window的大小后, 每次有一条新的请求到来, 都会清理掉最旧的一条请求, 以保证算法记录的请求次数不超过window的大小. 
    Window-LFU的优缺点
    3.1 优点
        由于Window-LFU不存储全部历史数据, 所以其额外内存开销要明显低于LFU算法, 同时由于数据量明显减少, Window-LFU排序的处理器成本也要低于LFU. 
        另外, 由于早于前window次的缓存访问记录会被清理掉, 所以Window-LFU也可以避免缓存污染的问题, 因为过早时间访问的缓存已经被清理掉了. 
        在缓存命中率方面, Window-LFU一般会由于LFU, 因为Window-LFU一定程度上解决了缓存污染的问题, 缓存的有效性更高了. 缓存污染问题越严重的场景, Window-LFU的命中率就比LFU高的越多. 
       另外, 和LFU-Aging相比, Window-LFU对缓存污染问题的解决更彻底一些, 所以在缓存使用场景产生改变时, 命中率会优于LFU-Aging. 
    3.2 缺点
        但是Window-LFU需要维护一个队列去记录历史的访问流, 复杂度略高于LFU. 
```