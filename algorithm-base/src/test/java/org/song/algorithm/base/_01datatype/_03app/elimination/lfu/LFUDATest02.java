package org.song.algorithm.base._01datatype._03app.elimination.lfu;

public class LFUDATest02 {
    /*
    
    
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
    
     */
}
