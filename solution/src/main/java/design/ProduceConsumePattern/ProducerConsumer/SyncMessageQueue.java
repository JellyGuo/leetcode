package design.ProduceConsumePattern.ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;

/*
synchronized 修饰实例方法、代码块、(this)的话锁对象实例：
    同一时刻只有一个线程可以进入实例X的其中一个实例方法，多个实例间同步方法可以并发
synchronized 修饰静态方法、静态代码块，会锁类：
    全部实例、类的调用 同一时刻只能有一个线程执行同步方法、代码块
()后面写什么 不建议字符串常量、基本数据类型作为锁对象，因为常量池存在方法区，所有线程共享
要锁线程共享资源的对象
wait/notify是Object的方法：持有Object对象锁的线程可以调用，否则会抛异常，一般放到synchronized代码后面
调用wait()会释放锁对象，其他线程可以进入对象的其他或者该同步方法获取对象的控制权，然后执行notify方法
wait写在while而不是if中，这样被唤醒后可以重复检查条件
notify通知等待队列的第一个线程，notifyall通知所有线程
执行完notify要等执行完synchronized才会释放锁，
*/
public class SyncMessageQueue<T> implements MessageQueue<T> {
    private final static int MAX_SIZE = 3;
    private final Queue<T> queue;

    public SyncMessageQueue() {
        queue = new LinkedList<>();
    }

    @Override
    public synchronized boolean put(T t) {
        while (queue.size() == MAX_SIZE) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        queue.offer(t);
        notify();
        return true;
    }

    @Override
    public synchronized T get() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        T t = queue.poll();
        notify();
        return t;
    }
}
