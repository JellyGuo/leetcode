import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// 双优先队列求中位数
// 延时删除
public class DualHeap {
    PriorityQueue<Integer> largerHalf;
    PriorityQueue<Integer> smallerHalf;
    Map<Integer, Integer> delayed;
    int largerHalfSize;
    int smallerHalfSize;
    int k;

    public DualHeap(int k) {
        largerHalf = new PriorityQueue<>(Integer::compareTo);
        smallerHalf = new PriorityQueue<>(Comparator.reverseOrder());
        delayed = new HashMap<>();
        largerHalfSize = 0;
        smallerHalfSize = 0;
        this.k = k;
    }

    public double getMedian() {
        return (k & 1) == 1 ? largerHalf.peek() : ((double) largerHalf.peek() + smallerHalf.peek()) / 2.0;
    }

    public void insert(int x) {
        if (largerHalf.isEmpty() || x > largerHalf.peek()) {
            largerHalfSize++;
            largerHalf.offer(x);
        } else {
            smallerHalfSize++;
            smallerHalf.offer(x);
        }
        makeBalance();
    }

    public void erase(int x) {
        delayed.put(x, delayed.getOrDefault(x, 0) + 1);
        if (x >= largerHalf.peek()) {
            largerHalfSize--;
            if (x == largerHalf.peek()) {
                prune(largerHalf);
            }
        } else {
            smallerHalfSize--;
            if (x == smallerHalf.peek()) {
                prune(smallerHalf);
            }
        }
        makeBalance();
    }

    private void makeBalance() {
        if (largerHalfSize > smallerHalfSize + 1) {
            largerHalfSize--;
            smallerHalfSize++;
            smallerHalf.offer(largerHalf.poll());
            prune(largerHalf);
        } else {
            largerHalfSize++;
            smallerHalfSize--;
            smallerHalf.offer(largerHalf.poll());
            prune(smallerHalf);
        }
    }

    private void prune(PriorityQueue<Integer> heap) {
        while (!heap.isEmpty()) {
            int num = heap.peek();
            if (delayed.containsKey(num)) {
                delayed.put(num, delayed.get(num) - 1);
                if (delayed.get(num) == 0) {
                    delayed.remove(num);
                }
                heap.poll();
            } else {
                break;
            }
        }
    }
}
