package ProduceConsumePattern.ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
AQS{
    维护了state，加锁状态 0 未加锁 1加锁 >1重入次数
    exclusiveOwnerThread 获得锁的线程 state!=0 && ==exclusiveOwnerThread 即是重入
    FIFO双链表
    acquire(){
     if (!tryAcquire(arg) && //尝试获取锁
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) //addWaiter() 当前线程转成node节点 acquireQueued()判断是不是第一个node，是的话再次尝试获取锁
            selfInterrupt(); //阻塞当前线程
    }
}

ReentrantLock implement Lock
lock阻塞线程 tryLock只有资源空闲可以获取锁时才获取，否则不阻塞当前线程，可以执行其他逻辑
调用关系 lock->调用成员Sync的lock方法，默认是NonfairSync
        ->Sync的lock调用AQS的acquire
        ->调用实现的tryAcquire方法

        unlock->调用 sync.release()->调用AQSrelease->tryRelease
        state-1 若=0释放锁成功设置当前获取锁线程为null
{
    Sync extends AQS{
        抽象方法lock();
        实现nonfairTryAcquire(acquires){
            if(state=0 && cas) 设置持有锁的线程
            else =持有锁的线程 state+=acquires 设置重入次数
        }
        release()->调用AQSrelease->tryRelease
    }
    NonfairSync{
        实现lock(){
            if(cas) 成功获取资源，设置持有锁的线程
            else acquire(1)
        }
        实现tryAcquire()执行Sync实现的nonfairTryAcquire
    }
    FairSync{
        实现lock(){
            acquire();
        }
        实现tryAcquire(){
            if(state=0 && cas && 是等待队列队首) 设置持有锁的线程
            else =持有锁的线程 state+=acquires 设置重入次数
        }
    }
}

Condition 是一个等待队列
await()操作：
1.new Node(Thread.currentThread(),Node.Condition)插入条件队列尾部
2.如果等待队列有其他线程节点，则唤醒后续的一个节点
3.阻塞当前线程
signal()操作：
1.把这个待唤醒的线程结点插入等待队列尾部
2.唤醒这个结点的线程：如果等待队列没有其他结点，该线程获取锁
前面还有其他等待线程，等队列执行完才到自己
Object.wait()/notify()是随机唤醒
Condition.signal()唤醒的是最先调用await()方法挂起的线程
* */
public class LockMessageQueue<T> implements MessageQueue<T> {
    private final static int MAX_SIZE = 3;
    private final Queue<T> queue;
    private final Lock lock = new ReentrantLock();
    // 都是lock内部的多个等待队列
    private final Condition producerCondition = lock.newCondition();
    private final Condition consumerCondition = lock.newCondition();

    public LockMessageQueue() {
        queue = new LinkedList<>();
    }

    @Override
    public boolean put(T t) {
        final Lock lock = this.lock;
        lock.lock();
        try {
            while (queue.size() == MAX_SIZE) {
                try {
                    // 添加
                    producerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.offer(t);
            producerCondition.signal();
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Override
    public T get() {
        final Lock lock = this.lock;
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    consumerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.poll();
            consumerCondition.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }
}
