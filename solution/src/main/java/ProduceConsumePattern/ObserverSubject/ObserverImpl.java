package ProduceConsumePattern.ObserverSubject;

public class ObserverImpl implements Observer {
    private String name;
    private Receiver receiver;

    public interface Receiver {
        void onMessage(String message);
    }

    public ObserverImpl(String name) {
        this.name = name;
    }

    public ObserverImpl(String name, Receiver receiver) {
        this.name = name;
        this.receiver = receiver;
    }

    @Override
    public void update(String message) {
        if (receiver != null) {
            receiver.onMessage(message);
        }
    }
}
