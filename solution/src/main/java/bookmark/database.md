
事务日志
binlog（追加写） 用于故障恢复、主从同步
redo log（循环写）一种wal的实现(随机写通过顺序写来缓解)，解决 db把数据加载到内存，又不用频繁写磁盘的io问题

https://blog.csdn.net/weixin_43213517/article/details/117457184
innodb是以页为单位来管理存储空间的，任何的增删改差操作最终都会操作完整的一个页，
会将整个页加载到buffer pool中，然后对需要修改的记录进行修改，
修改完毕不会立即刷新到磁盘，因为此时的刷新是一个随机io，而且仅仅修改了一条记录，
刷新一个完整的数据页的话过于浪费了。
但是如果不立即刷新的话，数据此时还在内存中，如果此时发生系统崩溃最终数据会丢失的，
因此权衡利弊，引入了redo log，也就是说，修改完后，不立即刷新，而是记录一条日志，
日志内容就是记录哪个页面，多少偏移量，什么数据发生了什么变更。
这样即使系统崩溃，再恢复后，也可以根据redo日志进行数据恢复。
另外，redo log是循环写入固定的文件，是顺序写入磁盘的。

更新的一条数据->Innodb加载页到buffer pool
            ->记录undo log
            ->执行器->更新数据
                   ->写入redo log buffer
                        ->redo log buffer写入redo log文件 刷盘时机：1.commit时刷盘 2.commit刷到内核buffer但未到盘 3.定时刷新
                   ->写入bin log
写redo log和binlog不是原子的，存在故障后数据不一致，引入二阶段提交：
问题：先写redolog后写binlog，主从同步失败，数据不一致
    先写binlog再写redolog，主从同步完成，主库没有这条数据，数据不一致
两阶段提交用于故障恢复：
    写redo log prepare
                        ->此时断电，恢复后查看有没有binlog，有提交，无删除prepare的redolog
    写bin log
                        ->此时断电，恢复后查prepare的redo log和binlog都有提交事务
    写redo log commit

主从同步：
1. 3个线程：slave有2个线程,1个是I/O线程，负责从master读取binlog到slave的relay log中
                        第2个是SQL线程，负责读取并执行中继日志中的事务
           master有1个I/O线程：负责发送binlog
2. 异步同步：默认异步，意味着谈到数据一致性时，主从是独立的，异步可以提供最佳性能
   半同步复制：master进行事务处理前，必须确认更新已经收到并写入了slave的relay log；如果超时，转为异步
            原理：提交事务的线程被锁定，直到至少一个slave接收到这个事务
            上述master的I/O线程还负责接受ack信号
   旧半同步方式：master先commit，再等到slave ack，此时宕机恢复的问题：1.slave已同步，会再次收到binlog 2.slave未同步，若原master作为slave重启，会被再执行一次
   新半同步方式：只有slave的事务ack后master才commit，问题：master等待slave时挂，master数据比slave少

锁与线程安全：
并发场景：
读-写：脏读、幻读、不可重复读 （MVCC解决的问题）
写-写：更新丢失 (S\X锁)

缓存延迟双删
    先更新 db，再删缓存，隔段时间再删缓存
    目的:一切应以 db 数据为准，如果先删缓存再更新 db，那么删缓存->更新 db这段时间数据依然为脏数据
    为什么隔段时间再删缓存，为了避免删缓存的进程失败导致缓存删失败

事务
索引
分库分表 雪花算法

### 数据库

#### B树和B+树

**B树**

1. 相较于AVL或者红黑树，每个节点含有多个值，比如1-100，可以有4个区间 1-25，..，每个节点内一次性排除75%的数据
2. B树的每个节点包含key和data数据
3. 因为数据库的查询需要把数据从磁盘加载到内存，IO耗时较多，所以每次加载尽可能加载更多的数据，减少IO频率；每次加载一页数据
4. 存储更多的数据后，树的深度更低，IO和查询次数就更少
5. 查询效率不同，可能O(1)也可能O(logn)

**B+树**

1. B+树的每个非叶节点只保存key数据，data数据都在叶子节点上，所有的key也都在叶子节点
2. 每个非叶节点不保存数据后，就可以存储更多的key，每一页可以加载更多的key用来查找，树的结构更加矮胖，IO次数再次减少
3. 叶子节点有双向链表链接，方便范围查询
4. 查询效率较为稳定，都是O（logn）

#### 索引

**主键索引**

主键索引构建的B+树中，叶子节点保存了主键和全部数据，因此用主键索引可以直接获取全部数据

**普通索引、联合索引**

普通索引、联合索引构建的B+树中，叶子节点保存了该索引对应的数据和主键ID

**索引覆盖、回表**
如果构建的索引能够覆盖select的所有列，即索引覆盖，此时不需要回表操作
eg:

- 普通索引 a,对应sql：select a from table where a=xx,此时无需回表
- 普通索引a,对应sql：select a,b from table where a=xx,此时先通过索引a找到主键id，再回表通过主键索引找到全部数据，拿到b
- 联合索引(a,b,c) 实际创建了(a)(a,b)(a,b,c)三个索引，对应sql：select a,b,c from table where a=XX and b=xx and c=xx，此时走(a,b,c)索引，且能覆盖select的列，此时不需要回表
- 联合索引(a,b,c)对应sql：select a,b from table where a=XX and b=xx 此时走(a,b,c)联合索引中的(a,b)，且能覆盖select的列，此时不需要回表
- 联合索引(a,b,c)对应sql：select a,c from table where a=XX and c=xx 此时走(a,b,c)联合索引中的(a)，但是不能覆盖select中的c列，此时需要回表

**索引下推**

下推其实就是指将部分上层（服务层）负责的事情，交给了下层（引擎层）去处理
判断WHERE条件部分能否用索引中的列来做检查

#### 深分页问题及优化

``SELECT a,b FROM table WHERE a>=xx LIMIT 10000,10（a不是主键）``

**问题：** MySQL 查询分页 OFFSET 越深入，性能越差
**原因：** 大量的回表查询带来的随机IO耗费性能，范围查询了10010条后，舍弃前10000条
**优化：**
1. 子查询优化
   思路：子查询通过二级索引查出主键id，外层查询根据主键id查询，无需回表
   ``select id,a,b FROM table where id >= (select a.id from table a where a.a >= xxx limit 100000, 1) LIMIT 10``
2. 延迟关联
   子查询查出主键id，与外层查询用主键join
3. 游标/书签记录
   本地记录/前端带入每次查询后的最大主键id值，下次用主键id查询
   注意：翻页时用户不能跳页翻，只能按顺序翻页


#### 并发事务控制


##### 1. 脏读：
事务B读到了事务A未提交的值v，此时A事务回滚，B读的数据有误

| 时序 | 事务B | 事务A |
| --- | --- | --- |
| 1 |  | 开始事务 |
|  2| 开始事务 |  |
|  3|  | 设置x=2000|
| 4|读取x=2000 ||
| 5| | 回滚 x=1000|
| 6|提交 x=2000(实际是1000) ||

##### 2. 不可重复读
事务B读取x值，期间事务A修改并提交了x值，B再读时x值发生变化

| 时序 | 事务B | 事务A |
| --- | --- | --- |
| 1 |开始事务 |  |
| 2|第一次查询x=1000 | |
| 3|  |开始事务 |
| 4| | 设置x=2000提交事务|
| 5|第二次查询x=2000（前后不一致） ||

##### 3. 幻读
事务B读取>x值10条，此时事务A不能修改x值，但是插入了>x的数据2条，此时事务B的数据变成了12条，数量不一样
| 时序 | 事务B | 事务A |
| --- | --- | --- |
| 1 |开始事务 | |
| 2|第一次查询100条数据 | |
| 3| |开始事务 |
| 4| | 新增100条并提交|
| 5|第二次查询200条数据（前后不一致） ||

##### 区别

(1) 不可重复读是读取了其他事务更改的数据，针对**update** 操作
解决：使用行级锁，锁定该行，事务A多次读取操作完成后才释放该锁，这个时候才允许其他事务更改刚才的数据。
(2) 幻读是读取了其他事务新增的数据，针对**insert**和**delete**操作
解决：使用表级锁，锁定整张表，事务A多次读取数据总量之后才释放该锁，这个时候才允许其他事务新增数据。

#### 数据库隔离级别
1. Read uncommitted 读未提交 : 事务B可以读到事务A未提交的数据，造成**脏读**
2. Read committed (RC) 读提交：事务B只能读事务A提交的数据，避免脏读，会有**不可重复读**的问题 （Sql Server、Oracle的默认隔离级别）
3. Repeatable read (RR)(类似行锁) 重复读：事务B读取某条数据时，其他事务不能修改这条数据，避免不可重复读，会有**幻读**的问题 (MySql的默认隔离级别)
4. Serializable 序列化（串行化）(类似表锁)


#### 悲观锁和乐观锁
##### 悲观锁
悲观锁的实现，往往依靠数据库提供的锁机制（也只有数据库层提供的锁机制才能真正保证数据访问的排他性，否则，即使在本系统中实现了加锁机 制，也无法保证外部系统不会修改数据）
##### 乐观锁
大多是基于数据版本（ Version ）记录机制实现。即为数据增加一个版本标识，在基于数据库表的版本解决方案中，一般是通过为数据库表增加一个 “version” 字段来实现。读取出数据时，将此版本号一同读出，之后更新时，对此版本号加一。此时，将提交数据的版本数据与数据库表对应记录的当前版本信息进行比对，如 果提交的数据版本号大于数据库表当前版本号，则予以更新，否则认为是过期数据。

#### MVCC 多版本并发控制
 MySql的默认隔离级别是RR，是通过MVCC而不是行锁来解决脏读和不可重复读问题
 
 **隐藏字段：** 事务版本号trx_id和回滚指针roll_pointer（指向undo log）
 **快照读：** 读取的是记录数据的可见版本（有旧版本）。不加锁的select语句都是快照读：
 `select * form table where id>2;`
 **当前读：** 读取的是记录数据的最新版本，显示加锁的都是当前读：
 `select  * from table where id>2 for update`
 `select * from table where id>2 lock in share mode`
**Read View：** 事务执行SQL时，产生的读视图，在innodb中，每个SQL执行前都会得到一个Read View。主要用来做可见性判断，即判断事务属于哪个版本


Read View是如何保证可见性判断的呢？我们先看看Read view 的几个重要属性
- m_ids:当前系统中那些活跃(未提交)的读写事务ID, 它数据结构为一个List。
- min_limit_id:表示在生成ReadView时，当前系统中活跃的读写事务中最小的事务id，即m_ids中的最小值。
- max_limit_id:表示生成ReadView时，系统中应该分配给下一个事务的id值。
- creator_trx_id: 创建当前read view的事务ID
Read view 匹配条件规则如下：
1. 如果数据事务ID **trx_id < min_limit_id**，表明生成该版本的事务在生成Read View前，已经提交(因为事务ID是递增的)，所以该版本可以被当前事务访问。
2. 如果**trx_id>= max_limit_id**，表明生成该版本的事务在生成ReadView后才生成，所以该版本不可以被当前事务访问。
3. 如果 **min_limit_id =<trx_id< max_limit_id**,需腰分3种情况讨论
>- （1）.如果m_ids包含trx_id,则代表Read View生成时刻，这个事务还未提交，但是如果数据的trx_id等于creator_trx_id的话，表明数据是自己生成的，因此是可见的。
>- （2）如果m_ids包含trx_id，并且trx_id不等于creator_trx_id，则Read   View生成时，事务未提交，并且不是自己生产的，所以当前事务也是看不见的；
>- （3）.如果m_ids不包含trx_id，则说明你这个事务在Read View生成之前就已经提交了，修改的结果，当前事务是能看见的。

[MVCC](%3Ca href="https://juejin.cn/post/7016165148020703246"%3E看一遍就理解：MVCC原理详解 - 掘金 %28juejin.cn%29%3C/a%3E)
[MVCC2](%3Ca href="https://zhuanlan.zhihu.com/p/52977862"%3E数据库基础（四）Innodb MVCC实现原理 - 知乎 %28zhihu.com%29%3C/a%3E)
#### MVCC实现原理分析
##### 查询一条记录，基于MVCC，是怎样的流程
1. 获取事务自己的版本号，即事务ID
2. 获取Read View
3. 查询得到的数据，然后Read View中的事务版本号进行比较。
4. 如果不符合Read View的可见性规则， 即就需要Undo log中历史快照;
5. 最后返回符合规则的数据
InnoDB 实现MVCC，是通过 Read View+ Undo Log 实现的，Undo Log 保存了历史快照，Read View可见性规则帮助判断当前版本的数据是否可见。

## NewSQL：TiDB、OceanBase

背景：MySql单机在500w数据量时会有性能瓶颈，大多通过分库分表、读写分离方式解决：
1. 在MyBatis或JPA之上使用AOP或者拦截器，指定分区查询
2. 在JDBC驱动层实现，分库分表路由维护在内存，重写DataSource、Connection、Statment、Resultset实现
3. 利用中间件Proxy模式，ShardingSphere

分布式数据库从设计根源解决：**TiDB**、**Oceanbase** （LSMTree）
**AWS Aurora**原理是数据层共享，但是本质还是单机 

## 索引结构LSMTree(Log-Structured-Merge-Tree)

 https://zhuanlan.zhihu.com/p/181498475

### 背景
NoSQL存储层引擎的索引结构，HBase、Cassandre、Rocksdb等均用到了LSM-Tree
分布式NewSQL（TiDB）、GraphDB（Nebula）的存储层用到了rocksdb
ClickHouse中的MergeTree也是LSM树的思想
>Rocks是一个高性能的多线程版本的kv storage Engine，提供了open/put/get/writebatch/iterator之类的kv操作的原语，高级特性非常丰富，比如mvcc，snapshot，ingest，range delete之类。总起来说，rocks是lsm-tree的cpp语言的优秀实现。
>
>哪些数据库用了rocks呢，或者说什么场景下用rocks比价好呢？老的单机关系库还是b+tree为主，用rocks不多，虽然也出现过myrocks这种还不算主流。
>nosql领域用lsm-tree的非常多，但是用rocks不太多，因为很多nosql（hbase cassandra）是非c/cpp实现的，很早就自己实现了一套。
>分布式的newsql（tidb ，yugabyte，cockroach初期也是rocks后面切成了go-pebbles）和分布式graph db都非常喜欢用rocks。分布式强一致的db，实现multi-raft是标配。rocks的mvcc方式的snapshot对于raft非常合适（这点b+树也可以）。但是做分片调度的场景，需要批量的删除和插入大量的数据，lsmtree做range delete和bulk ingest就非常友好了，b+树就不行了。因此，分布式强一致，有事务需求的db，multi-raft+lsmtree（rocks）是标配了。
>对于ap的场景，由于列存的原因，存储多是lsm-tree变体，倒是鲜有用rocks的。
>分布式存储sds领域也有很多项目喜欢用rocks作为索引。例如ceph nutanix等。传统的磁盘文件系统，在海量小文件场景下，写放大和读放大都比较严重。在对象和block这两种语义下，用一个统一的索引树（b树）做为object/block的index，比传统的磁盘文件系统要好很多。B+树对于nandflash很不友好，append only的lsmtree的rocks自然是首选了。
>Rocks的最大问题是write penalty太高了，持续性高强度的写入，一旦有l0的sst 有积压，马上write stall，throughput狂掉。background的 compaction对于性能稳定性也有很大的影响。
>这个问题大概有两个思路：
- 一个是kv分离的方式wisckey，核心思想是大的value不进入lsm-tree的compact。例如rocksdb的一个分支blobdb，tidb的titan terakdb之类都是rocks的分支。
- pebbles db也是对lsm的一个改进，核心思想是对lsm-tree做分片，分片之后的结构有点像skip-list，从设计原理和论文实验效果上看看是不错的。工业界也已经有人开始尝鲜了，cockroach做了个go版本的pebbles。
>
>rocks跟multi-raft的结合使用，其实还有一个额外的写放大问题。rocks有自己的wal-log，raft也有自己的raftlog。直接用rocksdb作为raft的state machine，会导致一份数据写了3次(一次raftlog，一次rocks-wal-log，一次rocks的lsm-tree的sst table)。在nandflash上，这种write amp显然太多了。

### 核心思想

https://zhuanlan.zhihu.com/p/181498475
利用顺序写来提高写性能，但因为分层(此处分层是指的分为内存和文件两部分)的设计会稍微降低读性能，但是通过牺牲小部分读性能换来高性能写

### 组成部分
1. MemTable
MemTable是在内存中的数据结构，用于保存最近更新的数据，会按照Key有序地组织这些数据，LSM树对于具体如何组织有序地组织数据并没有明确的数据结构定义，例如Hbase使跳跃表来保证内存中key的有序。

    因为数据暂时保存在内存中，内存并不是可靠存储，如果断电会丢失数据，因此通常会通过WAL(Write-ahead logging，预写式日志)的方式来保证数据的可靠性。（HBase中的HLog）

2. Immutable MemTable

    当 MemTable达到一定大小后，会转化成Immutable MemTable。Immutable MemTable是将转MemTable变为SSTable的一种中间状态。写操作由新的MemTable处理，在转存过程中不阻塞数据更新操作。

3. SSTbale（Sorted String Table）

    有序键值对集合，是LSM树组在磁盘中的数据结构。为了加快SSTable的读取，可以通过建立key的索引以及布隆过滤器来加快key的查找。

| Index表 |  |
| --- | --- |
| key | offset |
| key | offset |
| ... | ... |

| SSTableFile |  |  |  |
| --- | --- | --- | --- |
|key  | value | key | value |


LSM树会将所有的数据插入、修改、删除等操作记录(注意是操作记录)保存在内存之中，当此类操作达到一定的数据量后，再批量地顺序写入到磁盘当中。

这与B+树不同，B+树数据的更新会直接在原数据所在处修改对应的值，但是LSM数的数据更新是日志式的，当一条数据更新是直接append一条更新记录完成的。

这样设计的目的就是为了顺序写，不断地将Immutable MemTable flush到持久化存储即可，而不用去修改之前的SSTable中的key，保证了顺序写。


因此当MemTable达到一定大小flush到持久化存储变成SSTable后，在不同的SSTable中，可能存在相同Key的记录，当然最新的那条记录才是准确的。这样设计的虽然大大提高了写性能，但同时也会带来一些问题：
1. 冗余存储，对于某个key，实际上除了最新的那条记录外，其他的记录都是冗余无用的，但是仍然占用了存储空间。因此需要进行Compact操作(合并多个SSTable)来清除冗余的记录。
2. 读取时需要从最新的倒着查询，直到找到某个key的记录。最坏情况需要查询完所有的SSTable，这里可以通过前面提到的**索引/布隆过滤器**来优化查找速度。（最常见的方法是在内存建立页索引，将sstable按照LRU缓存在内存中）

### Compact策略
#### 概念

**1. 读放大:** 读取数据时实际读取的数据量大于真正的数据量。例如在LSM树中需要先在MemTable查看当前key是否存在，不存在继续从SSTable中寻找。
**2. 写放大:** 写入数据时实际写入的数据量大于真正的数据量。例如在LSM树中写入时可能触发Compact操作，导致实际写入的数据量远大于该key的数据量。
**3. 空间放大:** 数据实际占用的磁盘空间比数据的真正大小更多。上面提到的冗余存储，对于一个key来说，只有最新的那条记录是有效的，而之前的记录都是可以被清理回收的。

#### 策略
1. size-tiered策略
    1. size-tiered策略保证每层SSTable的大小相近，同时限制每一层SSTable的数量。每层限制SSTable为N，当每层SSTable达到N后，则触发Compact操作合并这些SSTable，并将合并后的结果写入到下一层成为一个更大的sstable。
    2. 当层数达到一定数量时，最底层的单个SSTable的大小会变得非常大。并且size-tiered策略会导致空间放大比较严重。即使对于同一层的SSTable，每个key的记录是可能存在多份的，只有当该层的SSTable执行compact操作才会消除这些key的冗余记录。
2. leveled策略
    1. leveled策略也是采用分层的思想，每一层限制总文件的大小。但是跟size-tiered策略不同的是，leveled会将每一层切分成多个大小相近的SSTable。这些SSTable是这一层是全局有序的，意味着一个key在每一层至多只有1条记录，不存在冗余记录。
    2. 具体流程：
        1. 若L1的总大小超过L1本身大小限制
        2. 此时会从L1中选择至少一个文件，然后把它跟L2有交集的部分(非常关键)进行合并。生成的文件会放在L2
        3. 如果L2合并后的结果仍旧超出L5的阈值大小，需要重复之前的操作 - 选至少一个文件然后把它合并到下一层；多个不相干的合并是可以并发进行的
     3. leveled策略相较于size-tiered策略来说，每层内key是不会重复的，即使是最坏的情况，除开最底层外，其余层都是重复key，按照相邻层大小比例为10来算，冗余占比也很小。因此空间放大问题得到缓解。但是写放大问题会更加突出。举一个最坏场景，如果LevelN层某个SSTable的key的范围跨度非常大，覆盖了LevelN+1层所有key的范围，那么进行Compact时将涉及LevelN+1层的全部数据。

## Shared Nothing/Shared Disk/Shared Storage
> https://zhuanlan.zhihu.com/p/32924680
> https://www.zhihu.com/question/63987114
### Shared Disk
- Oracle的RAC集群，**AWS Aurora** (mysql on ebs)、PolarDB
- 读写节点只有1个
- HBase：共享一个HDFS，跨节点复制通过HDFS实现

aurora多个存储节点之间共享的是redo日志，然后每个节点都独立地通过redo日志进行数据的复制，每个节点都有自己的数据库，gossip系统保证了任意时刻每个节点的redo日志是完全相同的，从数据库文件的角度上来看，其实每个节点之间存在差异的（当然，每个存储节点提供给计算节点的数据应该会保证是相同的吧）
aurora的存储层跟计算无法完全分离，cache实际上offload到了存储层，(存储节点具有将redolog转换为innodb page的能力)从这个角度上看，aurora与 mysql on ebs 主要的区别在于cache的一致性，page页如果只是从在文件系统层支持了同步，相当于vcl推进了cpl并没有推进。
### Shared Nothing
- MySQL分表分库+大部分NoSQL，Spanner为代表的TiDB （Ti Server层）
- 解决扩展性问题
- 数据复制和一致性：
  1. 最终一致性：简单主从复制
  2. 强一致性的数据复制协议：zookeeper、etcd轻量级分布式存储框架
### Shared Storage
- 目的为了存储计算分离：NoSQL一般不存在跨sharding计算，NewSQL、OLAP数仓要支持跨Sharding
- 多个无状态的计算节点，共享一个有状态的分布式存储引擎，这就是所谓的share storage。


## Amazon Aurora 和 阿里云 Polar DB-X

通过共享存储解决扩展问题，本质是可扩展存储的单机MySQL，具体搜两者架构区别
> 赛道玩家：https://developer.aliyun.com/article/720563
Aurora ：计算节点垂直扩展，存储节点可以水平扩展

## NewSQL

背景：MySql单机在500w数据量时会有性能瓶颈，大多通过分库分表、读写分离方式解决：
1. 在MyBatis或JPA之上使用AOP或者拦截器，指定分区查询
2. 在JDBC驱动层实现，分库分表路由维护在内存，重写DataSource、Connection、Statment、Resultset实现
3. 利用中间件Proxy模式，ShardingSphere

国产数据库，基于Google Spanner/F1 论文设计的开源分布式数据库
国外类似产品有：cockroachDB

分布式数据库从设计根源解决：**TiDB**、**Oceanbase** （LSMTree）
**AWS Aurora**原理是数据层共享，但是本质还是单机

### TiDB （PingCAP公司）（细节待补充）
> https://docs.pingcap.com/zh/tidb/stable/overview
##### 架构：
**1. PD Server（Placement Driver）**
1. 存储集群元信息（某个key存在哪个TiKV节点）
2. 对TiKV集群继续调度和负载均衡（数据迁移、raft group）
3. 分配全局唯一且递增的事务ID
**2. TiDB Server （请求处理层）**
1. 负责接收SQL请求，解析SQL语句
2. 与TiKV交互
**3. TiKV Server （行存储引擎层）**
1. 每个节点包含多个Region，存储再一个RocksDB实例上
2. 每个Region存储一个Key Range
3. Raft协议保证一致性和容灾，读写都leader负责
4. 副本以Region为单位，不同节点多个Region构成一个RaftGroup
5. 数据再多个TiKV之间负载均衡由PD调度，也是以Region为单位进行调度
**4. TiFlash 列存储引擎**
1. Raft协议，以Region为单位进行数据复制和分散
2. 低消耗不阻塞TiKV写入的方式，实时复制TiKV集群中的数据，并保证数据实时性与一致性
#### 特性

1. 一键水平扩容：计算、存储分别扩容
2. 金融级高可用
3. 实时HTAP：行、列存储引擎
4. 云原生
5. 兼容MySQL

#### 适用场景
1. 需要高可用、强一致性：金融行业
2. 海量数据且高并发的OLTP场景
3. 实时HTAP场景：同一系统内做联机交易处理、实时数据分析等
4. 数据汇聚、二次加工：比Hadoop简单
### OceanBase （细节待补充）
#### 架构：
**RootServer（一主一备）**

*      管理进群中所有服务器，子表数据分布以及副本管理

**UpdateServer（一主一备）**

* 存储OB系统的增量更新数据

**ChunkServer**

* 存储OB的基线数据

**MergeServer**

* SQL引擎层，解析器、优化器、执行器
  1. Paxos协议
  2. 存储层：
  1. 基线数据、增量数据
  2. LSMTree索引：增量保存在MemTable，基线保存在SSTable，定期刷盘保存成SSTable，每晚空闲时与基线SSTbale合并
  3. 多级缓存

# NoSql
## KV数据库

* Redis
* LevelDB（Google）
* RocksDB （Facebook）
* TiKV （TiDB数据存储层，用的RocksDB）

## 列族数据库（面向列的KV数据库）

简单来说，keyspace：{col1,col2,col3}，每种类型的数据的列组成列族
rowKey1:{col1,col2}
rowKey2:{col2,col3}

        ID Last First Bonus
        1 Doe John 8000
        2 Smith Jane 4000
        3 Beck Sam 1000
- 在面向行的数据库管理系统中，数据将像这样存储：
  1,Doe,John,8000;2,Smith,Jane,4000;3,Beck,Sam,1000;
- 在面向列的数据库管理系统中，数据将像这样存储:
  1,2,3;Doe,Smith,Beck;John,Jane,Sam;8000,4000,1000;


### Cassandra (AWS DynamoDB) （架构细节待补充)
#### 概念
Cassandra 是一个分区的行存储。行被组织成具有所需主键的表。

分区意味着 Cassandra 可以在应用程序透明的情况下将您的数据分布在多台机器上。Cassandra 将在集群中添加和删除机器时自动重新分区。

行存储意味着像关系数据库一样，Cassandra 按行和列组织数据。

列族，但不是面向列，行分区存储
#### 特点
- dynamo风格分布式节点：
  去中心化，每个节点都可以写数据
- 单机效率高：bigtable LSM单机引擎
    1. 写入数据的时候先写入commit log来保持数据持久化，
    2. 然后写入Memtable（内存表），根据partition key和clustering key排序
    3. 在Memtable排序后写入SSTable（磁盘文件），SSTable包含全部的行数据
    4. 之后后台Compaction线程会合并多个SSTable
- 根据partition key路由到不同node，不是传统列式数据库，因为数据根据key hash存储在不同分片上，针对某列的聚合操作十分低效
    1. 查询时，根据partition key路由到不同node
    2. Memtable和SSTable中过滤partition key和其他条件
- SSTable中是压缩文件，需要解压，查询数据多时会OOM



### HBase

>https://zhuanlan.zhihu.com/p/145551967

HBase 是一个分布式的、多版本、面向列的开源 KV 数据库。运行在 HDFS 的基础上，支持 PB 级别、百万列的数据存储。

#### 数据模型
1. 在HBase里边，定位一行数据会有一个唯一的值，这个叫做行键(RowKey)
2. HBase的列（Column）都得归属到列族（Column Family）中。在HBase中用列修饰符（Column Qualifier）来标识每个列。
3. 在HBase里边，先有列族，后有列。
   {rowkey:1,orderinfo{orderid,money},userinfo{name,age}}
4. HBase表的每一行中，列的组成都是灵活的，行与行之间的列不需要相同
5. 修改和删除 都是新增一条数据，根据时间戳取最新

#### HBase的key-value
Key由RowKey(行键)+ColumnFamily（列族）+Column Qualifier（列修饰符）+TimeStamp（时间戳--版本）+KeyType（类型）组成，而Value就是实际上的值。

KeyType用来标记是否删除

#### HBase架构
**读写流程**
1. Client客户端，它提供了访问HBase的接口，并且维护了对应的cache来加速HBase的访问。
2. Zookeeper存储HBase的元数据的**索引**（meta表）,meta表还是在HRegion中（相当于二级索引），无论是读还是写数据，都是去Zookeeper里边拿到meta元数据告诉给客户端去哪台机器读写数据
3. HRegionServer它是处理客户端的读写请求，负责与HDFS底层交互，是真正干活的节点。

总结：client请求到Zookeeper，然后Zookeeper返回HRegionServer地址给client，client得到Zookeeper返回的地址去请求HRegionServer，HRegionServer读写数据后返回给client。

**写入流程**
1. 根据rowkey切分数据，横向切分
2. 每行数据定位到一个具体的HRegionServer，里面有多个HRegion，每个存储部分数据
3. 一个列族存在同一个HRegion的Store中
4. Store包含Mem Store、StoreFile、HFile
    - HBase在写数据的时候，会先写到Mem Store，当MemStore超过一定阈值，就会将内存中的数据刷写到硬盘上，形成StoreFile，而StoreFile底层是以HFile的格式保存，HFile是HBase中KeyValue数据的存储格式。
5. HLog：数据在写到内存mem store时也会同时写到HLog，用于故障恢复；顺序写入，一个regionserver有一个HLog
    - 当一个regionserver挂时，zk通知hmaster，hmaster处理问题regionserver上的hlog文件，根据region中的记录和hlog中的对应关系对hlog进行拆分，并把hlog放到相应的region目录下，region服务器领取到相应的region和hlog之后把hlog上的数据操作重新做一遍,然后memstore缓存,刷新到storefile就可以了。
      **HMaster**
      HMaster会处理 HRegion 的分配或转移。如果我们HRegion的数据量太大的话，HMaster会对拆分后的Region重新分配RegionServer。（如果发现失效的HRegion，也会将失效的HRegion分配到正常的HRegionServer中）HMaster会处理元数据的变更和监控RegionServer的状态

#### RowKey设计
1. 分区键，尽量散列
    1. 精确预分区
    2. 设置startkey-endkey
2. 范围查询：rowkey设计尽量在一个HRegion上

#### 总结
HBase本身是分布式的，读写请求根据rowkey分布到不同region上，但是数据本身没有副本，数据副本是HDFS做的，（不像其他Nebula数据副本在不同分片上）
regionserver故障靠zookeeper和hmaster来把hlog任务拆分到不同regionserver上

### ClickHouse（架构细节待补充)

类LSM Tree结构

**适用场景**

更适合实时分析和查询，HBase更适合海量数据存储和管理（实时读写）

1. 读多于写，读大量行少量列
2. 低频批量写入
3. 无事务性，数据一致性较低

#### 表引擎
##### TinyLog

最简单的表引擎，用于将数据存储在磁盘上，每列都存储在单独的压缩文件中，写入时，数据附加到文件末尾.

**缺点**：
（1）没有并发控制（没有做优化，同时写会数据会损坏，报错）
（2）不支持索引
（3）数据存储在磁盘上
**优点**：
（1）小表节省空间
（2）数据写入，只查询，不做增删改操

##### Memory

内存引擎，数据以未压缩的原始形式直接保存在内存中，服务器重启，数据会消失，读写操作不会相互阻塞，不支持索引。 建议上限1亿行的场景。
**优点**：简单查询下有非常高的性能表现（超过10G/s）

##### Merge

本身不存储数据，但可用于同时从任意多个其他的表中读取数据，读是自动并行的，不支持写入，读取时，那些真正被读取到数据的表的索引（如果有的话）会被占用,默认是本地表，不能跨机器。
参数：一个数据库名和一个用于匹配表名的正则表达式 创建表：
>create table t1(id Int8, name String)ENGINE=TinyLog
create table t2(id Int8, name String)ENGINE=TinyLog
create table t (id UInt16, name String)ENGINE=Merge(currentDatabase(), ‘^t’)

##### MergeTree

CK中最强大的表引擎MergeTree(合并树)和该系列（*MergeTree）中的其他引擎。

**使用场景**：有巨量数据要插入到表中，高效一批批写入数据片段，并希望这些数据片段在后台按照一定规则合并。相比在插入时不断修改（重写）数据进行存储，会高效很多。
**优点**：
（1）数据按主键排序
（2）可以使用分区（如果指定了主键）
（3）支持数据副本
（4）支持数据采样 创建表

**ReplacingMergeTree**

删除具有相同主键的重复项，数据的去重只会在合并的过程中出现，合并会在未知的时间在后台进行，不保证没有重复的数据出现

**SummingMergeTree** 汇总
**AggregatingMergeTree** 聚合
**CollapsingMergeTree** 折叠
**VersionedCollapsingMergeTree** 数据版本

##### Distributed

分布式引擎，本身不存储数据，但可以在多个服务器上进行分布式查询，读是自动并行的，读取时，远程服务器的索引（如果有的话）会被使用

#### 列式存储

每列数据存在一个block中

1. 读取时可以只选择需要的列
2. 同一列的数据类型相同，数据压缩效率高
3. 高压缩比，磁盘IO快，cache加载数据多

#### 主键索引有序存储

- 建表时如果未指定primary key，则以order by 的列时主键，在磁盘上连续存储、有序摆放

- 每列数据按照index granularity（8192行）进行划分，每个index granularity的第一行被称作mark行，主键索引存储该mark行对应的primary key 的值

- 查询时，对主键索引进行二分查找

- 所以 ClickHouse 根据主键生成的索引实际上稀疏索引，默认情况下是每隔 8192 行数据才生成一条索引

#### 数据存储结构

https://www.cnblogs.com/traditional/p/15218743.html


## 时序数据库
随时间不断产生的一系列数据，简单说，带时间戳的数据

非CRUD，不支持更新数据、删除单条数据操作

时序数据库相比较Clickhouse来说，写入速度更优
TSM 类似于LSM


### IoT数据的发展
1. 业务场景：海量设备持续产生运行时数据，数据存储量大、写入吞吐量要求高
2. 数据特征：
    - 按时间周期性产生
    - 数据结构相对固定
    - 写多读少，无更新，无事务
    - 数据访问按时间段访问
    - 热数据访问高
    - 存储量大
3. 存储要求：
   1. 高并发的写入吞吐
   2. 高效的查询分析：统计某时间段的均值、最大值等
   3. 低成本的数据存储
4. 发展阶段：
   1. 第一阶段：解决监控类业务需求，通常使用RDDTool、Graphite等，缺点：单机容量受限、内嵌监控告警解决方案，业务单一
   2. 第二阶段：大数据和Hadoop生态发展，基于分布式存储构建时序数据，HBase、Cassandra，解决存储能力能问题；缺点：本质是KV存储，检索、存储压缩效率都不高，对聚合处理支持较弱
   3. 第三阶段：容器、K8S、微服务发展，时序数据需求越来越高，专门的时序数据库influxdb来解决这些痛点：高效能、低成本，缺点：influxdb只开源了单机版本，高可用的集群模式需要自运维
   4. 第四阶段：云上时序数据库服务，阿里TSDB、Amazon TimeStream、Azure Timeseires Insight


### Influxdb

#### 概念
**度量 Metric/Measurement**：类似table，一系列同类时序数据的集合
**标签 Tag**：key-value结构，描述数据源特征，不随时间变化: 设备ID
**时间戳 Timestamp**：可以写入时指定，或系统自动生成
**量测值 Field**：描述数据源量测指标，随时间变化：设备温度等
**数据点 DataPoint**: 数据源在某个时间产生的某个量测指标值为一个数据点，查询与写入按数据点来作为统计指标
**时间线 Time Series**: 数据源的某一个指标随时间变化，形成时间线，Metric + Tags + Field 组合确定一条时间线；针对时序数据的计算包括降采样、聚合（sum、count、max、min等）、插值等都基于时间线维度进行；

#### 特性

随时间不断产生的一系列数据，简单说，带时间戳的数据
非CRUD，不支持更新数据、删除单条数据操作
- 特性：
  1. 多写少读，高并发写入
  2. 无更新：可利用时间戳（Timestamp）和时间序列线（Series）完全相同的时序数据记录，是同一条时序数据记录，新插入的时序数据，会覆盖原有的时序数据记录
  3. 不支持单条删除时序数据记录，
  1. 通过保留策略周期性定时删除
  2. 通过WHERE条件语句、删除时间序列线、删除表、删除数据库、删除分片（Shard）等方式直接批量删除指定的时序数据记录，不支持Field，只支持标签和时间戳
  4. 不支持join，可以连续查询
  5. 配合Telegraf监控、Grafana服务做可视化
  6. 高压缩比：数据压缩效果好，节省存储成本
  7. 分布式集群版闭源
#### 适用场景：
1. 专注于海量时序数据的高性能读、高性能写、高效存储与实时分析等，在DB-Engines Ranking时序型数据库排行榜上排名第一，广泛应用于DevOps监控、IoT监控、实时分析等场景
2. 适合无update、delete等OLTP场景
3. TICK生态：Telegraf、 InfluxDB、Chronograf、Kapacitor，采集、存储、分析、可视化等能力的开源时序中台：Prometheus+InfluxDB+Grafana
4. 相比OpenTSDB、MongoDB、Graphite、Cassandra等，InfluxDB的性能优势和成本优势明显。


#### TSDB存储引擎（Shard）

>https://datamining.blog.csdn.net/article/details/107688423


**Shard** 在 InfluxDB 中按照数据的时间戳所在的范围，会去创建不同的 shard，每一个 shard 都有自己的 cache、wal、tsm file 以及 compactor，这样做的目的就是为了可以通过时间来快速定位到要查询数据的相关资源，加速查询的过程，并且也让之后的批量删除数据的操作变得非常简单且高效

在 LSM Tree 中删除数据是通过给指定 key 插入一个删除标记的方式，数据并不立即删除，需要等之后对文件进行压缩合并时才会真正地将数据删除，所以删除大量数据在 LSM Tree 中是一个非常低效的操作。

而在 InfluxDB 中，通过 retention policy 设置数据的保留时间，当检测到一个 shard 中的数据过期后，只需要将这个 shard 的资源释放，相关文件删除即可，这样的做法使得删除过期数据变得非常高效

一个shard就是编码压缩后的数据真实存储的位置。这种数据被组织成了一个TSM的结构。并且每一个shard属于一个shard group。

**Retention policy** 描述数据保留多久，数据的副本数量以及shard group 的时间范围

**Shard group**是一个逻辑概念，和retention policy（保留策略）相关，决定一个shard group 有限期多长时间的叫做shard duration

#### TSM（Time-Structured Merge Tree）

TSM存储引擎主要包括四部分：**Cache，WAL，TSM File，Compactor**

1. **Cache**: cache 相当于是 LSM Tree 中的 memtable，在内存中是一个简单的 map 结构，这里的 key 为 seriesKey + 分隔符 + filedName，目前代码中的分隔符为 #!~#，entry 相当于是一个按照时间排序的存放实际值的数组
2. **Wal**: 日志文件，用于故障恢复
3. **TSM File**: 类似SSTable，用于存放数据
4. **Compactor**: 后台持续运行，每隔1s压缩合并数据
    1. cache数据达到阈值后，进行快照，转存到新tsm文件
    2. 合并当前tsm文件，将多个小的 tsm 文件合并成一个，使每一个文件尽量达到单个文件的最大大小，减少文件的数量，并且一些数据的删除操作也是在这个时候完成

**索引读取操作优化**：
> https://blog.csdn.net/xiaolei1982/article/details/75004852/?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0--blog-126382492.235^v40^pc_relevant_anti_vip&spm=1001.2101.3001.4242.1&utm_relevant_index=3


1. **元数据索引**：一个数据库的元数据索引通过 DatabaseIndex 这个结构体来存储，在数据库启动时，会进行初始化，从所有 shard 下的 tsm file 中加载 index 数据，获取其中所有 Measurement 以及 Series 的信息并缓存到内存中
    1. **元数据查询**:
       例如我们需要查询 cpu_usage 这个 measurement 上传数据的机器有哪些，一个可能的查询语句为：
       ``SHOW TAG VALUES FROM "cpu_usage" WITH KEY = "host"``
        1. 首先根据 measurement 可以在 DatabaseIndex.measurements 中拿到 cpu_usage 所对应的 Measurement 对象。
        2. 通过 Measurement.seriesByTagKeyValue 获取 tagk=host 所对应的以 tagv 为键的 map 对象。
        3. 遍历这个 map 对象，所有的 key 则为我们需要获取的数据。

    2. **普通数据查询**:
       对于普通的数据查询语句，则可以通过上述的元数据索引快速定位到要查询的数据所包含的所有 seriesKey，fieldName 和时间范围。
       举个例子，假设查询语句为获取 server01 这台机器上 cpu_usage 指标最近一小时的数据：`SELECT value FROM "cpu_usage" WHERE host='server01' AND time > now() - 1h`
        1. 先根据 measurement=cpu_usage 从 DatabaseIndex.measurements 中获取到 cpu_usage 对应的 Measurement 对象。
        2. 之后通过 DatabaseIndex.measurements["cpu_usage"].seriesByTagKeyValue["host"]["server01"] 获取到所有匹配的 series 的 ID值，再通过 Measurement.seriesByID 这个 map 对象根据 series ID 获取它们的实际对象。
        3. 注意这里虽然只指定了 host=server01，但不代表 cpu_usage 下只有这一个 series，可能还有其他的 tags 例如 user=1 以及 user=2，这样获取到的 series ID 实际上有两个，获取数据时需要获取所有 series 下的数据。
        4. 在 Series 结构体中的 shardIDs 这个 map 变量存放了哪些 shard 中存在这个 series 的数据。而 Measurement.fieldNames 这个 map 可以帮助过滤掉 fieldName 不存在的情况。
        5. 至此，我们在 o(1) 的时间复杂度内，获取到了所有符合要求的 series key、这些 series key 所存在的 shardID，要查询数据的时间范围，之后我们就可以创建数据迭代器从不同的 shard 中获取每一个 series key 在指定时间范围内的数据。后续的查询则和 tsm file 中的 Index 的在内存中的缓存相关
3. **TSM File 索引**:
    1. 对于 tsm file 中的 Index 部分会在内存中做间接索引，从而可以实现快速检索的目的。b 直接对应着 tsm file 中的 Index 部分，通过对 offsets 进行二分查找，可以获取到指定 key 的所有 block 的索引信息，之后根据 offset 和 size 信息可以取出一个指定的 block 中的所有数据。
    2. 通过元数据索引可以获取到所有 符合要求的 series key，它们对应的 shardID，时间范围。通过 tsm file 索引，我们就可以根据 series key 和 时间范围快速定位到数据在 tsm file 中的位置
    3. 从tsm file中读取数据：
        1. influxDB中所有数据读取操作都通过iterator
        2. Iterator 是一个抽象概念，并且支持嵌套，一个 Iterator 可以从底层的其他 Iterator 中获取数据并进行处理，之后再将结果传递给上层的 Iterator
        3. cursor 提供了一个 next() 方法用于获取一个 value 值。每一种数据类型都有一个自己的 cursor 实现。
        4. 底层实现都是 KeyCursor，KeyCursor 会缓存每个 Block 的数据，通过 Next() 函数依次返回，当一个 Block 中的内容读完后再通过 ReadBlock() 函数读取下一个 Block 中的内容。
#### 目录结构
influxdb->data->db->rp->shard->tsm引擎
->meta->meta.db
->wal->db->rp->shard->wal file


## 文档数据库 （细节待补充）
MongoDB
非结构化支持，KV存储，适合低成本缓存
存储成本高，复杂查询效率低，不适合OLTP、BI分析

## 图数据库
Neo4j
Nebula:
- Meta 管理Graph的链接（定时同步，更新时间戳用于链接复用、销毁过期链接）、Storage内的调度
- Grouph
- Storage(RocksDB内核)


# 数据仓库
OLAP分类：
- MOLAP 预聚合
    1. Druid
    2. Kylin
- ROLPA 实时计算
    1. MPP架构：充分利用计算，大型并行处理
        - Greenplum：底层存储基于Postgresql (阿里的
          AnalyticDB)
        - Apache Doris
        - ClickHouse
    2. SQL on Hadoop
        - 基于MPP的pipline系统
            1. Presto
            2. Apache Impala (kudu存储引擎，HDFS存储系统，impala分析引擎)
        - 基于DAG批处理系统
            1. HIVE
            2. SparkSQL
## Apach Druid （细节待补充）

https://www.cnblogs.com/WeaRang/p/12421873.html

时序+列式+全文检索

## Apache Kylin

预聚合，查询速度快

Kylin是ebay大数据部门（应该是一群来自中国的工程师）从2014年开始研发的支持TB到PB级别数据量的分布式Olap分析引擎。

Kylin（麒麟）是一个Hadoop生态圈下的MOLAP系统，其**特点**包括：
1. 可扩展的超快的OLAP引擎；
2. 提供ANSI-SQL接口；
3. 交互式查询能力；
4. MOLAP Cube 的概念；
5. 与BI工具可无缝整合。

Kylin典型的**应用场景**如下：
1. 用户数据存在于Hadoop HDFS中，利用Hive将HDFS文件数据以关系数据方式存取，数据量巨大，在500G以上；
2. 每天有数G甚至数十G的数据增量导入；
3. 有10个左右为固定的分析维度。

Kylin的核心思想是利用空间换时间，由于查询方面制定了多种灵活的策略，进一步提高空间的利用率，使得这样的平衡策略在应用中是值得采用的。


## Apache Doris
- 商业化版本：StarRocks、SelectDB
- MPP架构的OLAP系统
- 整合Google Mesa（数据模型）+Apache Impala （MPP Query Engine）和 Apache ORCFile（存储格式，编码和压缩）
    - **存储格式**：主流两类的存储格式是Apache Parquet和Apache ORC，分别来自Spark和Hive生态。两者均为适应大数据的列式存储格式，ORC在压缩编码上有特长，Parquet在半结构支持上更优。此外另有一种内存格式Apache Arrow，设计体系也属于format，但主要为内存交换优化

> https://zhuanlan.zhihu.com/p/589992367

#### 架构

##### FE 前端
1. 内存存储Metadata（paxos协议的内存性高可用架构）
2. 查询计划、查询协调分发
3. 支持多节点负载均衡、高可用、垂直变配、横向扩缩容
4. 读写分离、通过扩展支持高并发
5. 任意MySQL客户端直连

##### BE 后端
1. 负责数据存储、计算执行，以及compaction、副本管理
2. 支持垂直、横向扩缩容，PB存储容量
3. 数据自动分片，在所有节点所有磁盘自动均衡
4. 增加磁盘、数据冷热分层 HDD\SDD\对象存储

#### 特性

1. 列式存储+向量化引擎：聚合和join效率高
2. 数据分区保证高可用：
    - 每个table有多个分区表，每个分区表又有多个副本，分别存储在不同BE节点上
    -  每个table按照一定大小256M拆分为多个segment文件，每个segment是列存的LSMTree
3. 实时数据接入组件Stream Load，有：
    - 内置的 Canal 客户端实时获取 MySQL 的 binlog；
    -  通过 Doris Flink Connector 对接 Flink 的 CDC 能力实现数据的精确导入；
    - 通过内置的 Kafka客户端订阅 Kafka 的 Topic， 从而实现数据的实时更新。
4. 支持高并发下的低延时查询
    1. MVCC技术，根据主键的多版本数据
    2. 异步Compaction，在LSMTree上的两种Compaction
5. 索引结构：
    1. 智能索引：
        - 前缀稀疏索引：
          Doris 存储在文件中的数据，是按照排序列有序存储的，Doris 会在排序数据上，每 1024 行创建一个稀疏索引项。索引的 Key 即当前这1024行中，第一行的前缀排序列的值。当用户的查询条件包含这些排序列是，我们可以通过前缀稀疏索引快速的定位到起始行。
        - MinMax索引
    2. 二级索引：二级索引是用户可以选择性的在某些列上添加的辅助索引

## Snowflake：

https://www.zhihu.com/question/421034559

https://docs.snowflake.com/en/user-guide/intro-key-concepts

### 主要特点：
- 云原生，snowflake的优势用一句概况就是用现有云组件组装出的数据仓库服务
- 实时弹性，不需要的时候几乎0成本
- 自动运维：只有一个参数可设置

### 架构
-  multi-cluster + shared data架构，AP
-  整体架构分为3层：Cloud Servier -> Virture Warehouse -> Data Storage,各层通过restful api进行交互
-   数据链路：S3->SSD->Memory

#### Data Storage 存储层

块存储支持Aws S3、Azure Blob（云盘）
**优点**：无限扩容+不丢数据+超低成本，吞吐量大
**缺点**：访问延迟高，**全量写+部分读**：filte操作只能整体写入/覆盖，无法append，影响并发控制，get可以只获取部分列

#### Vitural Warehouse 计算层

一个Virtual Warehouse是一个由一堆EC2机器（叫做worker node）组成的集群，通过VW实现了计算资源的扩缩容和隔离。具体来说，一个query只在一个VW上运行，这个集群有多少个worker node可以资源的需求进行配置

##### 本地缓存与file stealing

1. 利用计算节点的磁盘进行缓存
2. File stealing是只空闲的worker node会询问peer node是否需要处理输入文件，拿到file header 和列offset后去S3下载

##### 执行引擎
- **columnar**：方便SIMD指令，利于压缩，CPU缓存友好。
- **vectorized**：流水线执行，batch处理。
- **push-based**：operator把结果push到下游operator，与volcano的pull-base正好相反

#### Cloud Services

访问控制、查询优化、事务管理、适用FoundationDB存储Metadata数据（table-files映射）等功能

##### 查询管理和优化

优化器类似Cascade-style，采用top-down cost-based的模型。因为Snowflake没有索引，整个优化的search space大大减小，同时由于将一些决策下推到执行引擎，也减少了选错最优执行方案的几率

##### 事务控制

基于底层S3 Copy-On-Write的特性，每个table file只要变更，就会产生一个新文件，同时构成新的table version，建立新的metadata维护新的映射，因此随机更新效果很差，bulk load / bulk update比较适用。
file的变更都是COW，天然适合MVCC

##### 剪枝 pruning
无索引设计，采用min-max（zone-map），维护一个chunk中的最大最小值，用于判断这个chunk中是否有目标数据


# 数据湖、湖仓一体
### 数据仓库
1. 数据格式提前规整好（schema）
2. 对海量数据有管理能力：事务型写入(ACID)、查询能力，足够好的性能
3. 是数据湖的下游应用
### 数据湖
#### 特性
1. 海量存储作为统一的数据底座
2. 任意形式的原始数据：文档、视频、语音、db中的结构、非结构化
3. 长期存在，低成本
#### 应用
1. 对外提供API对下提供数据，整合成结构化的数据仓库
2. 可以直接执行SQL进行分析
3. 机器学习的数据源
4. 具备搜索和分类功能，快速检索数据
5. 提供丰富的数据采集能力：从各类数据源摄入数据
#### 背景
数据湖的存储一般采用低成本易扩展的对象存储：AWS S3、 Ali OSS，这些存储有延迟高、无事务、只能保证最终一致性等特点，Delta，Hudi，IceBerg就是为了管理这类存储的工具，主要提供近实时化存储格式：
1. 数据摄取组件，上游对接各种类型数据源：
2. 元数据管理，存储格式的统一管理
3. 写入的事务支持
4. 下游可以对接数据存储（OSS）和计算引擎，用于提供查询、分析能力（Hive、Spark、Flink等）
在这些组件出现之前，通常使用传统的Lambda架构来实现流批处理场景

##### 传统的Lambda架构

> https://www.cnblogs.com/cciejh/p/lambda-architecture.html

基于HDFS的分布式文件系统的批量数据的计算系统（MapReduce作业）往往不是低延迟的，Lambda架构（Storm作者提出）通过批量 MapReduce作业提供了虽有些延迟但是结果准确的计算，同时通过Storm将最新数据的计算结果初步展示出来

#### Databricks（Spark的公司）-Delta Lake (细节待补充)
默认Spark为计算引擎

> https://www.databricks.com/wp-content/uploads/2020/08/p975-armbrust.pdf

#### Uber-Apache Hudi (细节待补充)

默认Spark为计算引擎
1. 数据写入和存储在基于列的存储的parquet文件（一种存储格式）
2. Copy on Write: 文件更新是发生，优化了查询性能，限制了写入性能和数据新鲜度
3. 表存储布局：Merge On Read：基于列的parquet和基于行的Avro日志文件的组合来存储数据，在读取时compaction
    - 更新可以在日志文件中批量更新，以后可以同步或异步压缩到新的 parquet 文件中，以平衡最大查询性能和降低写入放大。
    - 对于近乎实时的流式工作负载，Hudi 可以使用更高效的面向行的格式，而对于批处理工作负载，hudi 格式使用可向量化的面向列的格式，并在需要时无缝合并两种格式。

#### Netflix-Apachge Iceburgh (细节待补充)

高度抽象、通用化设计的Table Format，可以结合各种存储引擎和计算引擎，结合Flink做到流批一体


## 对象存储
### Object Storage的特性

云上的对象存储是目前全球最大的数据存储系统，包括AWS S3，Azure Blob storage，Alibaba OSS...这些存储系统的特点类似：
1. Key-Value的数据组织方式，key是数据的path，value是data object(file)
2. 超低成本
3. 超高扩展性，可以认为是近乎无限的存储容量超高持久性，写入后不用担心数据的丢失
4. 读取的延迟大，单链接吞吐量不高data object(file)支持按字节offset的范围read，但写只能是完全覆盖，也就是partial read + overwrite

这种key-value的组织形式类似filesystem，但并没有文件系统这么丰富的操作接口。而是提供了例如LIST这样的metadata API，用来快速获取data object列表，列举的方式是按照字典序获取比给定key(filepath)更大的object集合，也就相当于读取某个特定"目录"下的所有文件+子目录。
但这个操作效率很低（S3一个LIST request最多返回1000个object，如果有大量object可能花费很长时间）
从一致性的角度，object storage只能提供eventual consistency的保证，也就是说，对一个key-value对象，写入后不一定立即可见，有些系统甚至无法做到read-after-write，此外单个object的写入是原子的，但多object之间不提供原子写能力，metadata也是如此，LIST不一定看到最新状态。
从性能角度，每次读操作的基础延迟是5-10ms，这就意味着需要做大块的sequential IO(xxMB)来均摊掉这部分latency，此外由于单个read操作吞吐低 (50-100M/s），需要在上层做充分并行来提升throughput。LIST操作也需要并行来加速，但如果data object非常多，即使并行了延迟也仍然很大。写操作是原子的，但这里会有读性能和写延迟的trade-off，如果要求写入得快，可能产生文件会比较碎比较小导致不利于读，反之则写延迟会变大。

提供事务型的读写能力并保证性能ok，必然要克服以上问题，包括：
1. 尽量保证顺序访问的模式 - 列存 （Parquet/ORC）
2. data object要有合适的大小
3. 尽量避免LIST操作

### 常见解决方案
- 原始方案

  最简单的方式就是没有单独的元数据管理层，只是原生的一堆data objects集合，data objects按照类似文件目录的方式来组织，例如如果业务侧有分区，可以把不同分区组织为不同子目录，但由于上面提到的各类一致性、性能缺陷，用户会遇到类似partial update，corrupt state，没有任何管理接口，性能糟糕等问题。

- 定制Storage engine
  这也是Snowflake/Hive等系统采用的方案，把metadata，statstics组织在一个单独的，高可用的，具备事务能力的存储系统中，作为底层数据视图的"source of truth"，并利用其ACID的能力提供对底层存储的事务支持，包括并发控制、多版本管理等。这样data lake就成了一个dummy的存储系统而已，策略完全在第三方组件里。这是一个很不错也很流行的方案，但paper认为它存在以下问题：
  1. 所有I/O操作都要以第三方组件作为入口点，例如Snowflake cloud service，这使得集成变得更复杂，也降低了性能，databricks更倾向于直接访问底层存储。
  2. 存在供应商绑定问题，不具备开放性。
  3. 额外组件意味着更高的运维成本、失效风险。

- 用Object storage存储metadata
  这是Databricks采用的方案，这个"source of truth"仍然由cloud object storage来提供，所有以上定制系统提到的能力都由其完成，这也就解决了包括供应商绑定、额外组件的问题。目前Apache Hudi和Apache Iceberg也采用了同样的机制，但他们没有像Databricks一样提供更全面的上层能力，后面会介绍.


## ElasticSearch (细节待补充）

Elasticsearch会把数据写到translog然后结合FileSystemCache将数据刷到磁盘中

查询快，支持文本查找
复杂查询效率低（join、聚合），不是集群部署


![69313f7d5a80161dac0a85f6c4e7fbc9.png](evernotecid://7C421C31-405D-49A0-9EBC-98E479245B63/appyinxiangcom/50728397/ENResource/p5)
