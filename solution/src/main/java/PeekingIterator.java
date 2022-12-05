import java.util.Iterator;

//284. 顶端迭代器
class PeekingIterator implements Iterator<Integer> {
    Iterator<Integer> iter;
    Integer next;

    public PeekingIterator(Iterator<Integer> iterator) {
        iter = iterator;
        if (iter.hasNext()) next = iter.next();
    }

    public Integer peek() {
        return next;
    }

    @Override
    public Integer next() {
        Integer ans = next;
        next = iter.hasNext() ? iter.next() : null;
        return ans;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

}
