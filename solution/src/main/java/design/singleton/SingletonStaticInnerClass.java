package design.singleton;

public class SingletonStaticInnerClass {
    private SingletonStaticInnerClass(){}
    // 加载外部类时不会加载内部类
    private static class Singleton{
        private static SingletonStaticInnerClass instance = new SingletonStaticInnerClass();
    }
    //只有执行该方法时，才会去加载Singleton类从而加载Singleton的static成员
    public SingletonStaticInnerClass getInstance(){
        return Singleton.instance;
    }
}
