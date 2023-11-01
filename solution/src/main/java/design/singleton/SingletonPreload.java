package design.singleton;

public class SingletonPreload {
    // 类初始化时加载，浪费内存，引用该类但不一定需要该类的实例
    private static SingletonPreload instance = new SingletonPreload();
    private SingletonPreload(){
    }
    public static SingletonPreload getInstance(){
        return instance;
    }
}
