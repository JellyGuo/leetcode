//面试题 03.01. 三合一
class TripleInOne {
    int[] nums;
    int[] pts;
    int stackSize;

    public TripleInOne(int stackSize) {
        this.stackSize = stackSize;
        nums = new int[3 * stackSize];
        pts = new int[]{0, stackSize, 2 * stackSize};
    }

    public void push(int stackNum, int value) {
        if (pts[stackNum] < (stackNum + 1) * stackSize) {
            nums[pts[stackNum]++] = value;
        }
    }

    public int pop(int stackNum) {
        int pt = pts[stackNum] - 1;
        if (pt >= stackNum * stackSize) {
            pts[stackNum]--;
            return nums[pts[stackNum]];
        } else {
            return -1;
        }
    }

    public int peek(int stackNum) {
        int vpt = pts[stackNum] - 1;
        if (vpt >= stackNum * stackSize) {
            return nums[vpt];
        } else {
            return -1;
        }
    }

    public boolean isEmpty(int stackNum) {
        return pts[stackNum] == stackNum * stackSize;
    }
}
