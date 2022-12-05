package encrypt;

class MyCalendar {

    SegTree[] segTrees;
    int N = (int) 1e9, M = 120010, cnt = 1;

    public MyCalendar() {
        segTrees = new SegTree[M];

    }

    public boolean book(int start, int end) {
        if (queryCt(1, 1, N + 1, start + 1, end) > 0) return false;
        updateCt(1, 1, N + 1, start + 1, end, 1);
        return true;
    }

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

    int queryCt(int node, int lc, int rc, int left, int right) {
        if (left <= lc && rc <= right) return segTrees[node].ct;
        lazyCreate(node);
        pushdown(node, rc - lc + 1);
        int mid = lc + rc >> 1, ans = 0;
        if (left <= mid) ans += queryCt(segTrees[node].leftChildIdx, lc, mid, left, right);
        if (right > mid) ans += queryCt(segTrees[node].rightChildIdx, mid + 1, rc, left, right);
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

    class SegTree {
        int l, r, plus;
        // 树存法 子节点
        SegTree left, right;
        // 动态开点数组存法子节点
        // 分别代表当前节点的左右子节点在tr的下标
        int leftChildIdx, rightChildIdx;
        // add 为懒标记
        int add;
        // ct代表当前节点有多少数
        int ct;
        // max当前节点区间的最大值
        int max;

        public SegTree(int l, int r, int plus) {
            this.l = l;
            this.r = r;
            this.plus = plus;
        }

        public SegTree(int l, int r) {
            this.l = l;
            this.r = r;
        }

        public SegTree() {
        }

    }
}

/**
 * Your MyCalendar object will be instantiated and called as such:
 * MyCalendar obj = new MyCalendar();
 * boolean param_1 = obj.book(start,end);
 */