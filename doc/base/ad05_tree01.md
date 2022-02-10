# tree 数据结构

# 树

```
树的术语概念

节点: 节点包含数据元素及若干指向子树的分支信息. 
节点的度: 节点拥有的子树个数. 
树的度: 树中节点的最大度数. 
终端节点: 度为0的节点，又称为叶子. 
分支节点: 度大于0的节点. 除了叶子都是分支节点. 
内部节点: 除了树根和叶子都是内部节点. 
```

## 二叉树

### BST二叉搜索树

### AVL自平衡二叉树

### B树

#### 基础

```
B树 (平衡树, 包括2叉树和多叉树)
    多路平衡搜索树, 又称为B-树, 或者B树. 一棵m阶B-树, 或为空树, 或满足以下特性. 
    1. 每个节点最多有m棵子树. 
    2. 根节点至少有两棵子树. 
    3. 内部节点(除根和叶子之外的节点)至少有floor(m/2)棵子树. 
    4. 终端节点(叶子)在同一层上, 并且不带信息(空指针), 通常称为失败节点. 
    5. 非终端节点的关键字个数比子树个数少1. 
    也就是说, 根节点至少有一个关键字和两棵子树, 其他非终端节点关键字个数范围为[floor(m/2)-1, m-1], 子树个数范围为[floor(m/2), m]. 

说明:
    1. B-树具有平衡/有序/多路的特点. 在B-树中, 所有的叶子都在最后一层, 因此左右子树的高差为0, 体现了平衡的特性. B-树具有中序有序的特性, 即左子树<根<右子树. 多路是指可以有多个分支, m阶B-树中的节点最多可以有m个分支, 所以称为m路平衡搜索树. 
    2. 层和节点的关系: h+1层至少有2*floor(m/2)^(h-1)个节点
    3. B树所有叶子到根节点的高度都相同
    4. 节点可以存储多个key, 多个key可以由链表存储, 也可以有数组存储
    5, 新添加的元素必须添加到叶子结点
    6. 应用, 多用于文件索引, 多大成百上千阶

阶数m: m阶B树
    根节点中key的数量x, 1 <= x <= m-1
    非根节点中key的数量x, floor(m/2)-1 <= x <= m-1
    页子节点数量y, y = x+1
    以2-3树为例, 非叶子结点和其子节点的关系, 
        要么 1个父节点(1个key)带2个子节点
        要么 1个父节点(2个key)带3个子节点
        
    m=2, 每个节点最多1个值, 最多2个子节点, 也就是普通二叉树
    m=3, 每个节点最多2个值, 最多3个子节点, 就是23树
    m=4, 每个节点最多3个值, 最多4个子节点, 就是234树, 简称24树

性能分析
    1. 单个节点内最多有m-1个key, 搜索到这些key, 可以通过遍历或者二分法, 由于都是内存操作, 所以通常复杂度忽略不计
    2. 复杂度仅仅与树的高度和阶数有关, O(log_mN), 当m=2(2阶), 复杂度=O(logN)
    3. 插入复杂度: O(log_mN)

调整
    1. 上溢
        当节点key>m-1, 则需要分裂节点并将某个key上溢, 直到满足 key<=m-1
        1) 上溢到根节点如果还需要上溢, 则树的整体高度会增高, 且只有这种方式能让B树增高
    2. 下溢
        非叶子结点子节点<=x, 则需要将父节点分裂并下溢, 直到满足  y = x+1
        1) 如果父节点直到根节点都不够下溢, 则会出现父子节点合并, 从而B树的高度降低, 且只有这种方式能让B树降低
    3. 上溢或者下溢过程中, 为了满足B的有序性, 可能会出现(当前节点/父节点/兄弟节点)相互交换的情况, 这其实就等价于二叉树的旋转

```

#### 添加节点-上溢

```
如果节点数量到达临界值, 就会发生上溢, 可能会增高树的高度, 而并不会平衡所有子节点来降低(不增高)高度
```

节点的 key>=3 需要上溢

![image-20220206183425510](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-add02.png)

如果上溢后, 父节点 key>=3, 则也会触发父节点上溢

![image-20220206183456845](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-add03.png)

![image-20220206183714972](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-add04.png)

上溢可能会触发连锁反应, 一直上溢到根节点, 而根节点也可能会触发上溢, 从而增高B树的高度

![image-20220206183745648](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-add05.png)

![image-20220206183816582](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-add06.png)

![image-20220206183922189](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-add07.png)



#### 删除节点-下溢

```

```



![image-20220206184134043](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del01.png)

删除节点80, 叶子节点数=父节点key数, 不满足B树规定, 发生下溢

过程相当于右旋

![image-20220206184208829](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del-02.png)

![image-20220206184234262](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del-03.png)

删除节点25, 叶子节点数=父节点key数, 不满足B树规定, 发生下溢

过程相当于左旋

![image-20220206184257700](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del-04.png)

![image-20220206184323412](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del-05.png)

下溢并合并?

![image-20220206184350370](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del-06.png)

![image-20220206184418135](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del-07.png)

下溢并合并

![image-20220206184514404](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del-08.png)

![image-20220206184537600](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del-09.png)

![image-20220206184602614](D:\workspace\project\idea-git\song\algorithm\doc\img\Btree-23-del-10.png)

### 红黑树