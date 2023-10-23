package design;

public interface Pipe<I,O> {
    O process(I input);
}
