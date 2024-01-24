package SegmentTree;


public class NumArrayTwoSegmentTree {
    // 线段树 1.数组存法(只维护数组中的位置，区间作为方法入参传递)
    int[] segmentTree;
    int n;

    // 2 数组存法 实体维护区间和sum的值
    SegTree[] segTrees;
    int[] nums;

    public NumArrayTwoSegmentTree(int[] nums) {
        this.n = nums.length;
        this.nums = nums;
        segmentTree = new int[n * 4];
        build(0, 0, n - 1, nums);

        segTrees = new SegTree[n * 4];
        build(1, 1, n);
        for (int i = 0; i < n; i++) change(1, i + 1, nums[i]);
    }

    private void build(int node, int l, int r, int[] nums) {
        if (l == r) {
            segmentTree[node] = nums[l];
            return;
        }
        int mid = (l + r) / 2;
        build(node * 2 + 1, l, mid, nums);
        build(node * 2 + 2, mid + 1, r, nums);
        segmentTree[node] = segmentTree[node * 2 + 1] + segmentTree[node * 2 + 2];
    }

    private void build(int node, int l, int r) {
        segTrees[node] = new SegTree(l, r);
        if (l == r) return;
        int mid = (l + r) >> 1;
        build(node << 1, l, mid);
        build(node << 1 | 1, mid + 1, r);
    }

    public void update(int index, int val) {
        change(0, 0, n - 1, index, val);

        //------------------
        change(1, index + 1, val - nums[index]);
        nums[index] = val;
    }

    private void change(int node, int l, int r, int index, int val) {
        if (l == r) {
            segmentTree[node] = val;
            return;
        }
        int mid = (l + r) / 2;
        if (index <= mid) {
            change(node * 2 + 1, l, mid, index, val);
        } else {
            change(node * 2 + 2, mid + 1, r, index, val);
        }
        segmentTree[node] = segmentTree[node * 2 + 1] + segmentTree[node * 2 + 2];
    }

    private void change(int node, int index, int val) {
        if (segTrees[node].l == index && segTrees[node].r == index) {
            segTrees[node].plus += val;
            return;
        }
        int mid = segTrees[node].l + segTrees[node].r >> 1;
        if (index <= mid) {
            change(node << 1, index, val);
        } else {
            change(node << 1 | 1, index, val);
        }
        pushUp(node);
    }

    public int sumRange(int left, int right) {
        range(0, 0, n - 1, left, right);
        return range(1, left + 1, right + 1);
    }

    private int range(int node, int l, int r, int left, int right) {
        if (l == left && r == right) {
            return segmentTree[node];
        }
        int mid = (l + r) / 2;
        if (right <= mid) {
            return range(node * 2 + 1, l, mid, left, right);
        } else if (left > mid) {
            return range(node * 2 + 2, mid + 1, r, left, right);
        } else {
            return range(node * 2 + 1, l, mid, left, mid) + range(node * 2 + 2, mid + 1, r, mid + 1, right);
        }
    }


    private void pushUp(int node) {
        segTrees[node].plus = segTrees[node << 1].plus + segTrees[node << 1 | 1].plus;
    }


    private int range(int node, int left, int right) {
        if (left <= segTrees[node].l && segTrees[node].r <= right) {
            return segTrees[node].plus;
        }
        int mid = segTrees[node].l + segTrees[node].r >> 1;
        int ans = 0;
        if (left <= mid) ans += range(node << 1, left, right);
        if (right > mid) ans += range(node << 1 | 1, left, right);
        return ans;
    }
}
