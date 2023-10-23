package design;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataSink<T extends SinkData> implements Runnable,DataSink<T> {
    private List<T> datas = new ArrayList<>();
    @Override
    public Sink<T> addData(T data) {
        datas.add(data);
        return this;
    }

    @Override
    public void run() {
        saveData();
    }

    abstract void saveData();
}
