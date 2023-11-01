package design.ProduceConsumePattern.ObserverSubject;

import java.util.ArrayList;
import java.util.List;

public class SubjectImpl implements Subject{
    List<Observer> observers = new ArrayList<>();
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(String message) {
        observers.forEach(observer -> observer.update(message));
    }
}
