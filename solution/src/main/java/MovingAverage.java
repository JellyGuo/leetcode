import java.util.ArrayDeque;
import java.util.Queue;

//剑指 Offer II 041. 滑动窗口的平均值
public class MovingAverage {
    Queue<Double> queue;
    double sum;
    int size;

    /**
     * Initialize your data structure here.
     */
    public MovingAverage(int size) {
        sum = 0d;
        this.size = size;
        queue = new ArrayDeque<>();
    }

    public double next(int val) {
        if (queue.size() < size) {
            queue.offer((double) val);
            sum += val;
            return sum / queue.size();
        }
        sum -= queue.poll();
        sum += val;
        queue.offer((double) val);
        return sum / size;
    }
}
