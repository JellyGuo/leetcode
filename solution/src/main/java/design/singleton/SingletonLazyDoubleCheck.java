package design.singleton;

public class SingletonLazyDoubleCheck {
    private SingletonLazyDoubleCheck() {
    }

    private static SingletonLazyDoubleCheck instance;

    public static SingletonLazyDoubleCheck getInstance() {
        if (instance == null) {
            synchronized (SingletonLazyDoubleCheck.class) {
                if (instance == null) {
                    instance = new SingletonLazyDoubleCheck();
                }
            }
        }
        return instance;
    }
}
