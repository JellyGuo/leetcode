package ProduceConsumePattern.PublisherSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublisherImpl implements Publisher {
    private static Map<String, List<Subscriber>> subscribers = new HashMap<>();

    public void subscribe(String topic, Subscriber subscriber) {
        List<Subscriber> subscriberList = subscribers.computeIfAbsent(topic, k -> new ArrayList<>());
        subscriberList.add(subscriber);
    }

    public void unsubscribe(String topic, Subscriber subscriber) {
        if (subscribers.containsKey(topic)) {
            List<Subscriber> subscriberList = subscribers.get(topic);
            subscriberList.remove(subscriber);
        }
    }

    public void publish(String topic, String message) {
        subscribers.getOrDefault(topic, new ArrayList<>()).forEach(subscriber -> subscriber.update(message));
    }

    public static void main(String[] args) {
        Publisher publisher = new PublisherImpl();
        Subscriber subscriber1 = message -> System.out.println("1");
        Subscriber subscriber2 = message -> System.out.println("2");
        publisher.subscribe("news", subscriber1);
        publisher.subscribe("news", subscriber2);
        publisher.publish("news", "111");
    }
}
