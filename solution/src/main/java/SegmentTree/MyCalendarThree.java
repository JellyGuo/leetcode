package SegmentTree;

import java.util.TreeMap;

//732
public class MyCalendarThree {
    TreeMap<Integer, Integer> calendar;

    public MyCalendarThree() {
        calendar = new TreeMap<>();
    }

    public int book(int start, int end) {
        calendar.put(start, calendar.getOrDefault(start, 0) + 1);
        calendar.put(end, calendar.getOrDefault(end, 0) - 1);
        int concurrent = 0, ans = 0;
        for (int v : calendar.values()) {
            concurrent += v;
            ans = Math.max(concurrent, ans);
        }
        return ans;
    }
}
