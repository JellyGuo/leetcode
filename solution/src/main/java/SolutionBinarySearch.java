import java.util.*;

public class SolutionBinarySearch {

    //region---------------------------------------------------二分-----------------------------------------------
    //二分模板
    public void binary() {
        long l = 0, r = 1000009;
        while (l < r) {
            long mid = l + r + 1 >> 1;
            if (check(mid)) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }

        while (l < r) {
            long mid = l + r >> 1;
            if (check(mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
    }

    public boolean check(long mid) {
        return false;
    }

    // 34 在排序数组中查找元素的第一个和最后一个位置
    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) return new int[]{-1, -1};
        int l = 0, r = nums.length - 1;
        while (l < r) {
            // >=target的最小的位置
            int mid = l + r >> 1;
            if (nums[mid] >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        int left = nums[l] == target ? l : -1;
        l = 0;
        r = nums.length - 1;
        while (l < r) {
            // <=target的最大的位置
            int mid = l + r + 1 >> 1;
            if (nums[mid] <= target) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        int right = nums[l] == target ? l : -1;
        return new int[]{left, right};
    }

    //35. 搜索插入位置
    public int searchInsert(int[] nums, int target) {
        int l = 0, r = nums.length;
        while (l < r) {
            // 查找>=target的最小的位置,全都<target的时候数组长度可加1，故r=nums.length
            int mid = (l + r) >> 1;
            if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    // 74 搜索二维矩阵
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        if (target < matrix[0][0] || target > matrix[m - 1][n - 1]) return false;
        int l = 0, r = m - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (matrix[mid][0] <= target) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        int row = l;
        l = 0;
        r = n - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (matrix[row][mid] >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        int col = l;
        return matrix[row][col] == target;
    }

    // 240 搜索二维矩阵
    public boolean searchMatrix2(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        if (target < matrix[0][0] || target > matrix[m - 1][n - 1]) return false;
        for (int[] row : matrix) {
            int l = 0, r = n - 1;
            while (l < r) {
                int mid = l + r >> 1;
                if (row[mid] >= target) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            if (row[l] == target) return true;
        }
        return false;
    }

    public boolean searchMatrix2BST(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int x = 0, y = n - 1;
        while (x < m && y >= 0) {
            if (matrix[x][y] == target) {
                return true;
            }
            if (matrix[x][y] > target) {
                y--;
            } else {
                x++;
            }
        }
        return false;
    }

    //33 搜索旋转数组
    public int search(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int mid = l + r >> 1;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[l] <= nums[mid]) {
                if (nums[l] <= target && nums[mid] > target) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            } else {
                if (nums[mid] < target && nums[r] >= target) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
        }
        return -1;
    }

    //分治
    public int searchDivideConquer(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }
        if (nums.length == 1) {
            return nums[0] == target ? 0 : -1;
        }
        return searchlr(nums, 0, nums.length - 1, target);

    }

    private int searchlr(int[] nums, int left, int right, int target) {
        if (left > right) {
            return -1;
        }

        int mid = (right + left) / 2;
        if (nums[mid] == target) {
            return mid;
        }

        if (nums[left] <= nums[mid]) {//left有序
            if (nums[left] <= target && target < nums[mid]) {
                return searchlr(nums, left, mid - 1, target);
            } else {
                return searchlr(nums, mid + 1, right, target);
            }
        } else {//right有序
            if (nums[mid] < target && target <= nums[right]) {
                return searchlr(nums, mid + 1, right, target);
            } else {
                return searchlr(nums, left, mid - 1, target);
            }
        }
    }

    public int search33(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) return -1;
        if (n == 1) return nums[0] == target ? 0 : -1;

        // 第一次「二分」：从中间开始找，找到满足 >=nums[0] 的分割点（旋转点）
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            // l向右收缩，找到>=nums[0]的最右的值，即是旋转点
            if (nums[mid] >= nums[0]) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }

        // 第二次「二分」：通过和 nums[0] 进行比较，得知 target 是在旋转点的左边还是右边
        if (target >= nums[0]) {
            l = 0;
        } else {
            l = l + 1;
            r = n - 1;
        }
        while (l < r) {
            int mid = l + r >> 1;
            if (nums[mid] >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }

        return nums[r] == target ? r : -1;
    }


    //已知存在一个按非降序排列的整数数组 nums ，数组中的值不必互不相同。
//
// 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了 旋转 ，使数组变为 [nums[k], nums
//[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标 从 0 开始 计数）。例如， [0,1,
//2,4,4,4,5,6,6,7] 在下标 5 处经旋转后可能变为 [4,5,6,6,7,0,1,2,4,4] 。
// 给你 旋转后 的数组 nums 和一个整数 target ，请你编写一个函数来判断给定的目标值是否存在于数组中。如果 nums 中存在这个目标值 targ
//et ，则返回 true ，否则返回 false 。
//输入：nums = [2,5,6,0,0,1,2], target = 0
//输出：true
    public boolean search2(int[] nums, int target) {
        if (nums.length == 0) {
            return false;
        }
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return true;
            }
            if (nums[left] == nums[mid]) {
                left++;
            } else if (nums[mid] == nums[right]) {
                right--;
            } else if (nums[left] < nums[mid]) {
                if (target >= nums[left] && nums[mid] > target) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            } else if (nums[mid] < nums[right]) {
                if (target > nums[mid] && nums[right] >= target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }

            }
        }
        return false;
    }

    //81 搜索旋转数组  带重复数据 [3,1,2,3,3,3,3,3]
    public boolean searchWithDuplicate(int[] nums, int target) {
        if (nums.length == 0) return false;
        if (nums.length == 1) return nums[0] == target;
        return searchWithDuplicate(nums, target, 0, nums.length - 1);
    }

    private boolean searchWithDuplicate(int[] nums, int target, int left, int right) {
        if (right < left) {
            return false;
        }
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return true;
            }
            //2 2 5 0 2 2 2 2 2
            if (nums[mid] == nums[left] && nums[mid] == nums[right]) {
                left++;
                right--;
            } else if (nums[mid] >= nums[left]) {
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return false;
    }

    public boolean search81(int[] nums, int target) {
        if (nums.length == 0) return false;
        if (nums.length == 1) return nums[0] == target;
        int l = 0, r = nums.length - 1;
        while (l < r && nums[r] == nums[l]) {
            r--;
        }
        int end = r;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (nums[mid] >= nums[0]) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        if (target >= nums[0]) {
            l = 0;
        } else {
            l = l + 1;
            r = end;
        }
        while (l < r) {
            int mid = l + r >> 1;
            if (nums[mid] >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return nums[r] == target;
    }

    //153 寻找旋转排序数组中的最小值
    public int findMin(int[] nums) {
        return findMin(nums, 0, nums.length - 1);
    }

    private int findMin(int[] nums, int left, int right) {
        if (left >= right) {
            return nums[left];
        }
        int mid = (left + right) / 2;
        if (nums[mid] > nums[mid + 1]) {
            return nums[mid + 1];
        }
        int leftMin = findMin(nums, left, mid);
        int rightMin = findMin(nums, mid + 1, right);
        return Math.min(leftMin, rightMin);
    }

    public int findMin2(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] > nums[mid + 1]) {
                return nums[mid + 1];
            }
            //left要变化，所以判mid+1和right的关系
            if (nums[mid + 1] > nums[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return nums[left];
    }

    public int findMin3(int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r >> 1;
            //找小于nums[r]最远的数
            if (nums[mid] <= nums[r]) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return nums[l];
    }

    public int findMin4(int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            // 找大于等于nums[0]最远的数
            if (nums[mid] >= nums[0]) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return nums[(l + 1) % n];
    }

    // 154 寻找旋转排序数组中的最小值
    // 数组可重复。
//输入：nums = [2,2,2,0,1]
//输出：0
    public int findMinWithDupi(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                right--;
            }
        }
        return nums[left];
    }

    public int findMinWithDupi2(int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r && nums[l] == nums[l + 1]) {
            l++;
        }
        while (l < r && nums[r] == nums[r - 1]) {
            r--;
        }
        while (l < r) {
            int mid = l + r >> 1;
            if (nums[mid] <= nums[r]) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return nums[l];
    }

    public int findMinWithDupi3(int[] numbers) {
        int n = numbers.length;
        int l = 0, r = n - 1;
        while (r >= 0 && numbers[l] == numbers[r]) r--;
        while (l < r) {
            int mid = l + r >> 1;
            if (numbers[mid] <= numbers[r]) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return numbers[l];
    }

    //搜索旋转数组。给定一个排序后的数组，包含n个整数，但这个数组已被旋转过很多次了，次数不详。请编写代码找出数组中的某个元素，假设数组元素原先是按升序排列的。若
//有多个相同元素，返回索引值最小的一个。
//  输入: arr = [15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14], target = 5
// 输出: 8（元素5在该数组中的索引）
    //搜索旋转数组最左边索引
    public int searchLeftIndex(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            //最左相等直接返回
            if (arr[left] == target) {
                return left;
            }
            //中间相等 右指针指向中间，继续搜左边
            if (arr[mid] == target) {
                right = mid;
            } else if (arr[left] < arr[mid]) {
                if (target >= arr[left] && target < arr[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else if (arr[left] > arr[mid]) {
                if (target > arr[mid] && target <= arr[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else {
                //最左和中间相等，左加一继续搜
                left++;
            }
        }
        return -1;
    }


    public int searchTwoBinarySearch(int[] nums, int target) {
        int i = 0, j = nums.length - 1;
        //找右边界
        while (i <= j) { //相等时进入，保证右边界>target
            int mid = (i + j) / 2;
            if (nums[mid] <= target) i = mid + 1; //相等继续向右探索，直到>target
            else j = mid - 1;
        }
        int right = i;
        if (j > 0 && nums[j] != target) return 0;
        //重置，找左边界
        i = 0;
        j = nums.length - 1;
        while (i <= j) {
            int mid = (i + j) / 2;
            if (nums[mid] < target) i = mid + 1;
            else j = mid - 1;
        }
        int left = j;
        return right - left - 1;
    }

    // 162 寻找峰值
    //峰值元素是指其值严格大于左右相邻值的元素。
// 给你一个整数数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。
// 你可以假设 nums[-1] = nums[n] = -∞ 。
    public int findPeakElement(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = l + r >> 1;
            // mid严格小于mid+1的时候往后找
            // 大于等于nums[mid+1]最近的值，每次mid+1都在变，每次往后找大于mid+1的值
            // 如果到了边界就取边界，如果有拐点，就能找到大于等于nums[mid+1]最近的值
            if (nums[mid] < nums[mid + 1]) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    public int findPeakElement2(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (nums[mid - 1] <= nums[mid]) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return l;
    }

    // 852 山峰数组的封顶索引
    public int peakIndexInMountainArray(int[] arr) {
        int l = 0, r = arr.length - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (arr[mid] > arr[mid + 1]) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    public int findInMountainArray(int target, MountainArray mountainArr) {
        int l = 0, r = mountainArr.length() - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (mountainArr.get(mid) < mountainArr.get(mid + 1)) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        int peak = l;
        l = 0;
        r = peak;
        while (l < r) {
            int mid = l + r >> 1;
            if (mountainArr.get(mid) >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        if (mountainArr.get(l) == target) return l;
        l = peak + 1;
        r = mountainArr.length() - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (mountainArr.get(mid) <= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return mountainArr.get(l) == target ? l : -1;
    }

    //1901. 寻找峰值 II
    public int[] findPeakGrid(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int l = 0, r = m - 1;
        while (l < r) {
            int mid = l + r >> 1;
            int j = maxIdx(mat[mid]);
            if (mat[mid][j] >= mat[mid + 1][j]) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return new int[]{l, maxIdx(mat[l])};
    }

    private int maxIdx(int[] array){
        int idx = 0;
        for (int j = 1; j < array.length; j++) {
            if (array[j] > array[idx]) {
                idx = j;
            }
        }
        return idx;
    }

    // 面试10.05 稀疏数组搜索
    //稀疏数组搜索。有个排好序的字符串数组，其中散布着一些空字符串，编写一种方法，找出给定字符串的位置。
    public int findString1(String[] words, String s) {
        int n = words.length;
        int l = 0, r = n - 1;
        while (l < r) {
            while (l < r && words[l].length() == 0) l++;
            while (l < r && words[r].length() == 0) r--;
            int mid = l + ((r - l) >> 1);
            // l是变动的，mid向l收缩；否则mid收缩到r，下一轮若r=mid r不变mid不变死循环
            while (mid >= l && words[mid].length() == 0) mid--;
            if (s.compareTo(words[mid]) > 0) {
                l = mid + 1;
            } else if (s.compareTo(words[mid]) < 0) {
                r = mid;
            } else {
                return mid;
            }
        }
        return s.equals(words[l]) ? l : -1;
    }

    public int findString2(String[] words, String s) {
        int n = words.length;
        int l = 0, r = n - 1;
        while (l <= r) {
            while (l <= r && words[l].length() == 0) l++;
            while (l <= r && words[r].length() == 0) r--;
            int mid = l + ((r - l) >> 1);
            while (mid >= l && words[mid].length() == 0) mid--;
            //while (mid <= r && words[mid].length() == 0) mid++;
            if (s.compareTo(words[mid]) > 0) {
                l = mid + 1;
            } else if (s.compareTo(words[mid]) < 0) {
                r = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // 274 H指数
    //引用次数至少为 x 次的 x 篇论文
    public int hIndex(int[] cs) {
        int n = cs.length;
        int l = 0, r = n;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            // 大于等于mid篇的数量最远的数
            if (check(cs, mid)) l = mid;
            else r = mid - 1;
        }
        return r;
    }

    boolean check(int[] cs, int mid) {
        int ans = 0;
        for (int i : cs) if (i >= mid) ans++;
        return ans >= mid;
    }

    // // 275 H指数 数组有序
    public int hIndex2(int[] cs) {
        int n = cs.length;
        int l = 0, r = n - 1;
        //寻找引用次数至少为 x 次的 x 篇论文
        while (l < r) {
            int mid = l + r >> 1;
            // n-mid即为大于等于mid的个数，坐标越小个数越多
            if (cs[mid] >= n - mid) r = mid;
            else l = mid + 1;
        }
        return cs[r] >= n - r ? n - r : 0;
    }

    //367. 有效的完全平方数
    public boolean isPerfectSquare(int num) {
        if (num == 1) return true;
        int l = 1, r = num/2;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if ((long)mid * mid >= num) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l * l == num;
    }

    //374. 猜数字大小
    public int guessNumber(int n) {
        GuessGame guessGame = num -> 0;
        int l = 1, r = n;
        while (l <= r) {
            int mid =l + (r -l)/2;
            if (guessGame.guess(mid) == 0) return mid;
            if (guessGame.guess(mid) < 0) {
                r = mid-1;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    // 475 供暖气
    // 暴力遍历
    public int findRadius(int[] houses, int[] heaters) {
        int min = Integer.MIN_VALUE;
        for (int house : houses) {
            int dist = Integer.MAX_VALUE;
            for (int heater : heaters) {
                dist = Math.min(dist, Math.abs(heater - house));
            }
            min = Math.max(min, dist);
        }
        return min;
    }

    // 二分
    public int findRadiusBinarySearch(int[] houses, int[] heaters) {
        int min = Integer.MIN_VALUE;
        int dist;

        Arrays.sort(heaters);
        //对于每个房屋，要么用前面的暖气，要么用后面的，二者取近的，得到距离
        //排序后求出离每个房屋最近的两个暖气
        for (int house : houses) {
            if (house <= heaters[0]) {
                dist = heaters[0] - house;
            } else if (house >= heaters[heaters.length - 1]) {
                dist = house - heaters[heaters.length - 1];
            } else {
                //找到小于house的最大的heater
                int l = 0, r = heaters.length - 1;
                while (l < r) {
                    // mid取中间后一个
                    int mid = (l + r + 1) / 2;
                    //严格大于时往前一个的范围找
                    if (heaters[mid] > house) {
                        r = mid - 1;
                    } else {
                        l = mid;
                    }
                }
//              找大于target的最小的坐标的模板
//                int l = 0, r = heaters.length - 1;
//                while (l < r) {
//                    int mid = (l + r) / 2;
//                    if (heaters[mid] < target) {
//                        l = mid + 1;
//                    } else {
//                        r = mid;
//                    }
//                }
//                return l;
                dist = Math.min(house - heaters[l], heaters[l + 1] - house);
            }
            min = Math.max(min, dist);
        }
        return min;
    }

    // 双指针
    public int findRadiusDualPointer(int[] houses, int[] heaters) {
        int min = Integer.MIN_VALUE;
        int dist;
        //不排序houses，对每个house从0开始找heaters也可以，但是这样等于暴力遍历
        //排序house后，下一个house可以服复用前一个j的值，j不是从0开始，提升了效率
        Arrays.sort(houses);
        Arrays.sort(heaters);
        for (int i = 0, j = 0; i < houses.length; i++) {
            while (j < heaters.length && heaters[j] < houses[i]) j++;
            if (j == 0) {
                dist = heaters[0] - houses[i];
            } else if (j == heaters.length) {
                dist = houses[i] - heaters[heaters.length - 1];
            } else {
                dist = Math.min(houses[i] - heaters[j - 1], heaters[j] - houses[i]);
            }
            min = Math.max(dist, min);
        }
        return min;
    }

    // 540 有序数组中的单一元素
    //给你一个仅由整数组成的有序数组，其中每个元素都会出现两次，唯有一个数只会出现一次。
// 请你找出并返回只出现一次的那个数。
// 你设计的解决方案必须满足 O(log n) 时间复杂度和 O(1) 空间复杂度。
    //输入: nums = [1,1,2,3,3,4,4,8,8] 输出: 2
    // O(n)
    public int singleNonDuplicate(int[] nums) {
        int i = 0;
        while (i < nums.length - 1) {
            if (nums[i + 1] - nums[i] != 0) {
                return nums[i];
            }
            i += 2;
            if (i >= nums.length - 1) return nums[i];
        }
        return nums[0];

    }

    //O(logN)
    public int singleNonDuplicateBinarySearch(int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (mid % 2 == 0) {
                if (mid + 1 < n && nums[mid] == nums[mid + 1]) l = mid + 1;
                else r = mid;
            } else {
                if (mid - 1 >= 0 && nums[mid - 1] == nums[mid]) l = mid + 1;
                else r = mid;
            }
        }
        return nums[r];
    }


    // 875 爱吃香蕉的珂珂
    public int minEatingSpeed(int[] piles, int h) {
        int low = 1;
        int high = 0;
        for (int pile : piles) {
            high = Math.max(high, pile);
        }
        while (low < high) {
            int mid = (low + high) / 2;
            // mid速率的时间严格大于h时,提高速率
            if (getTime(piles, mid) > h) {
                low = mid + 1;
            } else {
                // 小于等于h时,求最小的速率
                high = mid;
            }
        }
        return low;
    }

    private int getTime(int[] piles, int speed) {
        int time = 0;
        for (int pile : piles) {
            // 向上取整
            time += (pile + speed - 1) / speed;
        }
        return time;
    }

    //2560. 打家劫舍 IV
    // 类似还有
    // 2439. 最小化数组中的最大值
    //2513. 最小化两个数组中的最大值
    //2517. 礼盒的最大甜蜜度
    //2528. 最大化城市的最小供电站数目
    public int minCapability(int[] nums, int k) {
        int left = 0, right = (int) 1e9;
        while (left + 1 < right) {
            int mid = (left + right) >>> 1;
            int f0 = 0, f1 = 0;
            for (int x : nums)
                if (x > mid) f0 = f1;
                else {
                    int tmp = f1;
                    f1 = Math.max(f1, f0 + 1);
                    f0 = tmp;
                }
            if (f1 >= k) right = mid;
            else left = mid;
        }
        return right;
    }

    //2646. 最小化旅行的价格总和
    private List<Integer>[] g;
    private int[] price, cnt;
    private int end;

    public int minimumTotalPrice(int n, int[][] edges, int[] price, int[][] trips) {
        g = new ArrayList[n];
        Arrays.setAll(g, e -> new ArrayList<>());
        for (int[] e : edges) {
            int x = e[0], y = e[1];
            g[x].add(y);
            g[y].add(x); // 建树
        }
        this.price = price;

        cnt = new int[n];
        for (int[] t : trips) {
            end = t[1];
            path(t[0], -1);
        }

        int[] p = dfs(0, -1);
        return Math.min(p[0], p[1]);
    }

    private boolean path(int x, int fa) {
        if (x == end) { // 到达终点（注意树只有唯一的一条简单路径）
            ++cnt[x]; // 统计从 start 到 end 的路径上的点经过了多少次
            return true; // 找到终点
        }
        for (int y : g[x])
            if (y != fa && path(y, x)) {
                ++cnt[x]; // 统计从 start 到 end 的路径上的点经过了多少次
                return true; // 找到终点
            }
        return false; // 未找到终点
    }

    // 类似 337. 打家劫舍 III https://leetcode.cn/problems/house-robber-iii/
    private int[] dfs(int x, int fa) {
        int notHalve = price[x] * cnt[x]; // x 不变
        int halve = notHalve / 2; // x 减半
        for (int y : g[x])
            if (y != fa) {
                int[] p = dfs(y, x); // 计算 y 不变/减半的最小价值总和
                notHalve += Math.min(p[0], p[1]); // x 不变，那么 y 可以不变，可以减半，取这两种情况的最小值
                halve += p[0]; // x 减半，那么 y 只能不变
            }
        return new int[]{notHalve, halve};
    }

    //2300. 咒语和药水的成功对数
    public int[] successfulPairs(int[] spells, int[] potions, long success) {
        int n = spells.length, m = potions.length;
        int[] ans = new int[n];
        Arrays.sort(potions);
        for (int i = 0; i < n; i++) {
            long target = (success + spells[i] - 1) / spells[i];
            int idx = binarySearch((int) target, potions);
            if (potions[idx] >= target) {
                ans[i] = m - idx;
            }
        }
        return ans;
    }

    private int binarySearch(int target, int[] array) {
        int l = 0, r = array.length-1;
        while (l < r) {
            int mid = l + r >> 1;
            if (array[mid] >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    //2616. 最小化数对的最大差值
    // 转化为满足差值X的最大对数>=p的最小X
    // 差值越小，对数约少
    public int minimizeMax(int[] nums, int p) {
        Arrays.sort(nums);
        int n = nums.length;
        int l = 0, r = nums[n - 1] - nums[0];
        while (l < r) {
            int mid = l + r >> 1;
            if (getCnt2616(mid, nums) >= p) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    // 贪心求对数 类似198打家劫舍 可用dp求
    // 选nums[0]和nums[1]+剩下n-2个数的对数 => pair(n-2)+1
    // 不选nums[0]=>pair(n-1)
    // pair(n-1) <= pair(n-3)+1 选nums[1]和nums[2]+剩下n-3个数的对数
    // 而pair(n-2)>pair(n-3)
    //所以 pair(n) 最大的对数是pair(n-2)+1,即nums[0]和nums[1]放一起选
    private int getCnt2616(int x, int[] nums) {
        int n = nums.length;
        int cnt = 0;
        for (int i = 0; i < n - 1; i++) {
            if (nums[i + 1] - nums[i] <= x) {
                cnt++;
                i++;
            }
        }
        return cnt;
    }

    //1011. 在 D 天内送达包裹的能力
    public int shipWithinDays(int[] weights, int days) {
        int n = weights.length;
        int sum = 0, max = 0;
        for (int w : weights) {
            sum += w;
            max = Math.max(w, max);
        }
        int l = Math.max(max, (sum + days - 1) / days), r = sum;
        while (l < r) {
            int mid = l + r >> 1;
            // 当前load需要的天数 往days收缩
            if (getLoadDays(mid, weights) <= days) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private int getLoadDays(int load, int[] weights) {
        int days = 0;
        int diff = load;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] <= diff) {
                diff -= weights[i];
            } else {
                days++;
                diff = load;
                i--;
            }
        }
        return days + 1;
    }

    //1760. 袋子里最少数目的球
    public int minimumSize(int[] nums, int maxOperations) {
        int n = nums.length;
        int max = 0;
        for (int num : nums) {
            max = Math.max(max, num);
        }
        // 给定 maxOperations  次操作次数，能否可以使得单个袋子里球数目的最大值不超过 开销y。
        // y0>=y也满足,y1<y时不满足=>求最小的开销y
        // l,r,mid 代表数组操作后不超过的上限值
        // 上限值约大,所需操作数越少,希望在ops尽可能大时的最小ops
        // ops>maxOps时 要减少ops 提升y;ops<=maxOps时,y已经满足条件,向左收缩,取最小的开销y
        int l = 1, r = max;
        while (l < r) {
            int mid = l + r >> 1;
            int ops = getOps(nums, mid);
            if (ops <= maxOperations) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private int getOps(int[] nums, int y) {
        int ops = 0;
        for (int num : nums) {
            ops += (num - 1) / y;
        }
        return ops;
    }

    //1170. 比较字符串最小字母出现频次
    public int[] numSmallerByFrequency(String[] queries, String[] words) {
        int n = words.length;
        int[] fwords = new int[n];
        for (int i = 0; i < n; i++) {
            fwords[i] = f(words[i]);
        }
        Arrays.sort(fwords);
        int m = queries.length;
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            int t = f(queries[i]);
            ans[i] = largeThan(t, fwords);
        }
        return ans;
    }

    private int largeThan(int t, int[] num) {
        int l = 0, r = num.length - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (num[mid] <= t) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        if (num[r] <= t) return num.length - 1 - r;
        return num.length;
    }

    private int f(String word) {
        char[] chars = word.toCharArray();
        int[] cnt = new int[26];
        for (char c : chars) {
            cnt[c - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (cnt[i] == 0) continue;
            return cnt[i];
        }
        return 0;
    }

    //1802. 有界数组中指定下标处的最大值
    public int maxValue(int n, int index, int maxSum) {
        int l = 1, r = maxSum;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            long sum = getSum(mid, n, index);
            if (sum > maxSum) {
                r = mid - 1;
            } else {
                l = mid;
            }
        }
        return l;
    }

    private long getSum(int mid, int n, int index) {
        int left = index;
        int right = n - index - 1;
        return (long) mid + cal(mid, left) + cal(mid, right);
    }

    private long cal(int big, int length) {
        if (length + 1 < big) {
            int small = big - length;
            return (long) (big - 1 + small) * length / 2;
        } else {
            int ones = length - (big - 1);
            return (long) big * (big - 1) / 2 + ones;
        }
    }

    // 2517. 礼盒的最大甜蜜度
    public int maximumTastiness(int[] price, int k) {
        Arrays.sort(price);
        //  甜蜜度就是绝对值的差值 绝对值的差值d范围[0，price最大值]
        // 求最大 甜蜜度，即满足k个的最大绝对值差值d
        // d越小，cnt越多；d越大，cnt越少 当cnt小于k时说明d取得大了
        // d在cnt>=k时向右收缩取最大
        int l = 0, r = price[price.length - 1];
        while (l < r) {
            int mid = l + r + 1 >> 1;
            int cnt = getAbsCnt(price, mid);
            if (cnt < k) {
                r = mid - 1;
            } else {
                l = mid;
            }
        }
        return l;
    }

    private int getAbsCnt(int[] price, int x) {
        // 从前往后，先选取最小的，下一个可以选的数是第一个 ≥x+d 的数，依此类推，统计个数
        int cnt = 1, k = 0;
        for (int i = 1; i < price.length; i++) {
            if (price[i] - price[k] >= x) {
                cnt++;
                k = i;
            }
        }
        return cnt;
    }

    //2576. 求出最多标记下标
    public int maxNumOfMarkedIndices(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        // 对数最多n/2对
        int l = 0, r = n / 2;
        while (l < r) {
            // 求做多对数
            int m = (l + r + 1) / 2;
            if (checkPair(nums, m)) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return l * 2;
    }

    public boolean checkPair(int[] nums, int k) {
        if (k * 2 > nums.length) {
            return false;
        }
        int l = 0;
        int r = nums.length - k;
        for (int i = 0; i < k; i++, l++, r++) {
            if (nums[l] * 2 > nums[r]) {
                return false;
            }
        }
        return true;
    }

    //2594. 修车的最少时间
    public long repairCars(int[] ranks, int cars) {
        long l = 1, r = Long.MAX_VALUE;
        while (l < r) {
            long mid = l + r >> 1;
            if (check(ranks, cars, mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private boolean check(int[] ranks, int cars, long min) {
        long capacity = 0;
        for (int r : ranks) {
            capacity += Math.sqrt((double) min / r);
            if (capacity >= cars) return true;
        }
        return false;
    }

    //1782. 统计点对的数目
    public int[] countPairs(int n, int[][] edges, int[] queries) {
        int[] degree = new int[n];
        Map<Integer, Integer> cnt = new HashMap<Integer, Integer>();
        for (int[] edge : edges) {
            int x = edge[0] - 1, y = edge[1] - 1;
            if (x > y) {
                int temp = x;
                x = y;
                y = temp;
            }
            degree[x]++;
            degree[y]++;
            cnt.put(x * n + y, cnt.getOrDefault(x * n + y, 0) + 1);
        }

        int[] arr = Arrays.copyOf(degree, n);
        int[] ans = new int[queries.length];
        Arrays.sort(arr);
        for (int k = 0; k < queries.length; k++) {
            int bound = queries[k], total = 0;
            for (int i = 0; i < n; i++) {
                int j = binarySearch(arr, i + 1, n - 1, bound - arr[i]);
                total += n - j;
            }
            for (Map.Entry<Integer, Integer> entry : cnt.entrySet()) {
                int val = entry.getKey(), freq = entry.getValue();
                int x = val / n, y = val % n;
                if (degree[x] + degree[y] > bound && degree[x] + degree[y] - freq <= bound) {
                    total--;
                }
            }
            ans[k] = total;
        }

        return ans;
    }

    public int binarySearch(int[] arr, int left, int right, int target) {
        int ans = right + 1;
        while (left <= right) {
            int mid = (left + right) >> 1;
            if (arr[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
                ans = mid;
            }
        }
        return ans;
    }

    // 410 分割数组的最大值
    // DP做法搜splitArrayDP
    public int splitArray(int[] nums, int m) {
        int sum = 0, max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int l = max, r = sum;
        while (l < r) {
            int mid = l + r >> 1;
            int cnt = getCnt(nums, mid);
            // [l,r]和mid对应的是和，要使和尽可能小，在满足条件的情况下向左收缩
            if (cnt <= m) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private int getCnt(int[] nums, int x) {
        int sum = 0, cnt = 1;
        for (int num : nums) {
            if (sum + num > x) {
                cnt++;
                sum = num;
            } else {
                sum += num;
            }
        }
        return cnt;
    }

    // 1539 第k个缺失的正整数
    // 第i位缺失的个数是arr[i]-(i+1)
    // 找到小于k的最大的坐标l(往左压缩至小于k)
    // 那么第k个就是k-l缺失的个数+arr[l]
    public int findKthPositive(int[] arr, int k) {
        if (arr[0] > k) {
            return k;
        }
        int n = arr.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            int x = mid < n ? arr[mid] : Integer.MAX_VALUE;
            if (arr[mid] - (mid + 1) < k) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return k - (arr[l] - (l + 1)) + arr[l];
    }

    // 1608 特殊数组的特征值
    //给你一个非负整数数组 nums 。如果存在一个数 x ，使得 nums 中恰好有 x 个元素 大于或者等于 x ，那么就称 nums 是一个 特殊数组 ，而x 是该数组的 特征值 。
// 注意： x 不必 是 nums 的中的元素。
// 如果数组 nums 是一个 特殊数组 ，请返回它的特征值 x 。否则，返回 -1 。可以证明的是，如果 nums 是特殊数组，那么其特征值 x 是 唯一的
// 输入：nums = [3,5]
//输出：2
//解释：有 2 个元素（3 和 5）大于或等于 2 。
    public int specialArray(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int l = 0, r = nums[n - 1];
        while (l < r) {
            int mid = l + r >> 1;
            int cnt = getCnt(mid, nums);
            // 大于mid的数量太多
            if (cnt > mid) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return getCnt(l, nums) == l ? l : -1;
    }

    private int getCnt(int x, int[] nums) {
        int n = nums.length;
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (nums[mid] >= x) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return n - r;
    }

    // 2485 找出中枢整数
    public int pivotInteger(int n) {
        int r = (1 + n) * n / 2;
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
            if (sum == r - sum + i) return i;
        }
        return -1;
    }

    public int pivotInteger2(int n) {
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + i;
        }
        int l = 1, r = n;
        while (l < r) {
            int mid = l + r >> 1;
            if (sum[n] <= sum[mid] + sum[mid - 1]) {
                r = mid;
            } else {
                l = mid + 1;
            }
//            int mid = l + r+1 >> 1;
//            if (sum[n] >= sum[mid] + sum[mid - 1]) {
//                l = mid;
//            } else {
//                r = mid - 1;
//            }
        }
        return sum[l] + sum[l - 1] == sum[n] ? l : -1;
    }

    // 373 查找和最小的K对数字
    // 优先队列查kSmallestPairs
    public List<List<Integer>> kSmallestPairsBinarySearch(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> result = new ArrayList<>();
        int l = nums1[0] + nums2[0], r = nums1[nums1.length - 1] + nums2[nums2.length - 1];
        while (l < r) {
            int mid = (int) ((long) l + r >> 1);
            int cnt = getCnt(nums1, nums2, mid, k);
            //个数大于等于k的最小的和
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        int value = l;
        //把所有小于 最小和(此和是第k或k+i小的值，第k小的一定在小于value的里面)的组合先添加到result
        for (int n1 : nums1) {
            for (int n2 : nums2) {
                if (n1 + n2 < value) {
                    result.add(Arrays.asList(n1, n2));
                } else {
                    break;
                }
            }
        }
        // 此时result的数量可能大于k，需要选k个
        // eg 和的数组是[1,2,3,3,3,4]，求第4小，第四小的3有3个，求最左最右坐标加到result.size=k为止
        for (int i = 0; i < nums1.length && result.size() < k; i++) {
            int target = value - nums1[i];

            int left = 0, right = nums2.length - 1;
            while (left < right) {
                int mid = (int) ((long) left + right) >> 1;
                // 大于等于target的最小的nums2的值
                if (nums2[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            int x = left;
            if (nums2[x] != target) continue;
            left = 0;
            right = nums2.length - 1;
            while (left < right) {
                int mid = (int) ((long) left + right + 1) >> 1;
                //小于等于target的最大的值
                if (nums2[mid] > target) {
                    right = mid - 1;
                } else {
                    left = mid;
                }
            }
            int y = left;
            for (int p = x; p <= y && result.size() < k; p++) {
                result.add(Arrays.asList(nums1[i], nums2[p]));
            }
        }
        return result;
    }

    private int getCnt(int[] nums1, int[] nums2, int mid, int k) {
        int cnt = 0;
        for (int i = 0; i < nums1.length && cnt < k; i++) {
            for (int j = 0; j < nums2.length && cnt < k; j++) {
                if (nums1[i] + nums2[j] <= mid) cnt++;
                else break;
            }
        }
        return cnt;
    }

    //378有序矩阵中第K小的元素
    // 优先队列查kthSmallest
    public int kthSmallestBinarySearch(int[][] matrix, int k) {
        int n = matrix.length;
        int l = matrix[0][0], r = matrix[n - 1][n - 1];
        while (l < r) {
            int mid = (l + r) >> 1;
            // 数量大于等于k的最小的值，小于这个值的数量不足k个
            int cnt = getCnt(matrix, mid);
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    private int getCnt(int[][] matrix, int mid) {
        int n = matrix.length;
        int i = n - 1, j = 0;
        int cnt = 0;
        while (i >= 0 && j < n) {
            if (matrix[i][j] <= mid) {
                cnt += (i + 1);
                j++;
            } else {
                i--;
            }
        }
        return cnt;
    }

    //1439. 有序矩阵中的第 k 个最小数组和
    public int kthSmallest1439binarySearch(int[][] mat, int k) {
        int m = mat.length;
        int[] prev = mat[0];
        for (int i = 1; i < m; ++i) {
            prev = merge2(prev, mat[i], k);
        }
        return prev[k - 1];
    }

    public int[] merge2(int[] f, int[] g, int k) {
        int left = f[0] + g[0], right = f[f.length - 1] + g[g.length - 1], thres = 0;
        k = Math.min(k, f.length * g.length);
        while (left <= right) {
            int mid = (left + right) / 2;
            int rptr = g.length - 1, cnt = 0;
            for (int lptr = 0; lptr < f.length; ++lptr) {
                while (rptr >= 0 && f[lptr] + g[rptr] > mid) {
                    --rptr;
                }
                cnt += rptr + 1;
            }
            if (cnt >= k) {
                thres = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        List<Integer> list = new ArrayList<Integer>();
        int index = 0;
        for (int i = 0; i < f.length; ++i) {
            for (int j = 0; j < g.length; ++j) {
                int sum = f[i] + g[j];
                if (sum < thres) {
                    list.add(sum);
                } else {
                    break;
                }
            }
        }
        while (list.size() < k) {
            list.add(thres);
        }
        int[] ans = new int[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            ans[i] = list.get(i);
        }
        Arrays.sort(ans);
        return ans;
    }

    // 658 找到K个最接近的元素
    public List<Integer> findClosestElementsPriorityQueue(int[] arr, int k, int x) {
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>((o1, o2) -> Math.abs(o1[0] - x) - Math.abs(o2[0] - x) == 0 ? o1[1] - o2[1] : Math.abs(o1[0] - x) - Math.abs(o2[0] - x));
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            priorityQueue.offer(new int[]{arr[i], i});
        }
        while (k-- > 0 && !priorityQueue.isEmpty()) {
            result.add(priorityQueue.poll()[0]);
        }
        result.sort(Comparator.comparingInt(o -> o));
        return result;
    }

    public List<Integer> findClosestElementsSort(int[] arr, int k, int x) {
        int l = Integer.MAX_VALUE, r = Integer.MIN_VALUE;
        for (int value : arr) {
            l = Math.min(l, Math.abs(value - x));
            r = Math.max(r, Math.abs(value - x));
        }
        while (l < r) {
            int mid = l + r >> 1;
            //cnt arr中满足和x的差值<=mid的个数
            int cnt = getCnt(arr, x, mid);
            // 例如 arr[3 4 5] 和x的差值  个数排列是[1 2 2]
            //那么我们需要 数量为k 当中最小的差值，l就是差值
            //diff 1 2 3
            //cnt  2 4 4  k=3 diff=2就是cnt大于等于k的最小差值
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        int close = l;
        List<Integer> result = new ArrayList<>();
        for (int value : arr) {
            if (Math.abs(value - x) < close) result.add(value);
        }
        for (int i = 0; i < arr.length && result.size() < k; i++) {
            if (Math.abs(arr[i] - x) == close) result.add(arr[i]);
        }
        result.sort(Comparator.comparingInt(o -> o));
        return result;
    }


    private int getCnt(int[] arr, int x, int target) {
        int cnt = 0;
        for (int value : arr) {
            if (Math.abs(value - x) <= target) cnt++;
        }
        return cnt;
    }

    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        int l = 0, r = arr.length - 1;
        int removeNum = arr.length - k;
        while (removeNum-- > 0) {
            if (x - arr[l] > arr[r] - x) {
                l++;
            } else {
                r--;
            }
        }
        List<Integer> result = new ArrayList<>();
        for (int i = l; i < l + k; i++) {
            result.add(arr[i]);
        }
        return result;
    }

    public List<Integer> findClosestElementsBinarySearch(int[] arr, int k, int x) {
        int l = 0, r = arr.length - k;
        //查找与x距离最接近的左区间，相等时取小的，即向左收缩
        while (l < r) {
            int mid = l + r >> 1;
            if (x - arr[mid] > arr[mid + k] - x) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        List<Integer> result = new ArrayList<>();
        for (int i = l; i < l + k; i++) {
            result.add(arr[i]);
        }
        return result;
    }

    //668乘法表中第K小的数
    //PriorityQueue MLE
    public int findKthNumber(int m, int n, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (priorityQueue.size() < k) {
                    priorityQueue.offer(i * j);
                } else if (i * j < priorityQueue.peek()) {
                    priorityQueue.poll();
                    priorityQueue.offer(i * j);
                }
            }
        }
        return priorityQueue.poll();
    }

    // 二分法
    public int findKthNumberBinarySearch(int m, int n, int k) {
        int l = 1, r = m * n;
        while (l < r) {
            int mid = (l + r) >> 1;
            //数量大于等于k的最小乘积
            // eg 乘积小于等于20的有3个，乘积小于等于25的有6个，求第5小
            int cnt = getCnt(m, n, mid);
            if (cnt >= k) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private int getCnt(int m, int n, int x) {
        int count = x / n * n;
        for (int i = x / n + 1; i <= m; ++i) {
            count += x / i;
        }
        return count;
//        int res = 0;
//        // 统计每行小于等于 k 的数目
//        for (int i = 1; i <= m; ++i) {
//            res += Math.min(k / i, n);
//        }
//        return res;
    }

    // 878 第N个神奇数字
    public int nthMagicalNumber(int n, int a, int b) {
        int mod = (int) 1e9 + 7;
        long l = Math.min(a, b);
        long r = (long) n * Math.min(a, b);
        long c = lcm(a, b);
        while (l < r) {
            long mid = l + r >> 1;
            if (getCnt(mid, a, b, c) < n) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return (int) (l % mod);
    }

    private int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    private int gcd(int x, int y) {
        return y > 0 ? gcd(y, x % y) : x;
    }


    private long getCnt(long x, int a, int b, long c) {
        return x / a + x / b - x / c;
    }

    //719 找出第K小的数对距离
    public int smallestDistancePair(int[] nums, int k) {
        Arrays.sort(nums);
        int l = 0, r = nums[nums.length - 1] - nums[0];
        //计划查找 >=k的最小值
        //第k个ak 右侧的值都>=k，所以即找>=k的最小的值
        while (l < r) {
            int mid = (l + r) >> 1;
            // cnt <=mid的个数
            int cnt = 0;
            for (int j = 0; j < nums.length; j++) {
                int i = binarySearch(nums, j, nums[j] - mid);
                //[i,j]一共j-i+1个数，一共j-i对儿
                cnt += j - i;
            }
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    //查找>=target中最小的坐标，[i,j]满足距离<= mid
    private int binarySearch(int[] nums, int end, int target) {
        int l = 0, r = end;
        while (l < r) {
            int mid = (l + r) >> 1;
            if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    // 双指针
    public int smallestDistancePairDualPointer(int[] nums, int k) {
        Arrays.sort(nums);
        int l = 0, r = nums[nums.length - 1] - nums[0];
        //计划查找<=k的最大值 />=k的最小值
        while (l < r) {
            int mid = (l + r) >> 1;
            // cnt <=mid的个数
            int cnt = 0;
            for (int i = 0, j = 0; j < nums.length; j++) {
                //i跟j都大于mid，跟j后面的距离更大，i往后移
                while (nums[j] - nums[i] > mid) i++;
                cnt += j - i;
            }
            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    // 1175 质数排列
    int mod = (int) 1e9 + 7;

    public int numPrimeArrangements(int n) {
        int cnt = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                cnt++;
            }
        }
        return (int) (factorial(cnt) * factorial(n - cnt) % mod);
    }

    // 技巧 判断是否是质数
    private boolean isPrime(int n) {
        if (n == 1) return false;
        // n/i 当i大于sqrt(n)时另一个因子肯定小于sqrt(n),所以只遍历到sqrt(n)
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private long factorial(int n) {
        long ans = 1;
        for (int i = 1; i <= n; i++) {
            ans *= i;
            ans %= mod;
        }
        return ans;
    }

    // 打表+二分做法
    public int numPrimeArrangementsBinarySearch(int n) {
        List<Integer> primeList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            if (isPrime(i)) primeList.add(i);
        }
        int l = 0, r = primeList.size() - 1;
        //找到第一个小于等于n的质数的坐标，+1即为小于等于n的质数个数
        while (l < r) {
            int mid = (l + r + 1) >> 1;
            if (primeList.get(mid) <= n) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        int idx = l;
        int cnt = idx + 1;

        return (int) (factorial(cnt) * factorial(n - cnt) % mod);
    }

    // 786 第K小的素数分数
    // 自定义排序
    public int[] kthSmallestPrimeFraction(int[] arr, int k) {
        int n = arr.length;
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                result.add(new int[]{arr[i], arr[j]});
            }
        }
        result.sort((o1, o2) -> o1[0] * o2[1] - o2[0] * o1[1]);
        return result.get(k - 1);
    }

    //多路归并
    public int[] kthSmallestPrimeFractionPriorityQueue(int[] arr, int k) {
        int n = arr.length;
        PriorityQueue<int[]> queue = new PriorityQueue<>((o1, o2) -> arr[o1[0]] * arr[o2[1]] - arr[o1[1]] * arr[o2[0]]);
        for (int i = 1; i < n; i++) {
            queue.offer(new int[]{0, i});
        }
        //arr = [1,2,3,5], k = 3
        while (--k > 0) {
            int[] min = queue.poll();
            int x = min[0], y = min[1];
            if (x + 1 < y) {
                queue.offer(new int[]{x + 1, y});
            }
        }
        return new int[]{arr[queue.peek()[0]], arr[queue.peek()[1]]};
    }

    // 二分
    int a, b;

    public int[] kthSmallestPrimeFractionBinarySearch(int[] arr, int k) {
        double eps = 1e-8;
        double l = 0, r = 1;
        while (r - l > eps) {
            double mid = (l + r) / 2;
            // 大于等于k个的最小值，第k个就是该值
            if (check(arr, mid) >= k) r = mid;
            else l = mid;
        }
        return new int[]{a, b};
    }

    int check(int[] arr, double x) {
        double eps = 1e-8;
        int ans = 0;
        for (int i = 0, j = 1; j < arr.length; j++) {
            while (arr[i + 1] * 1.0 / arr[j] <= x) i++;
            if (arr[i] * 1.0 / arr[j] <= x) ans += i + 1;
            if (Math.abs(arr[i] * 1.0 / arr[j] - x) < eps) {
                a = arr[i];
                b = arr[j];
            }
        }
        return ans;
    }

    // 1818 绝对差值和
    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        int mod = (int) 1e9 + 7;
        int n = nums1.length;
        int[] sorted = nums1.clone();
        Arrays.sort(sorted);
        long sum = 0, maxDiff = 0;
        for (int i = 0; i < n; i++) {
            int x = Math.abs(nums1[i] - nums2[i]);
            if (x == 0) continue;
            sum += x;
            int l = 0, r = n - 1;
            while (l < r) {
                int mid = l + r >> 1;
                if (sorted[mid] >= nums2[i]) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            int newAbs = Math.abs(sorted[l] - nums2[i]);
            if (l > 0) newAbs = Math.min(newAbs, Math.abs(sorted[l - 1] - nums2[i]));
            if (newAbs < x) maxDiff = Math.max(maxDiff, x - newAbs);
        }
        return (int) ((sum - maxDiff) % mod);
    }

    // 1894 找到需要补充粉笔的学生编号
    public int chalkReplacer(int[] chalk, int k) {
        int n = chalk.length;
        if (chalk[0] > k) {
            return 0;
        }
        int[] preSum = new int[n];
        preSum[0] = chalk[0];
        for (int i = 1; i < n; i++) {
            preSum[i] = preSum[i - 1] + chalk[i];
            if (preSum[i] > k) {
                return i;
            }
        }
        k = k % preSum[n - 1];
        // 二分找到第一个比k大的前缀和
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = (l + r) >> 1;
            if (preSum[mid] <= k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    public int chalkReplacer2(int[] chalk, int k) {
        int n = chalk.length;
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + chalk[i - 1];
            if (sum[i] > k) return i - 1;
        }
        k %= sum[n];
        int l = 0, r = n;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            //<=k的最大的数
            if (sum[mid] <= k) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        // l>0?l-1+1  -1对应chalk的坐标，+1找下一个，如果l是0 没有比k小的，就取第一个;如果是n也取第一个 %n
        return l % n;
    }

    // 2055 蜡烛之间的盘子
    // 前缀和做法搜platesBetweenCandles
    public int[] platesBetweenCandles(String s, int[][] queries) {
        int[] ans = new int[queries.length];
        int n = s.length();
        int[] preSum = new int[n];
        int sum = 0;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '|') {
                list.add(i);
            }
            if (s.charAt(i) == '*') {
                sum++;
            }
            preSum[i] = sum;
        }
        if (list.size() == 0) return ans;
        for (int i = 0; i < queries.length; i++) {
            int a = queries[i][0], b = queries[i][1];
            int c = -1, d = -1;
            int l = 0, r = list.size() - 1;
            // 找到 a 右边最近的蜡烛
            while (l < r) {
                int mid = (l + r) >> 1;
                if (list.get(mid) >= a) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            if (list.get(r) >= a) c = list.get(r);
            else continue;
            // 找到 b 左边最近的蜡烛
            l = 0;
            r = list.size() - 1;
            while (l < r) {
                int mid = (l + r + 1) >> 1;
                if (list.get(mid) <= b) {
                    l = mid;
                } else {
                    r = mid - 1;
                }
            }
            if (list.get(r) <= b) d = list.get(r);
            else continue;
            if (c < d) ans[i] = preSum[d] - preSum[c];
        }
        return ans;
    }

    // 6133 分组的最大数量
    public int maximumGroups(int[] grades) {
        int n = grades.length;
        long l = 1, r = n;
        while (l < r) {
            long mid = l + r + 1 >> 1;
            //分成m组至少需要mid * (mid + 1) / 2 个人
            if (mid * (mid + 1) / 2 <= n) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return (int) l;
    }

    //2439. 最小化数组中的最大值
    public int minimizeArrayValue(int[] nums) {
        // [l,r] 使nums每个数在转移后都小于等于的值k
        // 求该值最小，r向左收缩（k满足大于等于结果的最小值）
        int l = 0, r = (int) 1e9;
        while (l < r) {
            int mid = l + r >> 1;
            if (canTransfer(nums, mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private boolean canTransfer(int[] nums, int k) {
        long have = 0;//前方的数字还可以帮我们后方的大数承载多少数字
        for (int n : nums) {
            if (n <= k) {
                have += k - n;//较小数，可以算入承载量
            } else {
                if (have < n - k) return false;//承载不了了，该答案不可行
                else have -= (n - k);//减去承载量
            }
        }
        return true;
    }

    //2423. 删除字符使频率相同
    public boolean equalFrequency(String word) {
        int[] cnt = new int[26];
        for (char c : word.toCharArray()) {
            cnt[c - 'a']++;
        }
        Map<Integer, Integer> freCnt = new HashMap<>();
        for (int fre : cnt) {
            if (fre == 0) continue;
            freCnt.put(fre, freCnt.getOrDefault(fre, 0) + 1);
        }
        for (int fre : cnt) {
            if (fre == 0) continue;
            freCnt.put(fre, freCnt.get(fre) - 1);
            if (freCnt.get(fre) == 0) freCnt.remove(fre);
            if (fre - 1 > 0) {
                freCnt.put(fre - 1, freCnt.getOrDefault(fre - 1, 0) + 1);
            }
            if (freCnt.size() == 1) return true;
            if (fre - 1 > 0) {
                freCnt.put(fre - 1, freCnt.get(fre - 1) - 1);
                if (freCnt.get(fre - 1) == 0) freCnt.remove(fre - 1);
            }
            freCnt.put(fre, freCnt.getOrDefault(fre, 0) + 1);
        }
        return false;
    }

    //2432. 处理用时最长的那个任务的员工
    public int hardestWorker(int n, int[][] logs) {
        int lastTime = 0;
        int maxTime = 0;
        int maxId = -1;
        for (int[] log : logs) {
            if (log[1] - lastTime > maxTime) {
                maxTime = log[1] - lastTime;
                maxId = log[0];
            } else if (log[1] - lastTime == maxTime && log[0] < maxId) {
                maxId = log[0];
            }
            lastTime = log[1];
        }
        return maxId;
    }

    //2441. 与对应负数同时存在的最大正整数
    public int findMaxK(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int max = -1;
        for (int num : nums) {
            if (num > 0 && set.contains(-num)) {
                max = Math.max(num, max);
            }
        }
        return max;
    }

    //2513. 最小化两个数组中的最大值
    public int minimizeSet(int d1, int d2, int n, int m) {
        long l = n + m, r = Integer.MAX_VALUE;//v[l,r]是满足条件的v，大于等于v的全都符合条件，小于v全部不符合题目条件
        // v越大，能满足条件的数越多，两个数组的最大值就越大
        while (l < r) {
            long mid = l + r >> 1;
            if (check(d1, d2, n, m, mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return (int) l;
    }

    // 计算[1，v]中有多少数可以选，此时v就是最大值
    private boolean check(long d1, long d2, long n, long m, long v) {
        long c1 = v / d1; // c1表示[1,v]中能被d1整除的个数
        long c2 = v / d2; // c2表示[1,v]中能被d2整除的个数
        long cc = v / lcm((int) d1, (int) d2); // cc 表示能被d1、d2同时整除的个数
        // c1和c2都减去同时被整除的这些数
        c1 -= cc;
        c2 -= cc;
        // 最终结果也减去同时被整除的这些数
        v -= cc;
        // c1是被d1整除的个数，可以放在arr2中，如果大于m，最终结果减去超过m的个数(即c1-m)
        if (c1 > m) {
            v -= (c1 - m);
        }
        // c2是被d2整除的个数，可以放在arr1中，如果大于n，最终结果减去超过n的个数(即c2-n)
        if (c2 > n) {
            v -= (c2 - n);
        }
        //最终的结果个数大于n+m 即为满足条件的v
        return v >= (n + m);
    }

    //2528. 最大化城市的最小供电站数目
    // 二分+前缀和+差分
    public long maxPower(int[] stations, int range, int k) {
        int n = stations.length;
        long[] sum = new long[n + 1]; // 前缀和
        for (int i = 0; i < n; ++i) {
            sum[i + 1] = sum[i] + stations[i];
        }
        long min = Long.MAX_VALUE;
        long[] power = new long[n]; // 电量
        for (int i = 0; i < n; ++i) {
            power[i] = sum[Math.min(i + range + 1, n)] - sum[Math.max(i - range, 0)];
            min = Math.min(min, power[i]);
        }

        long l = min, r = min + k;
        while (l < r) {
            long mid = l + r + 1 >> 1;
            if (check(mid, power, n, range, k)) l = mid;
            else r = mid - 1;
        }
        return l;
    }

    private boolean check(long minPower, long[] power, int n, int range, int k) {
        long[] diff = new long[n + 1]; // 差分数组
        long sumD = 0, need = 0;
        for (int i = 0; i < n; ++i) {
            sumD += diff[i]; // 累加差分值
            long m = minPower - power[i] - sumD;
            if (m > 0) { // 需要 m 个供电站
                need += m;
                if (need > k) return false; // 提前退出这样快一些
                sumD += m; // 差分更新
                if (i + range * 2 + 1 < n) diff[i + range * 2 + 1] -= m; // 差分更新
            }
        }
        return true;
    }

    //6355. 质数减法运算
    public boolean primeSubOperation(int[] nums) {
        int n = nums.length;
        int[] primes = new int[]{
                2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107,
                109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223,
                227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337,
                347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457,
                461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593,
                599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719,
                727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857,
                859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997
        };
        for (int i = n - 2; i >= 0; i--) {
            int next = nums[i + 1];
            int cur = nums[i];
            if (cur < next) continue;
            // 至少减少
            int needMinus = cur - next + 1;
            // 找到大于等于needMinus的第一个质数
            int prime = findPrime(needMinus, primes);
            if (prime >= nums[i] || prime < needMinus) return false;
            nums[i] -= prime;
        }
        return true;
    }

    private int findPrime(int x, int[] primes) {
        int l = 0, r = primes.length - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (primes[mid] >= x) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return primes[l];
    }

    //2476. 二叉搜索树最近节点查询
    public List<List<Integer>> closestNodes(TreeNode root, List<Integer> queries) {
        List<Integer> list = new ArrayList<>();
        dfs(root, list);
        List<List<Integer>> result= new ArrayList<>();
        for (int q : queries) {
            result.add(Arrays.asList(binarySearch(list,q),binarySearch2(list,q)));
        }
        return result;
    }

    private int binarySearch(List<Integer> list, int x) {
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (list.get(mid) <= x) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return list.get(l) <= x ? list.get(l) : -1;
    }

    private int binarySearch2(List<Integer> list, int x) {
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int mid = l + r >> 1;
            if (list.get(mid) >= x) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return list.get(l) >= x ?list.get(l) : -1;
    }

    private void dfs(TreeNode root, List<Integer> list) {
        if (root == null) return;
        dfs(root.left, list);
        list.add(root.val);
        dfs(root.right, list);
    }

    //2861. 最大合金数
    public int maxNumberOfAlloys(int n, int k, int budget, List<List<Integer>> composition, List<Integer> stock, List<Integer> cost) {
        int left = 1, right = 200000000, ans = 0;
        while (left <= right) {
            int mid = (left + right) / 2;
            boolean valid = false;
            for (int i = 0; i < k; ++i) {
                long spend = 0;
                for (int j = 0; j < n; ++j) {
                    spend += Math.max((long) composition.get(i).get(j) * mid - stock.get(j), 0) * cost.get(j);
                }
                if (spend <= budget) {
                    valid = true;
                    break;
                }
            }
            if (valid) {
                ans = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }

    //2386. 找出数组的第 K 大和
    int cnt2386;
    public long kSum(int[] nums, int k) {
        int n = nums.length;
        long total = 0, total2 = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] >= 0) {
                total += nums[i];
            } else {
                nums[i] = -nums[i];
            }
            total2 += Math.abs(nums[i]);
        }
        Arrays.sort(nums);

        long left = 0, right = total2;
        while (left <= right) {
            long mid = (left + right) / 2;
            cnt2386 = 0;
            dfs2386(nums, k, n, 0, 0, mid);
            if (cnt2386 >= k - 1) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return total - left;
    }

    public void dfs2386(int[] nums, int k, int n, int i, long t, long limit) {
        if (i == n || cnt2386 >= k - 1 || t + nums[i] > limit) {
            return;
        }
        cnt2386++;
        dfs2386(nums, k, n, i + 1, t + nums[i], limit);
        dfs2386(nums, k, n, i + 1, t, limit);
    }

    //2258. 逃离火灾
    boolean ok;

    public int maximumMinutes(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] fireGrid = new int[m][n];
        int[][] personGrid = new int[m][n];
        if (!check(0, grid, fireGrid, personGrid)) return -1;
        int l = 0, r = m * n;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (check(mid, grid, fireGrid, personGrid)) l = mid;
            else r = mid - 1;
        }
        return r == m * n ? (int) 1e9 : r;
    }

    boolean check(int t, int[][] grid, int[][] fireGrid, int[][] personGrid) {
        ok = false;
        int m = grid.length, n = grid[0].length;
        Deque<int[]> fire = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                fireGrid[i][j] = personGrid[i][j] = 0;
                if (grid[i][j] == 1) {
                    fireGrid[i][j] = 1;
                    fire.offerLast(new int[]{i, j});
                }
            }
        }
        while (t-- > 0) {
            update(fire, true, 0, grid, fireGrid, personGrid);  // 先执行 t 秒的火势蔓延
        }
        if (fireGrid[0][0] != 0) return false;
        Deque<int[]> people = new ArrayDeque<>();
        personGrid[0][0] = 1;
        people.addLast(new int[]{0, 0});
        while (!people.isEmpty()) {
            // 先火后人, 同步进行
            update(fire, true, 1, grid, fireGrid, personGrid);
            update(people, false, 1, grid, fireGrid, personGrid);
            if (ok) return true;
        }
        return false;
    }

    void update(Deque<int[]> deque, boolean isFire, int offset, int[][] grid, int[][] fireGrid, int[][] personGrid) {
        int[][] directions = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        int m = grid.length, n = grid[0].length;
        int sz = deque.size();
        while (sz-- > 0) {
            int[] info = deque.pollFirst();
            int x = info[0], y = info[1];
            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n) continue;
                if (grid[nx][ny] == 2) continue;
                if (isFire) {
                    if (fireGrid[nx][ny] != 0) continue;
                    fireGrid[nx][ny] = fireGrid[x][y] + offset;
                } else {
                    if (nx == m - 1 && ny == n - 1 &&
                            (fireGrid[nx][ny] == 0 || fireGrid[nx][ny] == personGrid[x][y] + offset))
                        ok = true;  // 火尚未到达 或 同时到达
                    if (fireGrid[nx][ny] != 0 || personGrid[nx][ny] != 0) continue;
                    personGrid[nx][ny] = personGrid[x][y] + offset;
                }
                deque.addLast(new int[]{nx, ny});
            }
        }
    }

    //endregion-------------------------------------------------------------------------------------------
}
