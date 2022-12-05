import java.util.*;

//307. 区域和检索 - 数组可修改
// 树状数组
public class NumArrayTwoBIT {
    int[] trees;
    int[] nums;
    int n;

    public NumArrayTwoBIT(int[] nums) {
        this.nums = nums;
        this.n = nums.length;
        // 下标从1开始
        trees = new int[n + 1];
        for (int i = 0; i < n; i++) {
            add(i + 1, nums[i]);
        }
    }

    public void update(int index, int val) {
        add(index + 1, val - nums[index]);
        nums[index] = val;
    }

    public int sumRange(int left, int right) {
        return query(right + 1) - query(left);
    }
    // 110 111 6-7
    // 100 110 4-6
    // 000 100 0-4
    // num中(0,4]的数只存tree(0,4]中，对于区间内也根据每一项求lowbit存
    // 1 存 1 10 和 100 3位
    // 2 存 10 100 2位
    // 3 存 11 1位
    // 查询从idx开始，eg[1,2] tree[2]-tree[0] 查tree[2] 即可(包含1、2，-lowbit=0)
    // 更新值更新小区间和小区间所在的大区间

    private int lowbit(int x) {
        return x & (-x);
    }

    // [idx,n]都加val
    private void add(int index, int val) {
        for (int i = index; i <= n; i += lowbit(i)) {
            trees[i] += val;
        }
    }

    //[1,idx]的和
    private int query(int index) {
        int ans = 0;
        for (int i = index; i > 0; i -= lowbit(i)) {
            ans += trees[i];
        }
        return ans;
    }

    //315. 计算右侧小于当前元素的个数 树状数组应用

    private int[] c;
    private int[] a;

    public List<Integer> countSmaller(int[] nums) {
        List<Integer> resultList = new ArrayList<Integer>();
        discretization(nums);
        init(nums.length + 5);
        for (int i = nums.length - 1; i >= 0; --i) {
            int id = getId(nums[i]);
            resultList.add(query(id - 1));
            update(id);
        }
        Collections.reverse(resultList);
        return resultList;
    }

    private void init(int length) {
        c = new int[length];
        Arrays.fill(c, 0);
    }

    private int lowBit(int x) {
        return x & (-x);
    }

    private void update(int pos) {
        while (pos < c.length) {
            c[pos] += 1;
            pos += lowBit(pos);
        }
    }

    private int query1(int pos) {
        int ret = 0;
        while (pos > 0) {
            ret += c[pos];
            pos -= lowBit(pos);
        }

        return ret;
    }

    private void discretization(int[] nums) {
        Set<Integer> set = new HashSet<Integer>();
        for (int num : nums) {
            set.add(num);
        }
        int size = set.size();
        a = new int[size];
        int index = 0;
        for (int num : set) {
            a[index++] = num;
        }
        Arrays.sort(a);
    }

    private int getId(int x) {
        return Arrays.binarySearch(a, x) + 1;
    }

}
