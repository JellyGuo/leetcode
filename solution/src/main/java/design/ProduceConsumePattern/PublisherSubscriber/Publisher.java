package design.ProduceConsumePattern.PublisherSubscriber;

public interface Publisher {
    void subscribe(String topic, Subscriber subscriber);

    void unsubscribe(String topic, Subscriber subscriber);


    void publish(String topic, String message);
}
