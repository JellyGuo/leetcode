public class RandomWeight {
    int[] sum;

    public RandomWeight(int[] w) {
        sum = new int[w.length];
        sum[0] = w[0];
        for (int i = 1; i < w.length; i++) {
            sum[i] = sum[i - 1] + w[i];
        }
    }

    public int pickIndex() {
        int total = sum[sum.length - 1];
        int x = (int) (Math.random() * total) + 1;
        return binarySearch(x);
    }

    private int binarySearch(int x) {
        int l = 0, r = sum.length - 1;
        while (l < r) {
            //x<=sum[i] return i
            int mid = l + r >> 1;
            if (sum[mid] >= x) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }
}
