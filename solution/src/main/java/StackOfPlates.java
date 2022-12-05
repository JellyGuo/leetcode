import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//面试题 03.03. 堆盘子
public class StackOfPlates {
    List<Stack<Integer>> list;
    int capacity;
    int idx;

    public StackOfPlates(int cap) {
        list = new ArrayList<>();
        capacity = cap;
        idx = -1;
    }

    public void push(int val) {
        if (capacity <= 0) return;
        if (idx < 0) {
            Stack<Integer> cur = new Stack<>();
            cur.push(val);
            list.add(cur);
            idx++;
        } else {
            Stack<Integer> cur = list.get(idx);
            if (cur.size() == capacity) {
                cur = new Stack<>();
                list.add(cur);
                idx++;
            }
            cur.push(val);
        }
    }

    public int pop() {
        if (capacity <= 0) return -1;
        if (idx < 0 || list.size() == 0) return -1;
        Stack<Integer> cur = list.get(idx);
        int res = cur.pop();
        if (cur.isEmpty()) {
            list.remove(idx--);
        }
        return res;
    }

    public int popAt(int index) {
        if (capacity <= 0) return -1;
        if (index > idx || list.size() == 0) return -1;
        if (idx < 0) return -1;
        Stack<Integer> cur = list.get(index);
        int res = cur.pop();
        if (cur.isEmpty()) {
            list.remove(index);
            idx--;
        }
        return res;
    }
}
