package design;

import java.util.function.Supplier;

public class Pipeline<I, O> {
    private final Pipe<I, O> currentPipes;

    public Pipeline(Pipe<I, O> currentPipes) {
        this.currentPipes = currentPipes;
    }

    public <K> Pipeline<I, K> addPipe(Pipe<O, K> newPipe) {
        return new Pipeline<>(input -> newPipe.process(currentPipes.process(input)));
    }

    public O execute(I input) {
        return currentPipes.process(input);
    }

    public static void main(String[] args) {
        Pipe<String, Integer> pipe1 = Integer::parseInt;
        Pipe<Integer, Integer> pipe2 = i -> i * 2;
        Pipe<Integer, String> pipe3 = String::valueOf;
        Pipeline<String, String> pipeline = new Pipeline<>(pipe1).addPipe(pipe2).addPipe(pipe3);
        System.out.println(pipeline.execute("1"));
    }
}
