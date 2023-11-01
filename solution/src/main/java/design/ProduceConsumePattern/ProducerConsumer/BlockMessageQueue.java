package design.ProduceConsumePattern.ProducerConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockMessageQueue<T> implements MessageQueue<T>{
    private final BlockingQueue<T> queue;

    public BlockMessageQueue(){
        queue = new LinkedBlockingQueue<>();
    }
    @Override
    public boolean put(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public T get() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
