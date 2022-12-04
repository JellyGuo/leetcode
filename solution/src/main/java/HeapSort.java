public class HeapSort {


    public void buildMaxHeap(int[] nums, int heapSize) {
        for (int i = (heapSize >>> 1) - 1; i >= 0; i--) {
            maxheapify(nums, i, heapSize);
        }
    }

    private void maxheapify(int[] nums, int i, int heapSize) {
        int l = 2 * i + 1, r = 2 * i + 2, largest = i;
        if (l < heapSize && nums[l] > nums[largest]) {
            largest = l;
        }
        if (r < heapSize && nums[r] > nums[largest]) {
            largest = r;
        }
        if (largest != i) {
            swap(nums, i, largest);
            maxheapify(nums, largest, heapSize);
        }
    }

    private void swap(int[] nums, int i, int largest) {
        int tmp = nums[i];
        nums[i] = nums[largest];
        nums[largest] = tmp;
    }
}
