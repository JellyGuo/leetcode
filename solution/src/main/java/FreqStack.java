import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

//895. 最大频率栈
//设计一个类似堆栈的数据结构，将元素推入堆栈，并从堆栈中弹出出现频率最高的元素。
//如果出现频率最高的元素不只一个，则移除并返回最接近栈顶的元素。
class FreqStack {
    Map<Integer, Integer> freq;
    Map<Integer, Deque<Integer>> group;
    int maxFreq;

    public FreqStack() {
        freq = new HashMap<>();
        group = new HashMap<>();
    }

    public void push(int val) {
        freq.put(val, freq.getOrDefault(val, 0) + 1);
        group.putIfAbsent(freq.get(val), new ArrayDeque<>());
        group.get(freq.get(val)).push(val);
        maxFreq = Math.max(maxFreq, freq.get(val));
    }

    public int pop() {
        int ans = group.get(maxFreq).pop();
        freq.put(ans, freq.get(ans) - 1);
        if (group.get(maxFreq).isEmpty()) {
            group.remove(maxFreq--);
        }
        return ans;
    }
}