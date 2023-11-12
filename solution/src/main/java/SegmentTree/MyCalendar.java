package SegmentTree;

import java.util.Map;
import java.util.TreeMap;

// 729 我的日程安排表1
//https://leetcode.cn/problems/my-calendar-i/solutions/1646079/by-lfool-xvpv/
public class MyCalendar {
    Map<Integer, Integer> map;

    public MyCalendar() {
        map = new TreeMap<>();
    }

    // 差分 插旗法
    public boolean book(int start, int end) {
        map.put(start, map.getOrDefault(start, 0) + 1);
        map.put(end, map.getOrDefault(end, 0) - 1);
        int concurrent = 0;
        for (int v : map.values()) {
            concurrent += v;
            if (concurrent > 1) {
                map.put(start, map.get(start) - 1);
                map.put(end, map.get(end) + 1);
                if (map.get(start) == 0) map.remove(start);
                if (map.get(end) == 0) map.remove(end);
                return false;
            }
        }
        return true;
    }

    //--------------------------------线段树-----------------------------------------
    SegTree st = new SegTree(0, (int) 1e9 - 1, 0);

    public boolean book2(int start, int end) {
        if (canBook(start, end - 1, st)) {
            addBooking(start, end - 1, st);
            return true;
        }
        return false;
    }

    public void addBooking(int start, int end, SegTree tree) {
        int l = tree.l, r = tree.r, mid = (l + r) / 2;
        if (l == start && end == r) {
            tree.plus++;
            if (l == r) {
                return;
            }
            if (tree.left != null) {
                addBooking(l, mid, tree.left);
            }
            if (tree.right != null) {
                addBooking(mid + 1, r, tree.right);
            }
        } else if (end <= mid) {
            addBooking(start, end, tree.left);
        } else if (start > mid) {
            addBooking(start, end, tree.right);
        } else {
            addBooking(start, mid, tree.left);
            addBooking(mid + 1, end, tree.right);
        }
    }

    public boolean canBook(int start, int end, SegTree tree) {
        //注意，必须在所有子分段上都返回true后才能在自己的分段上加1，先遍历判断，为true的话的再加
        if (tree.plus >= 1) {// 731 >=2
            return false;
        }
        int l = tree.l, r = tree.r, mid = (l + r) / 2;
        if (start == l && end == r) {
            return (tree.left == null || canBook(l, mid, tree.left)) && (tree.right == null || canBook(mid + 1, r, tree.right));
        } else if (end <= mid) {
            //只需要检查左侧所有有交集的区间
            if (tree.left == null) {
                tree.left = new SegTree(l, mid, tree.plus);
            }
            return canBook(start, end, tree.left);
        } else if (start > mid) {
            //只需要检查右侧所有有交集的区间
            if (tree.right == null) {
                tree.right = new SegTree(mid + 1, r, tree.plus);
            }
            return canBook(start, end, tree.right);
        } else {
            if (tree.left == null) {
                tree.left = new SegTree(l, mid, tree.plus);
            }
            if (tree.right == null) {
                tree.right = new SegTree(mid + 1, r, tree.plus);
            }
            return canBook(start, mid, tree.left) && canBook(mid + 1, end, tree.right);
        }
    }
}
