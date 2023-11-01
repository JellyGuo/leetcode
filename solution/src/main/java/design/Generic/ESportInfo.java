package design.Generic;

public class ESportInfo<K> extends RoomConfigInfo<ESportInfo.ESport<K>>{
    // {"roomId":1,"eSportInfo":K}
    public static class ESport<K> extends RoomConfig{
        private K eSportInfo;

        public K geteSportInfo() {
            return eSportInfo;
        }

        public void seteSportInfo(K eSportInfo) {
            this.eSportInfo = eSportInfo;
        }
    }
}
