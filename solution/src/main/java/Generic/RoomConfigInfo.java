package Generic;

import java.util.Set;

public class RoomConfigInfo<T extends RoomConfig> implements SpecialFacility{
    // {"roomConfigInfo":[T]}
    // T = {"roomId":1,"eSportInfo":K}
    // K = {"computerInfo":{"ram":1,"cpu":2}}
    // {"roomConfigInfo":[{"roomId":1,"eSportInfo":{"computerInfo":{"ram":1,"cpu":2}}}]}
    private Set<T> roomConfigInfo;

    @Override
    public Set<String> attributes() {
        return null;
    }

    public Set<T> getRoomConfigInfo() {
        return roomConfigInfo;
    }

    public void setRoomConfigInfo(Set<T> roomConfigInfo) {
        this.roomConfigInfo = roomConfigInfo;
    }
}
