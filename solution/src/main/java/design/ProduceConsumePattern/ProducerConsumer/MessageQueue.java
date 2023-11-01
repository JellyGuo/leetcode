package design.ProduceConsumePattern.ProducerConsumer;

public interface MessageQueue<T> {
    boolean put(T t);
    T get();
}
