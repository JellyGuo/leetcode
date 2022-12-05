import java.util.Random;

// 洗牌算法
// 思路：在前n-1张牌洗好的情况下，第n张牌随机与前n-1张牌中的其中一张交换或者不换，即为随机洗牌
// 共有n个不同的数，根据每个位置能够选择什么数，共有n！种组合
// 对于下标x而言，我们从[x,n-1]中随机出一个位置与x进行值交换，当所有位置都进行这样的处理后，我们便得到一个公平的洗牌方案
// 对于下标位0 的位置，从[0,n-1]随机一个位置进行交换，共有n中选择：下标为1 从[1,n-1]选择共有n-1中选择
public class RandomArray {
    int[] nums;
    int n;
    Random random;

    public RandomArray(int[] nums) {
        this.nums = nums;
        this.n = nums.length;
        random = new Random();
    }

    public int[] reset() {
        return nums;
    }

    public int[] shuffle() {
        int[] array = nums.clone();
        for (int i = 0; i < n; i++) {
            int j = i + random.nextInt(n - i);
            swap(array, i, j);
        }
        return array;
    }

    private void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] -= tmp;
    }
}
