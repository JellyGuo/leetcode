
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
## NoSql：
### Cassandra (AWS DynamoDB)
#### 概念
>Cassandra 是一个分区的行存储。行被组织成具有所需主键的表。
>
>分区意味着 Cassandra 可以在应用程序透明的情况下将您的数据分布在多台机器上。Cassandra 将在集群中添加和删除机器时自动重新分区。
>
>行存储意味着像关系数据库一样，Cassandra 按行和列组织数据。

列族，但不是面向列，行分区存储
#### 特点
1. dynamo风格分布式节点：
    - 去中心化，每个节点都可以写数据
2. 单机效率高：bigtable LSM单机引擎
    1. 写入数据的时候先写入commit log来保持数据持久化，
    2. 然后写入Memtable（内存表），根据partition key和clustering key排序
    3. 在Memtable排序后写入SSTable（磁盘文件），SSTable包含全部的行数据
    4. 之后后台Compaction线程会合并多个SSTable
3. 根据partition key路由到不同node，不是传统列式数据库，因为数据根据key hash存储在不同分片上，针对某列的聚合操作十分低效
    1. 查询时，根据partition key路由到不同node
    2. Memtable和SSTable中过滤partition key和其他条件
        - SSTable中是压缩文件，需要解压，查询数据多时会OOM
 
 rowkey1->{column1,column2,column3}
 rowkey2->{column1,column4}
## Hbase
## 列式数据库 HBase/Clickouse
ID Last First Bonus
1 Doe John 8000
2 Smith Jane 4000
3 Beck Sam 1000
- 在面向行的数据库管理系统中，数据将像这样存储： 
1,Doe,John,8000;2,Smith,Jane,4000;3,Beck,Sam,1000;
- 在面向列的数据库管理系统中，数据将像这样存储:
1,2,3;Doe,Smith,Beck;John,Jane,Sam;8000,4000,1000;

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

#### clickhouse
更适合实时分析和查询，HBase更适合海量数据存储和管理（实时读写）
**适用场景**
1. 读多于写，读大量行少量列
2. 低频批量写入
3. 无事务性，数据一致性较低

## 时序数据库 Influxdb
随时间不断产生的一系列数据，简单说，带时间戳的数据
非CRUD，不支持更新数据、删除单条数据操作
TSM 类似于LSM
### 概念
**度量 Metric/Measurement**：类似table，一系列同类时序数据的集合
**标签 Tag**：key-value结构，描述数据源特征，不随时间变化: 设备ID
**时间戳 Timestamp**：可以写入时指定，或系统自动生成
**量测值 Field**：描述数据源量测指标，随时间变化：设备温度等
**数据点 DataPoint**: 数据源在某个时间产生的某个量测指标值为一个数据点，查询与写入按数据点来作为统计指标
**时间线 Time Series**: 数据源的某一个指标随时间变化，形成时间线，Metric + Tags + Field 组合确定一条时间线；针对时序数据的计算包括降采样、聚合（sum、count、max、min等）、插值等都基于时间线维度进行；

### 特性
1. 多写少读
2. 无更新：可利用时间戳（Timestamp）和时间序列线（Series）完全相同的时序数据记录，是同一条时序数据记录，新插入的时序数据，会覆盖原有的时序数据记录
3. 不支持单条删除时序数据记录，
    1. 通过保留策略周期性定时删除
    2. 通过WHERE条件语句、删除时间序列线、删除表、删除数据库、删除分片（Shard）等方式直接批量删除指定的时序数据记录，**不支持Field，只支持标签和时间戳**
 4. 不支持join，可以连续查询
 5. 配合Telegraf监控、Grafana服务做可视化
 
 ### TSDB存储引擎（Shard）
**Shard** 在 InfluxDB 中按照数据的时间戳所在的范围，会去创建不同的 shard，每一个 shard 都有自己的 cache、wal、tsm file 以及 compactor，这样做的目的就是为了可以通过时间来快速定位到要查询数据的相关资源，加速查询的过程，并且也让之后的批量删除数据的操作变得非常简单且高效

在 LSM Tree 中删除数据是通过给指定 key 插入一个删除标记的方式，数据并不立即删除，需要等之后对文件进行压缩合并时才会真正地将数据删除，所以删除大量数据在 LSM Tree 中是一个非常低效的操作。

而在 InfluxDB 中，通过 retention policy 设置数据的保留时间，当检测到一个 shard 中的数据过期后，只需要将这个 shard 的资源释放，相关文件删除即可，这样的做法使得删除过期数据变得非常高效

 一个shard就是编码压缩后的数据真实存储的位置。这种数据被组织成了一个TSM的结构。并且每一个shard属于一个shard group。
 
**Retention policy** 描述数据保留多久，数据的副本数量以及shard group 的时间范围
 
**Shard group**是一个逻辑概念，和retention policy（保留策略）相关，决定一个shard group 有限期多长时间的叫做shard duration
 
 ### TSM（Time-Structured Merge Tree）
 
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
### 目录结构
 influxdb->data->db->rp->shard->tsm引擎
            ->meta->meta.db
            ->wal->db->rp->shard->wal file
## ElasticSearch

Elasticsearch会把数据写到translog然后结合FileSystemCache将数据刷到磁盘中

