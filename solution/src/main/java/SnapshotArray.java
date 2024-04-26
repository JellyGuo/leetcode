import java.util.ArrayList;
import java.util.List;

//1146. 快照数组
public class SnapshotArray {
    private int snap_cnt;
    private List<int[]>[] data;

    public SnapshotArray(int length) {
        snap_cnt = 0;
        data = new List[length];
        for (int i = 0; i < length; i++) {
            data[i] = new ArrayList<>();
            data[i].add(new int[]{0, 0});
        }
    }

    public void set(int index, int val) {
        data[index].add(new int[]{snap_cnt, val});
    }

    public int snap() {
        return snap_cnt++;
    }

    public int get(int index, int snap_id) {
        return binarySearch(data[index], snap_id);
    }

    private int binarySearch(List<int[]> list, int snap_id) {
        if (list.size() == 0) return 0;
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (list.get(mid)[0] <= snap_id) l = mid;
            else r = mid - 1;
        }
        return list.get(l)[1];
    }

}
