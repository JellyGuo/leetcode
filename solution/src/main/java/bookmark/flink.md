Flink SQL、TableAPI、DataStream API
数据源、数据消费、数据输出（sink）
checkpoint
适用于 spark streaming的 exactly once:根据 topic、partition、offset（本批次处理最后一位 offset）生成 UID
但是由于数据保存与 offset 保存不在一个事务：
1.数据保存成功，offset 保存失败（重新尝试保存 offset）
2.数据保存失败，offset 保存成功（重新尝试保存数据）
3.数据保存成功，宕机，offset 未保存，重启从上次保存的 offset 消费数据，重复消费
4.offset 保存成功，宕机，数据保存失败：丢失数据
解决：根据 topic、partition、from offset、until offset生成 UID

## Flink

https://blog.csdn.net/a805814077/article/details/108095451

### 什么是Flink

Flink是一个框架和分布式处理引擎，用于对无边界和有边界的数据流进行有状态的计算。

### 特点

**1. 支持有状态计算**：保存中间结果到内存或文件系统，下一个事件进入算子后可以获取之前状态的中间结果
**2. 支持事件时间**：根据事件本身自带的时间戳（事件的产生时间）进行结果的计算。这种基于事件驱动的机制使得事件即使乱序到达，Flink也能够计算出精确的结果，保证了结果的准确性和一致性。
**3. 支持高可用**：Flink可以将任务执行的快照保存在存储介质上，当需要停机运维等操作时，下次启动可以直接从事先保存的快照恢复原有的计算状态，使得任务继续按照停机之前的状态运行。
**4. 吞吐量高**
**5. 容错性好**：Flink的容错机制是基于分布式快照实现的，通过CheckPoint机制保存流处理作业某些时刻的状态，当任务异常结束时，默认从最近一次保存的完整快照处恢复任务。
**6. 提供不同层级API**：SQL-TableAPI-DataStream/DataSetAPI

### 程序运行流程与结构

1. 用户提交flink程序，会转换成逻辑数据流图，通过客户端Job client 连同jar包发送给服务端的JobManager
2. JobManager接收到逻辑数据流图转成物理数据流图：真实可执行、可放置在TaskManager上
3. TaskManager会把资源分成一个一个TaskSlot（实际上相当于jvm，是一个具体的线程，对内存划分，不对cpu划分）

**Source -> Transformation -> Sink**

Source：数据源操作，文件、socket、kafka
Transformation：map、flatmap、reduce
Sink：hdfs、mysql、kafka


### 流批一体处理引擎

Flink的执行引擎采用了一种十分灵活的方式，同时支持了这两种数据传输模型。

Flink以固定的缓存块为单位进行网络数据传输，用户可以通过缓存块超时值指定缓存块的传输时机。

如果缓存块的超时值为0，则Flink的数据传输方式类似上面提到的流处理系统的标准模型，此时系统可以获得最低的处理延迟。

如果缓存块的超时值为无限大，则Flink的数据传输方式类似上面提到的批处理系统的标准模型，此时系统可以获得最高的处理吞吐量。

### Flink关键机制

四个机制：**状态（State）、时间（Time）、检查点（Checkpoint）、窗口（Window）**



#### Window
Window是一种切割无限数据为有限块进行处理的手段，可以将无线的stream拆分成有限大小的buckets桶
**类型**
1. CountWindow：数据驱动，按照指定的数据条数生成一个Window，与时间无关。
2 .TimeWindow: 时间驱动，按照时间生成Window。
    1. 滚动窗口（Tumbling Window）：时间对齐，窗口长度固定，没有重叠
    ``stream.timeWindow(Time.minutes(1));``
    2. 滑动窗口（Sliding Window）：窗口长度+滑动间隔
    ``stream.timeWindow(Time.minutes(1),Time.seconds(30));``
    3. 会话窗口（Session Window）：一系列事件组合一个指定时间长度的timeout间隙组成，类似于web应用的session，也就是一段时间没有接收到新数据就会生成新的窗口

#### Time
1. Event time：事件发生时的时间，生产消息的时间（业务携带）
2. Ingestion time：事件进入 Flink 计算程序的时间，这个时候数据已经发送给窗口，也就是发送给窗口的时间，也就是程序处理计算的时间
3. Processing time：指执行相应操作的机器的系统时间

#### 乱序处理机制 Watermark

##### 乱序问题

Flink接收到的事件的先后顺序不是严格按照事件的event time顺序排列；
此时只能根据eventTime决定window的运行，（不明确数据是否全部到位，又不能无限期等下去）利用watermark机制来保证一个特定时间后触发window去进行计算, 告诉算子延迟到达的消息不应该再被接收

##### Watermark机制
- Watermark是一种衡量Event Time进展的机制。
- Watermark是用于处理乱序事件的，而正确的处理乱序事件，通常用Watermark机制结合window来实现。
- 数据流中的Watermark用于表示timestamp小于Watermark的数据都已经到达了，因此，window的执行也是由Watermark触发的。
- Watermark可以理解成一个延迟触发机制，我们可以设置Watermark的延时时长t，每次系统会校验已经到达的数据中最大的maxEventTime，然后认定eventTime小于maxEventTime - t的所有数据都已经到达，如果有窗口的停止时间等于maxEventTime – t，那么这个窗口被触发执行。
- watermark 用来让程序自己平衡延迟和结果正确性

##### Watermark原理

Watermark会携带一个单调递增的时间戳t，Watermark(t)表示所有时间戳不大于t的数据都已经到来了，未来小于等于t的数据不会再来，因此可以放心地触发和销毁窗口了。

Watermark就是触发前一窗口的“关窗时间”，一旦触发关门那么以当前时刻为准在窗口范围内的所有所有数据都会收入窗中。

##### 延迟的数据

无法设置完美的Watermark数值，因此存在Watermark(t)后还有小概率接收t之前数据的情况，（late elements）

##### 延迟数据处理机制

延迟事件是乱序事件的特例，和一般乱序事件不同的是它们的乱序程度超出了水位线( Watermark)的预计，导致窗口在它们到达之前已经关闭。

延迟事件出现时窗口已经关闭并产出了计算结果，对于此种情况处理的方法有3种:
- 重新激活已经关闭的窗口并重新计算以修正结果
   **Allowed Lateness机制**：Allowed Lateness机制允许用户设置一个允许的最大延迟时长。Flink会在窗口关闭后一直保存窗口的状态直至超过允许延迟时长，这期间的延迟事件不会被丢弃，而是默认会触发窗口重新计算。
- 将延迟事件收集起来另外处理。
   **Side Output机制**：Side Output机制可以将延迟事件单独放入一个数据流分支，这会作为Window计算结果的副产品，以便用户获取并对其进行特殊处理
- 将延迟事件视为错误消息并丢弃。

#### State

Flink 的失败恢复依赖于“检查点机制+可部分重发的数据源”。
1. 检查点机制：检查点定期触发，产生快照，快照中记录了：
    （1）当前检查点开始时数据源（例如 Kafka）中消息的 offset 
    （2）记录了所有有状态的 operator 当前的状态信息（例如 sum 中的数值）。
2. 可部分重发的数据源：
（1）Flink 选择最近完成的检查点 K。
（2）然后系统重放整个分布式的数据 流，然后给予每个 operator 他们在检查点 k 快照中的状态。（3）数据源被设置为从位置 Sk 开始 重新读取流。例如在 Apache Kafka 中，那意味着告诉消费者从偏移量 Sk 开始重新消费。

Flink 中有两种基本类型的 State，即 Keyed State 和 Operator State。State 可以被记录，在失败的情况下数据还可以恢复。state 一般指一个具体的 task/operator 的状态【state 数据默认保存在 JVM 的 堆内存中】

####  容错机制 Checkpoint


checkpoint 则表示了一个 Flink Job 在一个特定时刻的一份全局状态快照，即包含了所有 的 task/operator 的状态。可以理解成 checkpoint 是把所有 state 数据持久化存储了。
为了保证程序的容错恢复以及程序启动时其状态恢复，Flink任务都会开启Checkpoint或者触发Savepoint进行状态保存。

- **Checkpoint机制**: 这种机制保证了实时程序运行时，即使突然遇到异常也能够进行自我恢复。Checkpoint对于用户层面，是透明的，用户会感觉不到Checkpoint过程的存在。
- **Savepoint机制**: 是在某个时间点程序状态全局镜像，以后程序在进行升级，或者修改并发度等情况，还能从保存的状态位继续启动恢复。Savepoint可以看做是Checkpoint在特定时期的一个状态快照。

##### Checkpoint

异步轻量级分布式快照技术：

1. 分布式快照可以将同一时间点Task/Operator的状态数据全局统一快照处理
2. Flink会在输入的数据集上间隔性地生成checkpoint barrier，通过棚栏( barrier)将间隔时间段内的数据划分到相应的checkpoint中。
3. 当应用出现异常时，Operator就能够从上一次快照中恢复所有算子之前的状态，从而保证数据的一致性。
4. 对于状态占用空间比较小的应用，快照产生过程非常轻量，高频率创建且对Flink任务性能影响相对较小
5. Checkpoint过程中状态数据一般被保存在一个可配置的环境中，通常是在JobManager节点或HDFS上

注：默认情况下Flink不开启检查点，用户需要在程序中通过调用enableCheckpointing(n)方法配置和开启检查点，其中n为检查点执行的时间间隔，单位为毫秒。

重要参数：
    1. **Checkpoint超时时间**：指定每次Checkpoint执行过程中的上限时间范围，一旦Checkpoint执行时间超过该阈值，Flink将会中断Checkpoint过程,并按照超时处理
    2. **检查点之间最小时间间隔**：防止出现状态数据过大而导致Checkpoint执行时间过长，从而导致Checkpoint积压过多
    3. **最大并行执行的检查点数量**：默认1个
    4. **外部检查点**：将状态数据持久化到外部系统中，使用这种方式不会在任务停止的过程中清理掉检查点数据，而是一直保存在外部系统介质中
    
#### 语义选择

1. 批处理系统，由于文件可以重复访问，重试比较容易实现
2. 流处理系统，由于数据源无限，且无法缓存和持久化，flink基于分布式快照和可部分重发的数据源实现容错
3. 处理次数分为：At-Most-Once、At-Least-Once、Exactly-Once。
**At-Most-Once**：每条数据最多被处理一次，会有丢失数据的可能
**At-Least-Once**：每条数据至少被处理一次，保证数据不会丢失，但数据可能会被重复处理，吞吐量较高
**Exactly-Once**：每条数据仅被处理一次，不会丢失数据，也不会重复处理，性能较弱

#### Savepoint

Savepoints是用户以手工命令的方式触发，并将结果持久化到指定的存储路径中，目的是帮助用户在升级和维护集群过程中保存系统中的状态数据


|  | Checkpoint | Savepoint |
| --- | --- | --- |
| 触发方式 | 由Flink自动触发并管理  | 由用户手动触发并管理 |
| 主要用途 | 在Task发生异常时快速恢复 | 有计划的备份，修改代码、调整并发 |
| 特点 | 1.轻量 2.自动故障恢复 3.作业停止后默认清除 | 1.持久 2.格式标准 3.可手动 |

#### Barriers

##### Barriers 是 Flink 快照的核心要素。它们被摄取到数据流中而不会影响流量。barriers 永远不会超过记录。他们将记录集合分为快照。每个 barriers 都带有唯一的 ID

1. 出现一个 Barrier，在该 Barrier 之前出现的记录都属于该 Barrier 对应的 Snapshot，在该 Barrier 之后出现的记录属于下一个 Snapshot
2. 来自不同 Snapshot 多个 Barrier 可能同时出现在数据流中，也就是说同一个时刻可能并发生成多个 Snapshot
3. 当一个中间（Intermediate）Operator 接收到一个 Barrier 后，它会发送 Barrier 到属于该 Barrier 的 Snapshot 的数据流中，等到 Sink Operator 接收到该 Barrier 后会向 Checkpoint Coordinator 确认该 Snapshot，直到所有的 Sink Operator 都确认了该 Snapshot，才被认为 完成了该 Snapshot

Snapshot 并不仅仅是对数据流做了一个状态的 Checkpoint，它也包含了一个 Operator 内部所 持有的状态，这样才能够在保证在流处理系统失败时能够正确地恢复数据流处理。

##### 具体流程：
1. 一旦操作算子从一个输入流接收到快照 barriers n，它就不能处理来自该流的任何记录， 直到它从其他输入接收到 barriers n 为止。否则，它会搞混属于快照 n 的记录和属于快照 n + 1 的记录。
2. barriers n 所属的流暂时会被搁置。从这些流接收的记录不会被处理，而是放入输入缓冲区。
3. 一旦从最后一个流接收到 barriers n，操作算子就会发出所有挂起的向后传送的记录， 然后自己发出快照 n 的 barriers。
4. 之后，它恢复处理来自所有输入流的记录，在处理来自流的记录之前优先处理来自输 入缓冲区的记录

##### 类型

1. **BarrierBuffer** 通过阻塞已接收到 barrier 的 input channel 并缓存被阻塞的 channel 中后续流入的数据流，直到所有的 barrier 都接收到或者不满足特定的检查点的条件后，才会释放这些被阻塞的 channel，这个机制被称之为–aligning（对齐）。正是这种机制来实现 EXACTLY_ONCE 的一致性（它将检查点中的数据精准得隔离开）。

2. 而 **BarrierTrack** 的实现就要简单地多，它仅仅是对数据流中的 barrier 进行跟踪，但是数据流 中的元素 buffer 是直接放行的。这种情况会导致同一个检查点中可能会预先混入后续检查 点的元素，从而只能提供 AT_LEAST_ONCE 的一致性。

##### barrier 作用

它会作为数据流的记录被同等看待，被插入到数据流中，将数据流中记录的进 行分组，并沿着数据流的方向向前推进

**具体排列过程如下：**

1. Operator 从一个 incoming Stream 接收到 Snapshot Barrier n，然后暂停处理，直到其它的 incoming Stream 的 Barrier n（否则属于 2 个 Snapshot 的记录就混在一起了）到达该 Operator 接收到 Barrier n 的 Stream被临时搁置，来自这些 Stream 的记录不会被处理，而 是被放在一个 Buffer 中。

2. 一旦最后一个 Stream 接收到 Barrier n，Operator 会 emit 所有暂存在 Buffer 中的记录， 然后向 Checkpoint Coordinator 发送 Snapshot n，继续处理来自多个 Stream 的记录

3. 基于 Stream Aligning 操作能够实现 Exactly Once 语义，但是也会给流处理应用带来延迟，因为为了排列对齐 Barrier，会暂时缓存一部分 Stream 的记录到 Buffer 中，尤其是在数据流并行度很高的场景下可能更加明显，通常以最迟对齐 Barrier 的一个 Stream 为处理 Buffer 中缓存记录的时刻点。在 Flink 中，提供了一个开关，选择是否使用 Stream Aligning， 如果关掉则 Exactly Once 会变成 At least once。