package org.song.algorithm.base._01datatype._03app.elimination.lfu;

public class WLFUTest03 {
    /*
    https://blog.csdn.net/yunhua_lee/article/details/7648549
    Windows-LFU是LFU的一个改进版，差别在于Window-LFU并不记录所有数据的访问历史，而只是记录过去一段时间内的访问历史，这就是Window的由来，基于这个原因，传统的LFU又被称为“Perfect-LFU”。
    
    
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
     */
}
