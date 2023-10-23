package design;

public interface Sink<T> {
    Sink<T> addData(T data);
}
