import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

//341. 扁平化嵌套列表迭代器
public class NestedIterator implements Iterator<Integer> {
    Stack<Iterator<NestedInteger>> stack;

    public NestedIterator(List<NestedInteger> nestedList) {
        stack = new Stack<>();
        stack.push(nestedList.iterator());

        vals = new ArrayList<>();
        dfs(nestedList, vals);
        iterator = vals.iterator();
    }

    @Override
    public Integer next() {
        return stack.peek().next().getInteger();
    }

    @Override
    public boolean hasNext() {
        while (!stack.isEmpty()) {
            Iterator<NestedInteger> it = stack.peek();
            if (!it.hasNext()) {
                stack.pop();
                continue;
            }
            NestedInteger nest = it.next();
            if (nest.isInteger()) {
                List<NestedInteger> list = new ArrayList<>();
                list.add(nest);
                stack.push(list.iterator());
                return true;
            }
            stack.push(nest.getList().iterator());
        }
        return false;
    }

    List<Integer> vals;
    Iterator<Integer> iterator;

    private void dfs(List<NestedInteger> nestedList, List<Integer> list) {
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                list.add(ni.getInteger());
            } else {
                dfs(ni.getList(), list);
            }
        }
    }

    public Integer next2() {
        return iterator.next();
    }

    public boolean hasNext2() {
        return iterator.hasNext();
    }
}