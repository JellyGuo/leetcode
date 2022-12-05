public class SegTree {
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
