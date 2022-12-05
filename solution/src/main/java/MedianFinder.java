import java.util.PriorityQueue;

//295. 数据流的中位数
public class MedianFinder {
    PriorityQueue<Integer> small;
    PriorityQueue<Integer> large;

    public MedianFinder() {
        small = new PriorityQueue<>((o1, o2) -> o2 - o1);
        large = new PriorityQueue<>();
    }

    public void addNum(int num) {
        if (small.size() == large.size()) {
            large.offer(num);
            small.offer(large.poll());
        } else {
            small.offer(num);
            large.offer(small.poll());
        }
    }

    public double findMedian() {
        if (small.size() == large.size()) {
            return (small.peek() + large.peek()) / 2.0;
        } else {
            return small.peek();
        }
    }
}
