import java.util.HashMap;
import java.util.Map;

public class MyCalendarThreeSegmentTree {
    //https://leetcode.cn/problems/my-calendar-iii/solutions/1534312/wo-de-ri-cheng-an-pai-biao-iii-by-leetco-9rif/
    private Map<Integer, Integer> tree;
    private Map<Integer, Integer> lazy;


    public MyCalendarThreeSegmentTree() {
        segTrees = new SegTree[M];
        tree = new HashMap<>();
        lazy = new HashMap<>();
    }

    public int book(int start, int end) {
        update(start, end - 1, 0, 1000000000, 1);
        return tree.getOrDefault(1, 0);
    }

    public void update(int start, int end, int l, int r, int idx) {
        if (r < start || end < l) {
            return;
        }
        if (start <= l && r <= end) {
            tree.put(idx, tree.getOrDefault(idx, 0) + 1);
            lazy.put(idx, lazy.getOrDefault(idx, 0) + 1);
        } else {
            int mid = l + r >> 1;
            update(start, end, l, mid, 2 * idx);
            update(start, end, mid + 1, r, 2 * idx + 1);
            tree.put(idx, lazy.getOrDefault(idx, 0) + Math.max(tree.getOrDefault(2 * idx, 0), tree.getOrDefault(2 * idx + 1, 0)));
        }
    }

    //通用方法
    SegTree[] segTrees;
    // N 根节点线段右区间 M数组长度 cnt数组下标从1开始
    int N = (int) 1e9, M = 120010, cnt = 1;

    // 729
    public boolean book1(int start, int end) {
        if (queryCt(1, 1, N + 1, start + 1, end) > 0) return false;
        updateCt(1, 1, N + 1, start + 1, end, 1);
        return true;
    }

    // 731
    public boolean book2(int start, int end) {
        if (queryMax(1, 1, N + 1, start + 1, end) >= 2) return false;
        updateMax(1, 1, N + 1, start + 1, end, 1);
        return true;
    }

    // 732
    public int book3(int start, int end) {
        updateMax(1, 1, N + 1, start + 1, end, 1);
        return queryMax(1, 1, N + 1, 1, N + 1);
    }

    /**
     * @param node  当前节点在数组中的下标
     * @param lc    当前节点线段的左区间
     * @param rc    当前节点线段的右区间
     * @param left  要更新的线段左区间
     * @param right 要更新的线段右区间
     * @param val   要更新的值
     */
    void updateCt(int node, int lc, int rc, int left, int right, int val) {
        if (left <= lc && rc <= right) {
            segTrees[node].ct += (rc - lc + 1) * val;
            segTrees[node].add += val;
            return;
        }
        lazyCreate(node);
        pushdown(node, rc - lc + 1);
        int mid = lc + rc >> 1;
        if (left <= mid) updateCt(segTrees[node].leftChildIdx, lc, mid, left, right, val);
        if (right > mid) updateCt(segTrees[node].rightChildIdx, mid + 1, rc, left, right, val);
        pushup(node);
    }

    void updateMax(int node, int lc, int rc, int left, int right, int val) {
        if (left <= lc && rc <= right) {
            segTrees[node].max += val;
            segTrees[node].add += val;
            return;
        }
        lazyCreate(node);
        pushdown(node, rc - lc + 1);
        int mid = lc + rc >> 1;
        if (left <= mid) updateMax(segTrees[node].leftChildIdx, lc, mid, left, right, val);
        if (right > mid) updateMax(segTrees[node].rightChildIdx, mid + 1, rc, left, right, val);
        pushup(node);
    }

    /**
     * @param node  当前节点在数组中的下标
     * @param lc    当前节点线段的左区间
     * @param rc    当前节点线段的右区间
     * @param left  要更新的线段左区间
     * @param right 要更新的线段右区间
     * @return [left, right]的数量/最大值
     */
    int queryCt(int node, int lc, int rc, int left, int right) {
        if (left <= lc && rc <= right) return segTrees[node].ct;
        lazyCreate(node);
        pushdown(node, rc - lc + 1);
        int mid = lc + rc >> 1, ans = 0;
        if (left <= mid) ans += queryCt(segTrees[node].leftChildIdx, lc, mid, left, right);
        if (right > mid) ans += queryCt(segTrees[node].rightChildIdx, mid + 1, rc, left, right);
        return ans;
    }

    int queryMax(int node, int lc, int rc, int left, int right) {
        if (left <= lc && rc <= right) return segTrees[node].max;
        lazyCreate(node);
        pushdown(node, rc - lc + 1);
        int mid = lc + rc >> 1, ans = 0;
        if (left <= mid) ans = queryMax(segTrees[node].leftChildIdx, lc, mid, left, right);
        if (right > mid) ans = Math.max(ans, queryMax(segTrees[node].rightChildIdx, mid + 1, rc, left, right));
        return ans;
    }

    void lazyCreate(int node) {
        if (segTrees[node] == null) segTrees[node] = new SegTree();
        if (segTrees[node].leftChildIdx == 0) {
            segTrees[node].leftChildIdx = ++cnt;
            segTrees[segTrees[node].leftChildIdx] = new SegTree();
        }
        if (segTrees[node].rightChildIdx == 0) {
            segTrees[node].rightChildIdx = ++cnt;
            segTrees[segTrees[node].rightChildIdx] = new SegTree();
        }
    }

    void pushdown(int node, int len) {
        segTrees[segTrees[node].leftChildIdx].add += segTrees[node].add;
        segTrees[segTrees[node].rightChildIdx].add += segTrees[node].add;
        segTrees[segTrees[node].leftChildIdx].ct += (len - len / 2) * segTrees[node].add;
        segTrees[segTrees[node].rightChildIdx].ct += len / 2 * segTrees[node].add;
        segTrees[segTrees[node].leftChildIdx].max += segTrees[node].add;
        segTrees[segTrees[node].rightChildIdx].max += segTrees[node].add;
        segTrees[node].add = 0;
    }

    void pushup(int node) {
        segTrees[node].ct = segTrees[segTrees[node].leftChildIdx].ct + segTrees[segTrees[node].rightChildIdx].ct;
        segTrees[node].max = Math.max(segTrees[segTrees[node].leftChildIdx].max, segTrees[segTrees[node].rightChildIdx].max);
    }

}
