import java.util.*;

public class SolutionSort {

    //region---------------------------------------------排序-----------------------------------------------
    private void reverse(int[] nums, int left, int right) {
        int a = left, b = right;
        while (a < b) {
            swap(nums, a++, b--);
        }
    }

    private void swap(int[] nums, int k, int m) {
        int temp = nums[k];
        nums[k] = nums[m];
        nums[m] = temp;
    }
    //region -------------------------------------------------堆排序/快速选择/桶排序-------------------------------------------
// offer 54 BST的第k大节点
//给定一棵二叉搜索树，请找出其中第 k 大的节点的值。
    int reskthLargest, kthLargest;

    public int kthLargest(TreeNode root, int k) {
        this.kthLargest = k;
        dfskthLargest(root);
        return reskthLargest;
    }

    private void dfskthLargest(TreeNode node) {
        if (node == null) return;
        dfskthLargest(node.right);
        if (kthLargest == 0) return;
        if (--kthLargest == 0) reskthLargest = node.val;
        dfskthLargest(node.left);
    }

    // 215 数组中的第K个最大元素
    //快排-》快速选择
    public int findKthLargest(int[] nums, int k) {
        int length = nums.length;
        int left = 0;
        int right = nums.length - 1;
        int target = nums.length - k;
        while (true) {
            int index = partition(nums, left, right);
            if (index == target) {
                return nums[index];
            } else if (index < target) {
                left = index + 1;
            } else {
                right = index - 1;
            }
        }
    }

    private int partition(int[] nums, int l, int r) {
        int pivot = nums[l];
        while (l < r) {
            while (l < r && nums[r] >= pivot) {
                r--;
            }
            nums[l] = nums[r];
            while (l < r && nums[l] <= pivot) {
                l++;
            }
            nums[r] = nums[l];
        }
        nums[l] = pivot;
        return l;
    }

    private int partition2(int[] nums, int left, int right) {
        int pivot = nums[left];
        int j = left;
        for (int i = left + 1; i <= right; i++) {
            if (nums[i] < pivot) {
                j++;
                swap(nums, j, i);
            }
        }
        swap(nums, j, left);
        return j;
    }

    //堆排序
    public int findKthLargestMaxHeap(int[] nums, int k) {
        int length = nums.length;
        buildMaxHeap(nums, length);
        for (int i = nums.length - 1; i >= nums.length - k + 1; i--) {
            swap(nums, i, 0);
            length--;
            maxheapify(nums, 0, length);
        }
        return nums[0];
    }

    public int findKthLargestMinHeap(int[] nums, int k) {
        int max = 0;
        buildMinheap(nums, k);
        for (int i = k; i < nums.length; i++) {
            if (nums[i] > nums[0]) {
                swap(nums, 0, i);
                minheapify(nums, 0, k);
            }
        }
        return nums[0];
    }

    private void buildMinheap(int[] nums, int heapsize) {
        for (int i = (heapsize >> 1) - 1; i >= 0; i--) {
            minheapify(nums, i, heapsize);
        }
    }

    private void minheapify(int[] nums, int i, int heapsize) {
        int l = 2 * i + 1, r = 2 * i + 2;
        int min = i;
        if (l < heapsize && nums[l] < nums[min]) {
            min = l;
        }
        if (r < heapsize && nums[r] < nums[min]) {
            min = r;
        }
        if (min != i) {
            swap(nums, min, i);
            minheapify(nums, min, heapsize);
        }
    }


    private void buildMaxHeap(int[] nums, int heapsize) {
        for (int i = heapsize / 2 - 1; i >= 0; i--) {
            maxheapify(nums, i, heapsize);
        }
    }

    private void maxheapify(int[] nums, int i, int heapsize) {
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        int largest = i;
        if (l < heapsize && nums[l] > nums[largest]) {
            largest = l;
        }
        if (r < heapsize && nums[r] > nums[largest]) {
            largest = r;
        }
        if (largest != i) {
            swap(nums, largest, i);
            maxheapify(nums, largest, heapsize);
        }
    }

    // 347 前K个高频元素
    //给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。
//输入: nums = [1,1,1,2,2,3], k = 2
//输出: [1,2]
    public int[] topKFrequentHeapSort(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        int[][] array = new int[2][map.size()];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            array[0][i] = entry.getKey();
            array[1][i] = entry.getValue();
            i++;
        }
        buildMinHeap(array, k);
        for (int j = k; j < map.size(); j++) {
            if (array[1][j] > array[1][0]) {
                swap(array, j, 0);
                minheapify(array, 0, k);
            }
        }
        int[] result = new int[k];
        System.arraycopy(array[0], 0, result, 0, k);
        return result;
    }

    public void buildMinHeap(int[][] nums, int heapsize) {
        for (int i = ((heapsize >>> 1) - 1); i >= 0; i--) {
            minheapify(nums, i, heapsize);
        }
    }

    public void minheapify(int[][] nums, int i, int heapsize) {
        int l = 2 * i + 1, r = 2 * i + 2, min = i;
        if (l < heapsize && nums[1][l] < nums[1][min]) {
            min = l;
        }
        if (r < heapsize && nums[1][r] < nums[1][min]) {
            min = r;
        }
        if (min != i) {
            swap(nums, min, i);
            minheapify(nums, min, heapsize);
        }
    }

    private void swap(int[][] nums, int p, int q) {
        int tmp = nums[0][p];
        nums[0][p] = nums[0][q];
        nums[0][q] = tmp;
        int tmp2 = nums[1][p];
        nums[1][p] = nums[1][q];
        nums[1][q] = tmp2;
    }

    public int[] topKFrequentQuickSelect(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        int[][] array = new int[2][map.size()];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            array[0][i] = entry.getKey();
            array[1][i] = entry.getValue();
            i++;
        }
        int[] result = new int[k];

        int target = map.size() - k;
        int left = 0, right = map.size() - 1;
        while (true) {
            int idx = partitionId(array, left, right);
            if (idx == target) {
                for (int j = target; j < map.size(); j++) {
                    result[j - target] = array[0][j];
                }
                break;
            } else if (idx < target) {
                left = idx + 1;
            } else {
                right = idx - 1;
            }
        }
        return result;
    }

    public int partitionId(int[][] nums, int left, int right) {
        int pivot_0 = nums[0][left];
        int pivot = nums[1][left];
        int l = left, r = right;
        while (l < r) {
            while (r > l && nums[1][r] >= pivot) {
                r--;
            }
            swap(nums, l, r);
            while (l < r && nums[1][l] <= pivot) {
                l++;
            }
            swap(nums, l, r);
        }
        nums[0][l] = pivot_0;
        nums[1][l] = pivot;
        return l;
    }

    //桶排序
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        List<Integer>[] array = new ArrayList[nums.length + 1];
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (array[entry.getValue()] == null) {
                array[entry.getValue()] = new ArrayList<>();
            }
            array[entry.getValue()].add(entry.getKey());
        }
        List<Integer> result = new ArrayList<>();
        for (int i = nums.length; i >= 0 && result.size() < k; i--) {
            if (array[i] == null) continue;
            if (array[i].size() <= k) result.addAll(array[i]);
        }
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    //最小k个数  快速选择
    public int[] getLeastNumbers(int[] arr, int k) {
        if (arr.length == 0 || k == 0) return new int[0];
        int left = 0, right = arr.length - 1;
        while (true) {
            int idx = quickSelect(arr, left, right);
            if (idx == k - 1) {
                return Arrays.copyOf(arr, k);
            } else if (idx < k) {
                left = idx + 1;
            } else {
                right = idx - 1;
            }
        }
    }

    private int quickSelect(int[] arr, int left, int right) {
        int l = left, r = right;
        int pivot = arr[l];
        while (l < r) {
            while (r > l && arr[r] >= pivot) {
                r--;
            }
            arr[l] = arr[r];
            while (l < r && arr[l] <= pivot) {
                l++;
            }
            arr[r] = arr[l];
        }
        arr[l] = pivot;
        return l;
    }

    //优先队列
    public int[] getLeastNumbersPriorityQueue(int[] arr, int k) {
        if (arr.length == 0 || k == 0) return new int[0];
        PriorityQueue<Integer> queue = new PriorityQueue<>((o1, o2) -> o2 - o1);
        for (int i = 0; i < k; i++) {
            queue.offer(arr[i]);
        }
        for (int i = k; i < arr.length; i++) {
            if (arr[i] < queue.peek()) {
                queue.poll();
                queue.offer(arr[i]);
            }
        }
        int[] res = new int[k];
        for (int i = 0; i < k; i++) {
            res[i] = queue.poll();
        }
        return res;
    }

    //堆排序
    public int[] getLeastNumbersHeap(int[] arr, int k) {
        buildMaxHeap(arr, k);
        int[] res = new int[k];
        for (int i = k; i < arr.length; i++) {
            if (arr[i] < arr[0]) {
                swap(arr, 0, i);
                maxheapify(arr, 0, k);
            }
        }
        return Arrays.copyOfRange(arr, 0, k);
    }

    // 263丑数
    public boolean isUgly(int n) {
        if (n <= 0) return false;
        while (n % 5 == 0) {
            n /= 5;
        }
        while (n % 3 == 0) {
            n /= 3;
        }
        while (n % 2 == 0) {
            n /= 2;
        }
        return n == 1;
    }

    // 264 丑数
    // dp做法搜nthUglyNumber
    public int nthUglyNumberPriorityQueue(int n) {
        PriorityQueue<Long> priorityQueue = new PriorityQueue<>();
        int[] nums = new int[]{2, 3, 5};
        priorityQueue.offer(1L);
        Set<Long> set = new HashSet<>();
        set.add(1L);
        long ans = 0;
        while (n-- > 0 && !priorityQueue.isEmpty()) {
            ans = priorityQueue.poll();
            for (int num : nums) {
                if (!set.contains(ans * num)) {
                    priorityQueue.offer(ans * num);
                    set.add(ans * num);
                }
            }
        }
        return (int) ans;
    }

    //313. 超级丑数
    //假设只有2，3，5 3个prime 让任何数只有1种生成路径 对于任何数x = 2^i * 3^j * 5^k
    // 定义一种唯一的生成路径： 1，只要i不为0，都由 2^(i-1) * 3^j * 5^k 生成x
    // 2，当i为0时，只要j不为0， 都由3^(j-1) * 5^k 生成x
    // 3, 当i,j 都为0时，由 5^(k-1) 生成x
    //这样生成出来的x，只有唯一一个前驱，不会重复生成
    public int nthSuperUglyNumberPriorityQueue(int n, int[] primes) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(1);
        while (n-- > 0) {
            int cur = pq.poll();
            if (n == 0) return cur;
            List<String> ls = new ArrayList<>();
            for (int k : primes) {
                if (k <= Integer.MAX_VALUE / cur) pq.offer(k * cur);
                ls.add(String.valueOf(k * cur));
                if (cur % k == 0) break;
            }
            System.out.println("cur:" + cur + " add queue" + String.join(",", ls));
        }
        return -1;
    }

    // 多路归并
    public int nthSuperUglyNumber(int n, int[] primes) {
        int m = primes.length;
        PriorityQueue<int[]> q = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        for (int i = 0; i < m; i++) {
            q.add(new int[]{primes[i], i, 0});
        }
        int[] ans = new int[n];
        ans[0] = 1;
        for (int j = 1; j < n; ) {
            int[] poll = q.poll();
            int val = poll[0], i = poll[1], idx = poll[2];
            if (val != ans[j - 1]) ans[j++] = val;
            q.add(new int[]{ans[idx + 1] * primes[i], i, idx + 1});
        }
        return ans[n - 1];
    }

    // 373 查找和最小的K对数字
    // 状态压缩
    // 二分法查kSmallestPairsBinarySearch
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> (nums1[o[0]] + nums2[o[1]])));
        for (int i = 0; i < Math.min(k, nums1.length); i++) {
            priorityQueue.offer(new int[]{i, 0});
        }
        //数量小于k，priorityQueue的size小于k 提前变为空
        while (k-- > 0 && !priorityQueue.isEmpty()) {
            int[] pos = priorityQueue.poll();
            ans.add(Arrays.asList(nums1[pos[0]], nums2[pos[1]]));
            if (++pos[1] < nums2.length) {
                priorityQueue.offer(pos);
            }
        }
        return ans;
    }

    // 378 有序矩阵中第K小的元素
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o[0]));
        for (int i = 0; i < n; i++) {
            priorityQueue.offer(new int[]{matrix[0][i], 0, i});
        }
        int ans = 0;
        while (k-- > 0 && !priorityQueue.isEmpty()) {
            int[] tmp = priorityQueue.poll();
            ans = tmp[0];
            if (tmp[1] != n - 1) {
                tmp[0] = matrix[tmp[1] + 1][tmp[2]];
                tmp[1] = tmp[1] + 1;
                priorityQueue.offer(tmp);
            }
        }
        return ans;
    }

    //1439. 有序矩阵中的第 k 个最小数组和
    //二分+双指针做法搜kthSmallest1439binarySearch
    public int kthSmallest1439(int[][] mat, int k) {
        int m = mat.length;
        int[] prev = mat[0];
        for (int i = 1; i < m; ++i) {
            prev = merge(prev, mat[i], k);
        }
        return prev[k - 1];
    }

    public int[] merge(int[] f, int[] g, int k) {
        if (g.length > f.length) {
            return merge(g, f, k);
        }

        PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> a[2] - b[2]);
        for (int i = 0; i < g.length; ++i) {
            pq.offer(new int[]{0, i, f[0] + g[i]});
        }

        List<Integer> list = new ArrayList<Integer>();
        while (k > 0 && !pq.isEmpty()) {
            int[] entry = pq.poll();
            list.add(entry[2]);
            if (entry[0] + 1 < f.length) {
                pq.offer(new int[]{entry[0] + 1, entry[1], f[entry[0] + 1] + g[entry[1]]});
            }
            --k;
        }

        int[] ans = new int[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            ans[i] = list.get(i);
        }
        return ans;
    }

    // 937 重新排列日志文件
    // 自定义排序
    public String[] reorderLogFiles(String[] logs) {
        List<Log> list = new ArrayList<>();
        for (int i = 0; i < logs.length; i++) {
            list.add(new Log(i, logs[i]));
        }
        list.sort((o1, o2) -> {
            if (o1.type != o2.type) return o1.type - o2.type;
            if (o1.type == 1) return o1.idx - o2.idx;
            if (!o1.content.equals(o2.content)) return o1.content.compareTo(o2.content);
            return o1.sign.compareTo(o2.sign);
        });
        String[] result = new String[list.size()];
        int idx = 0;
        for (Log log : list) {
            result[idx++] = log.origin;
        }
        return result;
    }

    static class Log {
        int idx, type;
        String sign, content, origin;

        public Log(int _idx, String s) {
            this.idx = _idx;
            int n = s.length(), i = 0;
            while (i < n && s.charAt(i) != ' ') i++;
            sign = s.substring(0, i);
            content = s.substring(i + 1);
            origin = s;
            type = Character.isDigit(content.charAt(0)) ? 1 : 0;

        }
    }

    //1630. 等差子数组
    public List<Boolean> checkArithmeticSubarrays(int[] nums, int[] l, int[] r) {
        int n = nums.length, m = l.length;
        List<Boolean> ans = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int left = l[i], right = r[i];
            int min = nums[left], max = nums[left];
            for (int j = left + 1; j <= right; j++) {
                min = Math.min(min, nums[j]);
                max = Math.max(max, nums[j]);
            }
            if (max - min == 0) {
                ans.add(true);
                continue;
            }
            if ((max - min) % (right - left) != 0) {
                ans.add(false);
                continue;
            }
            int d = (max - min) / (right - left);
            boolean[] seen = new boolean[right - left + 1];
            boolean flag = true;
            for (int j = left; j <= right; j++) {
                if ((nums[j] - min) % d != 0) {
                    flag = false;
                    break;
                }
                int t = (nums[j] - min) / d;
                if (seen[t]) {
                    flag = false;
                    break;
                }
                seen[t] = true;
            }
            ans.add(flag);
        }
        return ans;
    }

    //2530. 执行 K 次操作后的最大分数
    public long maxKelements(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((o1, o2) -> o2 - o1);
        for (int num : nums) {
            pq.offer(num);
        }
        long score = 0;
        while (k-- > 0 && !pq.isEmpty()) {
            int num = pq.poll();
            score += num;
            pq.offer((num + 3 - 1) / 3);
        }
        return score;
    }

    //2545. 根据第 K 场考试的分数排序
    public int[][] sortTheStudents(int[][] score, int k) {
        Arrays.sort(score, (o1, o2) -> o2[k] - o1[k]);
        return score;
    }

    //endregion--------------------------------------------------------------------
    // 简单选择排序
    public void selectSort(int[] arr) {
        //每次从剩下的元素中选择最小值放到第一个位置
        for (int i = 0; i < arr.length - 1; i++) {
            //记录每一趟最小值坐标
            int min = i;
            //寻找每一趟的最小值 先找到坐标 最后再进行交换 减少交换次数
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            //元素交换
            if (min != i) {
                int temp = arr[min];
                arr[min] = arr[i];
                arr[i] = temp;
            }
        }
    }

    //插入排序
    public int[] insertionSort(int[] array) {
        if (array.length == 0)
            return array;
        int current;
        for (int i = 0; i < array.length - 1; i++) {
            current = array[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
        return array;
    }

    public int[] insertionSort2(int[] array) {
        if (array.length == 0)
            return array;
        int current;
        for (int i = 1; i < array.length; i++) {
            current = array[i];
            int preIndex = i - 1;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
        return array;
    }

    // 希尔排序 针对有序序列在插入时采用交换法
    public static void sort(int[] arr) {
        //增量gap，并逐步缩小增量
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                while (j - gap >= 0 && arr[j] < arr[j - gap]) {
                    //插入排序采用交换法
                    swap1(arr, j, j - gap);
                    j -= gap;
                }
            }
        }
    }

    // 希尔排序 针对有序序列在插入时采用移动法。
    public static void sort1(int[] arr) {
        //增量gap，并逐步缩小增量
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                int temp = arr[j];
                if (arr[j] < arr[j - gap]) {
                    while (j - gap >= 0 && temp < arr[j - gap]) {
                        //移动法
                        arr[j] = arr[j - gap];
                        j -= gap;
                    }
                    arr[j] = temp;
                }
            }
        }
    }

    public static void swap1(int[] arr, int a, int b) {
        arr[a] = arr[a] + arr[b];
        arr[b] = arr[a] - arr[b];
        arr[a] = arr[a] - arr[b];
    }

    /**
     * 归并排序
     */
    public int[] MergeSort(int[] array) {
        if (array.length < 2) return array;
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(MergeSort(left), MergeSort(right));
    }

    private int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length)
                result[index] = right[j++];
            else if (j >= right.length)
                result[index] = left[i++];
            else if (left[i] > right[j])
                result[index] = right[j++];
            else
                result[index] = left[i++];
        }
        return result;
    }

    /**
     * @param arr        待排序列
     * @param leftIndex  待排序列起始位置
     * @param rightIndex 待排序列结束位置
     */
    public void quickSort(int[] arr, int leftIndex, int rightIndex) {
        if (leftIndex >= rightIndex) {
            return;
        }

        int left = leftIndex;
        int right = rightIndex;
        //待排序的第一个元素作为基准值
        int key = arr[left];

        //从左右两边交替扫描，直到left = right
        while (left < right) {
            while (right > left && arr[right] >= key) {
                //从右往左扫描，找到第一个比基准值小的元素
                right--;
            }

            //找到这种元素将arr[right]放入arr[left]中
            arr[left] = arr[right];

            while (left < right && arr[left] <= key) {
                //从左往右扫描，找到第一个比基准值大的元素
                left++;
            }

            //找到这种元素将arr[left]放入arr[right]中
            arr[right] = arr[left];
        }
        //基准值归位
        arr[left] = key;
        //对基准值左边的元素进行递归排序
        quickSort(arr, leftIndex, left - 1);
        //对基准值右边的元素进行递归排序。
        quickSort(arr, right + 1, rightIndex);
    }

    //贪心+快排模板 offer 45
    public String minNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }
        quickSort(strs, 0, strs.length - 1);
        return String.join("", strs);
    }

    private void quickSort(String[] strings, int left, int right) {
        if (left >= right) return;
        int middle = quickSelect(strings, left, right);
        quickSort(strings, left, middle - 1);
        quickSort(strings, middle + 1, right);
    }

    private int quickSelect(String[] strings, int left, int right) {
        String pivot = strings[left];
        while (left < right) {
            while (left < right && (strings[right] + pivot).compareTo(pivot + strings[right]) >= 0) {
                right--;
            }
            strings[left] = strings[right];
            while (left < right && (strings[left] + pivot).compareTo(pivot + strings[left]) <= 0) {
                left++;
            }
            strings[right] = strings[left];
        }
        strings[left] = pivot;
        return left;
    }

    //327. 区间和的个数
    public int countRangeSum(int[] nums, int lower, int upper) {
        int n = nums.length;
        long[] sum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        // 这里sum区间含第一位0
        // 同时规避了[0],[0,0] {0,0}的进入次数问题，保证单个元素可以进入一次
        return mergeSortCountRangeSum(sum, lower, upper, 0,  n);
    }

    private int mergeSortCountRangeSum(long[] sum, int lower, int upper, int left, int right) {
        if (left == right) return 0;
        // 整体下标偏左
        int mid = left + right >> 1;
        int leftCnt = mergeSortCountRangeSum(sum, lower, upper, left, mid);
        int rightCnt = mergeSortCountRangeSum(sum, lower, upper, mid + 1, right);
        int res = leftCnt + rightCnt;

        int idx = left;
        int l = mid + 1, r = mid + 1;
        while (idx <= mid) {
            // 由于left从0开始，因此这里减到idx开始即可，不需要再找前一位
            while (l <= right && sum[l] - sum[idx] < lower) {
                l++;
            }
            while (r <= right && sum[r] - sum[idx] <= upper) {
                r++;
            }
            res += r - l;
            idx++;// 下一轮l,r分别从这一轮的位置开始，原因 nums1[idx+1]>nums1[idx],sum[idx+1]>sum[idx],所以sum[此时l之前的l]-sum[idx+1]肯定<lower
        }
        int p = 0;
        int p1 = left, p2 = mid + 1;
        long[] sorted = new long[right - left + 1];
        while (p1 <= mid || p2 <= right) {
            if (p1 > mid) {
                sorted[p++] = sum[p2++];
            } else if (p2 > right) {
                sorted[p++] = sum[p1++];
            } else if (sum[p1] < sum[p2]) {
                sorted[p++] = sum[p1++];
            } else {
                sorted[p++] = sum[p2++];
            }
        }
        for (int j = 0; j < sorted.length; j++) {
            sum[left + j] = sorted[j];
        }
        return res;
    }

    //1051. 高度检查器
    public int heightChecker(int[] heights) {
        int[] copy = Arrays.copyOfRange(heights, 0, heights.length);
        Arrays.sort(copy);
        int ans = 0;
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != copy[i]) {
                ans++;
            }
        }
        return ans;
    }

    // 桶排序计数
    public int heightCheckerCount(int[] heights) {
        // 值的范围是1 <= heights[i] <= 100，因此需要1,2,3,...,99,100，共101个桶
        int[] arr = new int[101];
        // 遍历数组heights，计算每个桶中有多少个元素，也就是数组heights中有多少个1，多少个2，。。。，多少个100
        // 将这101个桶中的元素，一个一个桶地取出来，元素就是有序的
        for (int height : heights) {
            arr[height]++;
        }

        int count = 0;
        for (int i = 1, j = 0; i < arr.length; i++) {
            // arr[i]，i就是桶中存放的元素的值，arr[i]是元素的个数
            // arr[i]-- 就是每次取出一个，一直取到没有元素，成为空桶
            while (arr[i]-- > 0) {
                // 从桶中取出元素时，元素的排列顺序就是非递减的，然后与heights中的元素比较，如果不同，计算器就加1
                if (heights[j++] != i) count++;
            }
        }
        return count;
    }

    //1637. 两点之间不包含任何点的最宽垂直区域
    public int maxWidthOfVerticalArea(int[][] points) {
        int n = points.length;
        Arrays.sort(points, (o1, o2) -> o1[0] - o2[0]);
        int max = 0;
        for (int i = 1; i < n; i++) {
            max = Math.max(max, points[i][0] - points[i - 1][0]);
        }
        return max;
    }

    //桶排序
    public int maxWidthOfVerticalArea2(int[][] points) {
        int n = points.length;
        int[] nums = new int[n];
        for (int i = 0; i < n; ++i) {
            nums[i] = points[i][0];
        }
        final int inf = 1 << 30;
        int mi = inf, mx = -inf;
        for (int v : nums) {
            mi = Math.min(mi, v);
            mx = Math.max(mx, v);
        }
        int bucketSize = Math.max(1, (mx - mi) / (n - 1));
        int bucketCount = (mx - mi) / bucketSize + 1;
        int[][] buckets = new int[bucketCount][2];
        for (int[] bucket : buckets) {
            bucket[0] = inf;
            bucket[1] = -inf;
        }
        for (int v : nums) {
            int i = (v - mi) / bucketSize;
            buckets[i][0] = Math.min(buckets[i][0], v);
            buckets[i][1] = Math.max(buckets[i][1], v);
        }
        int prev = inf;
        int ans = 0;
        for (int[] bucket : buckets) {
            if (bucket[0] > bucket[1]) {
                continue;
            }
            ans = Math.max(ans, bucket[0] - prev);
            prev = bucket[1];
        }
        return ans;
    }

    // 88 合并两个有序数组
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int idx = m + n - 1;
        int idx1 = m - 1, idx2 = n - 1;
        while (idx >= 0) {
            if (idx1 == -1) {
                nums1[idx--] = nums2[idx2--];
            } else if (idx2 == -1) {
                nums1[idx--] = nums1[idx1--];
            } else if (nums1[idx1] < nums2[idx2]) {
                nums1[idx--] = nums2[idx2--];
            } else {
                nums1[idx--] = nums1[idx1--];
            }
        }
    }

    // 164 最大间距
    // 基数排序
    public int maximumGap(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return 0;
        }
        long exp = 1;
        int[] buf = new int[n];
        int maxVal = Arrays.stream(nums).max().getAsInt();

        while (maxVal >= exp) {
            int[] cnt = new int[10];
            for (int i = 0; i < n; i++) {
                int digit = (nums[i] / (int) exp) % 10;
                cnt[digit]++;
            }
            for (int i = 1; i < 10; i++) {
                cnt[i] += cnt[i - 1];
            }
            for (int i = n - 1; i >= 0; i--) {
                int digit = (nums[i] / (int) exp) % 10;
                buf[cnt[digit] - 1] = nums[i];
                cnt[digit]--;
            }
            System.arraycopy(buf, 0, nums, 0, n);
            exp *= 10;
        }

        int ret = 0;
        for (int i = 1; i < n; i++) {
            ret = Math.max(ret, nums[i] - nums[i - 1]);
        }
        return ret;
    }

    //桶排序
    public int maximumGap2(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return 0;
        }
        int minVal = Arrays.stream(nums).min().getAsInt();
        int maxVal = Arrays.stream(nums).max().getAsInt();
        int d = Math.max(1, (maxVal - minVal) / (n - 1));
        int bucketSize = (maxVal - minVal) / d + 1;

        int[][] bucket = new int[bucketSize][2];
        for (int i = 0; i < bucketSize; ++i) {
            Arrays.fill(bucket[i], -1); // 存储 (桶内最小值，桶内最大值) 对， (-1, -1) 表示该桶是空的
        }
        for (int i = 0; i < n; i++) {
            int idx = (nums[i] - minVal) / d;
            if (bucket[idx][0] == -1) {
                bucket[idx][0] = bucket[idx][1] = nums[i];
            } else {
                bucket[idx][0] = Math.min(bucket[idx][0], nums[i]);
                bucket[idx][1] = Math.max(bucket[idx][1], nums[i]);
            }
        }

        int ret = 0;
        int prev = -1;
        for (int i = 0; i < bucketSize; i++) {
            if (bucket[i][0] == -1) {
                continue;
            }
            if (prev != -1) {
                ret = Math.max(ret, bucket[i][0] - bucket[prev][1]);
            }
            prev = i;
        }
        return ret;
    }

    // 324 摆动排序
//输入：nums = [1,5,1,1,6,4]
//输出：[1,6,1,5,1,4]
//解释：[1,4,1,5,1,6] 同样是符合题目要求的结果，可以被判题程序接受。
//输入：nums = [1,3,2,2,3,1]
//输出：[2,3,1,3,1,2]
    public void wiggleSort(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int[] arr = nums.clone();
        for (int i = 0, j = (n - 1) / 2, k = n - 1; i < n; i += 2) {
            nums[i] = arr[j--];
            if (i + 1 < n) {
                nums[i + 1] = arr[k--];
            }
        }
    }

    //O(n)
    public void wiggleSort2(int[] nums) {
        // 1. 找到中位数
        int n = nums.length;
        int median = getKthSmaller(nums, (n - 1) / 2);
        // 2. 三向切分
        for (int smallIdx = 0, idx = 0, largeIdx = n - 1; idx < largeIdx; idx++) {
            if (nums[idx] > median) {
                swap(nums, idx--, largeIdx--);
            } else if (nums[idx] < median) {
                swap(nums, idx, smallIdx++);
            }
        }
        // 3.倒序重组
        int[] arr = nums.clone();
        for (int i = 0, j = (n - 1) / 2, k = n - 1; i < n; i += 2, j--, k--) {
            nums[i] = arr[j];
            if (i + 1 < n) {
                nums[i + 1] = arr[k];
            }
        }
    }

    private int getKthSmaller(int[] nums, int k) {
        int left = 0, right = nums.length - 1;
        while (true) {
            int idx = partitionId(nums, left, right);
            if (idx == k) {
                return nums[idx];
            } else if (idx > k) {
                right = idx - 1;
            } else {
                left = idx + 1;
            }
        }
    }

    private int partitionId(int[] nums, int l, int r) {
        int left = l, right = r;
        int pivot = nums[left];
        while (left < right) {
            while (left < right && nums[right] >= pivot) {
                right--;
            }
            nums[left] = nums[right];
            while (left < right && nums[left] <= pivot) {
                left++;
            }
            nums[right] = nums[left];
        }
        nums[left] = pivot;
        return left;
    }

    // 三向切分 把小于中位数的放到前面，大于中位数的放到后面
    private void threeWayPartition(int[] nums, int median) {
        int l = 0, r = nums.length - 1, i = 0;
        // 类似3色问题
        while (i <= r) {
            if (nums[i] > median) {
                // 换完继续判断当前i
                swap(nums, r--, i);
            } else if (nums[i] < median) {
                // 和当前l一样，同时加1
                // 比l大，此时l指向的一定是median，换完继续往后移
                swap(nums, l++, i++);
            } else {
                i++;
            }
        }
    }

    // 三向切分+倒序重组
    private void threeWayPartition2(int[] nums, int median) {
        int l = 0, r = nums.length - 1, i = 0;
        // 类似3色问题
        while (i <= r) {
            if (nums[getIdx(i)] < median) {
                swap(nums, getIdx(r--), getIdx(i));
            } else if (nums[getIdx(i)] > median) {
                swap(nums, getIdx(l++), getIdx(i++));
            } else {
                i++;
            }
        }
    }

    public int getIdx(int i) {
        int n = 0;
        return (1 + 2 * (i)) % (n | 1);
    }

    // 406 根据身高重建队列
    public int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (p1, p2) -> p1[0] != p2[0] ? Integer.compare(p2[0], p1[0]) : Integer.compare(p1[1], p2[1]));
        List<int[]> list = new ArrayList<>();

        for (int[] ppl : people) list.add(ppl[1], ppl);
        return list.toArray(new int[people.length][]);
    }

    //2418. 按身高排序
    public String[] sortPeople(String[] names, int[] heights) {
        int n = names.length;
        int[][] arr = new int[n][2];
        for (int i = 0; i < n; i++) {
            arr[i] = new int[]{heights[i], i};
        }
        Arrays.sort(arr, (o1, o2) -> o2[0] - o1[0]);
        String[] ans = new String[n];
        for (int i = 0; i < n; i++) {
            ans[i] = names[arr[i][1]];
        }
        return ans;
    }


    //833. 字符串中的查找与替换
    public String findReplaceString(String s, int[] indices, String[] sources, String[] targets) {
        List<String> ls = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();
        for (int i = 0; i < indices.length; i++) {
            indicesList.add(i);
        }
        indicesList.sort(Comparator.comparingInt(o -> indices[o]));
        int last = 0;
        for (int i = 0; i < indicesList.size(); i++) {
            int pos =indicesList.get(i);
            int idx = indices[pos];
            if (last < idx) {
                ls.add(s.substring(last, idx));
                last = idx;
            }
            String source = sources[pos];
            String target = targets[pos];
            if (idx + source.length() > s.length()) continue;
            if (s.substring(idx, idx + source.length()).equals(source)) {
                ls.add(target);
                last = idx + source.length();
            }
        }
        if (last < s.length()) {
            ls.add(s.substring(last));
        }
        return String.join("", ls);
    }

    // 581 最短无序连续子数组  双指针
    //Order Check -> 检查数组中的数是否有序，返回错误排序的元素个数，ex 【1,1，3,4,1】return 3 因为3,4,1排序不对
    //给你一个整数数组 nums ，你需要找出一个 连续子数组 ，如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。
// 请你找出符合题意的 最短 子数组，并输出它的长度。
//输入：nums = [2,6,4,8,10,9,15]
//输出：5
//解释：你只需要对 [6, 4, 8, 10, 9] 进行升序排序，那么整个表都会变为升序排序。
//
    public int orderCheckOrfindUnsortedSubarray(int[] nums) {
        int n = nums.length;
        int[] arr = nums.clone();
        Arrays.sort(arr);
        int i = 0, j = n - 1;
        while (i <= j && nums[i] == arr[i]) i++;
        while (i <= j && nums[j] == arr[j]) j--;
        return j - i + 1;
    }

    int MIN = -100005, MAX = 100005;

    public int findUnsortedSubarray(int[] nums) {
        int n = nums.length;
        int i = 0, j = n - 1;
        while (i < j && nums[i] <= nums[i + 1]) i++;
        while (i < j && nums[j] >= nums[j - 1]) j--;
        int l = i, r = j;
        int min = nums[i], max = nums[j];
        for (int u = l; u <= r; u++) {
            // 1 3 5 4 2 8 6 7 9
            if (nums[u] < min) {
                //i从u往0找，找到第一个小于u的i1,从i1+1到u都要重新排列
                while (i >= 0 && nums[i] > nums[u]) i--;
                min = i >= 0 ? nums[i] : MIN;
            }
            //从j往后找到第一个比u大的，j-1的需要重新排列
            if (nums[u] > max) {
                while (j < n && nums[j] < nums[u]) j++;
                max = j < n ? nums[j] : MAX;
            }
        }
        return j == i ? 0 : (j - 1) - (i + 1) + 1;
    }

    // O(n)做法
    public int findUnsortedSubarray2(int[] nums) {
        int n = nums.length;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        // r从前往后找，l从后往前找
        int r = 0, l = n - 1;
        for (int i = 0; i < n; i++) {
            //max：从前往后的最大值,只要小于max的值，都应该被排列
            if (nums[i] < max) {
                r = i;
            } else {
                max = nums[i];
            }
            //min是从后往前的最小值，大于该值的都应该被排列
            if (nums[n - 1 - i] > min) {
                l = n - 1 - i;
            } else {
                min = nums[n - 1 - i];
            }
        }
        return r > l ? r - l + 1 : 0;
    }

    //1574. 删除最短的子数组使剩余数组有序
    //将数组分成三部分：原数组=开头非递减部分+中间被删除部分+末尾非递减部分，其中每一部分都可以为空
    //单独求一个开头非递减部分或末尾非递减部分都很好求，但问题是，开头非递减部分的最后一个元素要不大于末尾非递减部分的第一个元素。
    // 这可能就需要我们对开头或结尾的长度进行取舍。
    //方法也很简单，首先我们求出最长的末尾非递减部分，如果整个数组都是非递减的，直接返回0。否则，原数组必定可以被分成非空的三部分。
    //我们只需要使用再一个指针left从数组头部开始往后在非递减区间移动，从数组开头到left所指元素为开头非递减部分
    //如果arr[left]>arr[right]，就不断让right后移（减小末尾非递减部分以增大开头非递减部分），若right已经移出数组范围则不进行此判断
    //在left后移的过程中，不断判断答案的最小值即可
    public int findLengthOfShortestSubarray(int[] arr) {
        int n = arr.length;
        int i = 0, j = n - 1;
        while (i < n - 1 && arr[i] <= arr[i + 1]) {
            i++;
        }
        while (j > 0 && arr[j] >= arr[j - 1]) {
            j--;
        }
        if (i >= j) return 0;
        int ans = Math.min(n - i - 1, j);
        for (int l = 0; l <= i; l++) {
            int r = binarySearch1574(arr, arr[l], j);
            ans = Math.min(ans, r - l - 1);
        }
        return ans;
    }

    private int binarySearch1574(int[] arr, int x, int l) {
        int r = arr.length;
        while (l < r) {
            int mid = l + r >> 1;
            if (arr[mid] >= x) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return r;
    }

    public int findLengthOfShortestSubarrayDualPointer(int[] arr) {
        int n = arr.length;
        int i = 0, j = n - 1;
        while (i < n - 1 && arr[i] <= arr[i + 1]) {
            i++;
        }
        while (j > 0 && arr[j] >= arr[j - 1]) {
            j--;
        }
        if (i >= j) return 0;
        int ans = Math.min(n - i - 1, j);
        for (int l = 0, r = j; l <= i; l++) {
            while (r < n && arr[r] < arr[l]) {
                r++;
            }
            ans = Math.min(ans, r - l - 1);
        }
        return ans;
    }

    //逐层排序二叉树所需的最少操作数目
    // 经典问题：给一个序列，序列两两元素可以任意交换，求最少的交换次数使得序列有序
    //这是一个经典问题，一般有两种做法：
    //1.从1到n枚举下标i,设当前序列第i个数是ai，目标序列第i个数是bi，若ai!=bi，不断将ai交换到目标位置直到ai=bi，交换次数就是答案
    //2.求整个序列中置换环的数量，答案就是序列长度减去置换环的数量（并查集 连通分量）
    private int getSwapCnt(int[] nums) {
        int n = nums.length;
        int[][] arr = new int[n][2];
        for (int i = 0; i < n; i++) {
            arr[i] = new int[]{nums[i], i};
        }
        Arrays.sort(arr, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            return o1[1] - o2[1];
        });
        SolutionUnionFind.UnionFind1 unionFind = new SolutionUnionFind.UnionFind1(n);
        for (int i = 0; i < n; i++) {
            unionFind.union(i, arr[i][1]);
        }
//        for (int i = 0; i < arr.length; i++) map.put(temp[i], i);
//        for (int i = 0; i < arr.length; i++) {
//            while (arr[i] != temp[i]) {
//                int j = map.get(arr[i]);
//                int t = arr[i];
//                arr[i] = arr[j];
//                arr[j] = t;
//                ans++;
//            }
//        }
        return n - unionFind.getConnectedNum();
    }

    // 621 任务调度器
    // 计数排序
    public int leastInterval(char[] tasks, int n) {
        char[] chars = new char[26];
        int max = 0;
        for (char task : tasks) {
            max = Math.max(max, ++chars[task - 'A']);
        }
        int result = (max - 1) * (n + 1);
        for (int i = 0; i < 26; i++) {
            if (chars[i] == max) {
                result++;
            }
        }
        return Math.max(result, tasks.length);
    }

    //1054. 距离相等的条形码
    public int[] rearrangeBarcodes(int[] barcodes) {
        int n = barcodes.length;
        int[] cnt = new int[10001];
        int max = 0;
        for (int num : barcodes) {
            cnt[num]++;
            max = Math.max(max, cnt[num]);
        }
        int even = 0, odd = 1;
        int half = n / 2;
        int[] ans = new int[n];
        for (int i = 0; i < cnt.length; i++) {
            if (cnt[i] == 0) continue;
            while (cnt[i] > 0 && cnt[i] <= half && odd < n) {
                ans[odd] = i;
                cnt[i]--;
                odd += 2;
            }
            while (cnt[i] > 0) {
                ans[even] = i;
                cnt[i]--;
                even += 2;
            }
        }
        return ans;
    }

    // 1403 非递增顺序的最小子序列
    public List<Integer> minSubsequence(int[] nums) {
        Arrays.sort(nums);
        int sum = 0;
        for (int n : nums) {
            sum += n;
        }
        int tmp = 0;
        List<Integer> result = new ArrayList<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            tmp += nums[i];
            result.add(nums[i]);
            if (tmp > sum - tmp) return result;
        }
        return result;
    }

    // 计数排序
    public List<Integer> minSubsequence2(int[] nums) {
        List<Integer> ans = new ArrayList<>();

        int sum = 0;
        int[] count = new int[101];
        for (int num : nums) {
            count[num]++;
            sum += num;
        }

        int num = 100;
        int x = 0;
        while (x <= sum - x) {
            if (count[num] != 0) {
                ans.add(num);
                x += num;
                count[num]--;
            }
            if (count[num] == 0) {
                num--;
            }
        }

        return ans;
    }

    //2007. 从双倍数组中还原原数组
    public int[] findOriginalArray(int[] changed) {
        Arrays.sort(changed);
        Map<Integer, Integer> count = new HashMap<>();
        for (int a : changed) {
            count.put(a, count.getOrDefault(a, 0) + 1);
        }
        int[] res = new int[changed.length / 2];
        int i = 0;
        for (int a : changed) {
            if (count.get(a) == 0) {
                continue;
            }
            count.put(a, count.get(a) - 1);
            if (count.getOrDefault(a * 2, 0) == 0) {
                return new int[0];
            }
            count.put(a * 2, count.get(a * 2) - 1);
            res[i++] = a;
        }
        return res;
    }

    // 1122 数组的相对排序
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        int[] cnt = new int[1001];
        for (int num : arr1) {
            cnt[num]++;
        }
        int idx = 0;
        for (int num : arr2) {
            while (cnt[num]-- > 0) {
                arr1[idx++] = num;
            }
        }
        for (int i = 0; i <= 1000; i++) {
            while (cnt[i]-- > 0) {
                arr1[idx++] = i;
            }
        }
        return arr1;
    }

    // 899 有序队列
    //给定一个字符串 s 和一个整数 k 。你可以从 s 的前 k 个字母中选择一个，并把它加到字符串的末尾。
// 返回 在应用上述步骤的任意数量的移动后，字典上最小的字符串 。
//输入：s = "cba", k = 1
//输出："acb"
    // 朴素双循环
    public String orderlyQueue(String s, int k) {
        if (k == 1) {
            String smallest = s;
            StringBuilder sb = new StringBuilder(s);
            int n = s.length();
            for (int i = 1; i < n; i++) {
                char c = sb.charAt(0);
                sb.deleteCharAt(0);
                sb.append(c);
                if (sb.toString().compareTo(smallest) < 0) {
                    smallest = sb.toString();
                }
            }
            return smallest;
        } else {
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            return new String(arr);
        }
    }

    // 最小表示法
    public String orderlyQueue2(String s, int _k) {
        char[] cs = s.toCharArray();
        if (_k == 1) {
            int i = 0, j = 1, k = 0, n = cs.length;
            while (i < n && j < n && k < n) {
                char a = cs[(i + k) % n], b = cs[(j + k) % n];
                if (a == b) k++;
                else {
                    if (a > b) i += k + 1;
                    else j += k + 1;
                    if (i == j) i++;
                    k = 0;
                }
            }
            i = Math.min(i, j);
            return s.substring(i) + s.substring(0, i);
        } else {
            Arrays.sort(cs);
            return String.valueOf(cs);
        }
    }

    //offer 51 数组中的逆序对
    //在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数。
// 输入: [7,5,6,4]
//输出: 5
    public int reversePairs(int[] nums) {
        if (nums.length < 2) return 0;
        return reversePairs(nums, 0, nums.length - 1, new int[nums.length]);
    }

    private int reversePairs(int[] nums, int left, int right, int[] temp) {
        if (left >= right) {
            return 0;
        }

        int mid = (left + right) / 2;
        int leftPairs = reversePairs(nums, left, mid, temp);
        int rightPairs = reversePairs(nums, mid + 1, right, temp);

        if (nums[mid] <= nums[mid + 1]) {
            return leftPairs + rightPairs;
        }

        int crossPairs = mergeAndCount(nums, left, mid, right, temp);
        return leftPairs + rightPairs + crossPairs;
    }

    private int mergeAndCount(int[] nums, int left, int mid, int right, int[] temp) {
        for (int i = left; i <= right; i++) {
            temp[i] = nums[i];
        }

        int i = left;
        int j = mid + 1;

        int count = 0;
        for (int k = left; k <= right; k++) {
            // 左边数组已经遍历完
            if (i == mid + 1) {
                nums[k] = temp[j++];
            } else if (j == right + 1) {   // 右边数组已经遍历完
                nums[k] = temp[i++];
            } else if (temp[i] <= temp[j]) { //左边小于右边
                nums[k] = temp[i++];
            } else {
                nums[k] = temp[j++]; //左边大于右边时计算i到mid的个数[i-mid]都大于j
                count += (mid - i + 1);
            }
        }
        return count;
    }

    // 1710卡车上的最大单元数
    public int maximumUnits(int[][] boxTypes, int truckSize) {
        Arrays.sort(boxTypes, (o1, o2) -> {
            if (o2[1] != o1[1]) {
                return o2[1] - o1[1];
            }
            return o1[0] - o2[0];
        });
        int ans = 0;
        for (int[] box : boxTypes) {
            if (truckSize <= 0) break;
            if (box[0] > truckSize) {
                ans += box[1] * truckSize;
                truckSize = 0;
            } else {
                ans += box[1] * box[0];
                truckSize -= box[0];
            }
        }
        return ans;
    }

    //2512. 奖励最顶尖的 K 名学生
    public List<Integer> topStudents(String[] positive_feedback, String[] negative_feedback, String[] report, int[] student_id, int k) {
        Map<String, Integer> words = new HashMap<>();
        for (String word : positive_feedback) {
            words.put(word, 3);
        }
        for (String word : negative_feedback) {
            words.put(word, -1);
        }
        int n = report.length;
        int[] scores = new int[n];
        int[][] A = new int[n][2];
        for (int i = 0; i < n; i++) {
            int score = 0;
            for (String word : report[i].split(" ")) {
                score += words.getOrDefault(word, 0);
            }
            A[i] = new int[]{-score, student_id[i]};
        }
        Arrays.sort(A, (a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
        List<Integer> topK = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            topK.add(A[i][1]);
        }
        return topK;
    }

    //1333. 餐厅过滤器
    public List<Integer> filterRestaurants(int[][] restaurants, int veganFriendly, int maxPrice, int maxDistance) {
        int n = restaurants.length;
        List<int[]> filtered = new ArrayList<int[]>();
        for (int i = 0; i < n; i++) {
            if (restaurants[i][3] <= maxPrice && restaurants[i][4] <= maxDistance && !(veganFriendly == 1 && restaurants[i][2] == 0)) {
                filtered.add(restaurants[i]);
            }
        }
        Collections.sort(filtered, (a, b) -> {
            if (a[1] != b[1]) {
                return b[1] - a[1];
            } else {
                return b[0] - a[0];
            }
        });
        List<Integer> res = new ArrayList<Integer>();
        for (int[] v : filtered) {
            res.add(v[0]);
        }
        return res;
    }

    //2037. 使每位学生都有座位的最少移动次数
    public int minMovesToSeat(int[] seats, int[] students) {
        int n = seats.length;
        Arrays.sort(seats);
        Arrays.sort(students);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans += Math.abs(seats[i] - students[i]);
        }
        return ans;
    }

    //面试题 17.26. 稀疏相似度  倒排索引
    public List<String> computeSimilarities(int[][] docs) {
        List<String> ans = new ArrayList<>();
        int n = docs.length;
        Map<Integer, List<Integer>> map = new HashMap<>();
        // 计数
        int[][] help = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < docs[i].length; j++) {
                List<Integer> docIds = map.getOrDefault(docs[i][j], new ArrayList<>());
                // 对于具体的某个单词docs[i][j]，i文档与哪些文档有交集，help[i][docId]++;
                for (int docId : docIds) {
                    help[i][docId]++;
                }
                docIds.add(i);
                map.put(docs[i][j], docIds);
            }
            for (int docId = 0; docId < n; docId++) {
                // help的顺序性：docId从小到大遍历，遍历小doc时的单词没有其余doc，遍历大的doc的单词时，map中存了小doc的id，help是偏下的一个矩阵
                if (help[i][docId] > 0) {
                    ans.add(docId + "," + i + ": " + String.format("%.4f", (double) help[i][docId] / (docs[i].length + docs[docId].length - help[i][docId])));
                }
            }
        }
        return ans;

    }

    //endregion---------------------------------------------------------------------------------------------
}
