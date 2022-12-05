import java.util.ArrayList;
import java.util.List;

//1656. 设计有序流
class OrderedStream {
    String[] array;
    int ptr;

    public OrderedStream(int n) {
        ptr = 1;
        array = new String[n + 1];
    }

    public List<String> insert(int idKey, String value) {
        array[idKey] = value;
        List<String> result = new ArrayList<>();
        if (idKey != ptr) return result;
        while (ptr < array.length && array[ptr] != null) {
            ptr++;
        }
        for (int i = idKey; i < ptr; i++) {
            result.add(array[i]);
        }
        return result;
    }
}

