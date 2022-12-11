
//6259. 设计内存分配器
public class Allocator {
    private final int[] mem;
    private final int n;

    public Allocator(int n) {
        this.n = n;
        mem = new int[n];
    }

    public int allocate(int size, int mID) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            if ((count = mem[i] > 0 ? 0 : count + 1) == size) {
                for (int j = i - size + 1; j <= i; j++) {
                    mem[j] = mID;
                }
                return i - size + 1;
            }
        }
        return -1;
    }

    public int free(int mID) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (mem[i] == mID) {
                mem[i] = 0;
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Allocator allocator = new Allocator(10);
        //[[7],[7,8],[8,7],[6,2],[9],[8],[7,6],[9],[10,9]]
        System.out.println(allocator.allocate(1, 1));
        System.out.println(allocator.allocate(1, 2));
        System.out.println(allocator.allocate(1, 3));
        System.out.println(allocator.free(2));
        System.out.println(allocator.allocate(3, 4));
        System.out.println(allocator.allocate(1, 1));
        System.out.println(allocator.allocate(1, 1));
        System.out.println(allocator.free(1));
        System.out.println(allocator.allocate(10, 2));
        System.out.println(allocator.free(7));
    }
}
