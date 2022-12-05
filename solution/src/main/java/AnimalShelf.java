import java.util.ArrayDeque;
import java.util.Queue;

public class AnimalShelf {
    Queue<int[]> cats;
    Queue<int[]> dogs;
    int cnt;

    public AnimalShelf() {
        cats = new ArrayDeque<>();
        dogs = new ArrayDeque<>();
        cnt = 0;
    }

    public void enqueue(int[] animal) {
        if (animal[1] == 0) {
            cats.offer(new int[]{++cnt, animal[0]});
        } else {
            dogs.offer(new int[]{++cnt, animal[0]});
        }
    }

    public int[] dequeueAny() {
        if (cats.isEmpty() && dogs.isEmpty()) return new int[]{-1, -1};
        if (cats.isEmpty()) return new int[]{dogs.poll()[1], 1};
        if (dogs.isEmpty()) return new int[]{cats.poll()[1], 0};
        if (cats.peek()[0] < dogs.peek()[0]) return new int[]{cats.poll()[1], 0};
        return new int[]{dogs.poll()[1], 1};
    }

    public int[] dequeueDog() {
        if (dogs.isEmpty()) return new int[]{-1, -1};
        return new int[]{dogs.poll()[1], 1};
    }

    public int[] dequeueCat() {
        if (cats.isEmpty()) return new int[]{-1, -1};
        return new int[]{cats.poll()[1], 0};
    }
}
