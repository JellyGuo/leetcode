import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//1797. 设计一个验证系统
class AuthenticationManager {
    Map<String, Integer> map;
    int timeToLive;

    public AuthenticationManager(int timeToLive) {
        map = new HashMap<>();
        this.timeToLive = timeToLive;
    }

    public void generate(String tokenId, int currentTime) {
        map.put(tokenId, currentTime + timeToLive);
    }

    public void renew(String tokenId, int currentTime) {
        if (!map.containsKey(tokenId)) return;
        if (map.get(tokenId) <= currentTime) {
            map.remove(tokenId);
            return;
        }
        map.put(tokenId, currentTime + timeToLive);
    }

    public int countUnexpiredTokens(int currentTime) {
        Set<String> removeKey = new HashSet<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() <= currentTime) removeKey.add(entry.getKey());
        }
        for(String key:removeKey){
            map.remove(key);
        }
        return map.size();
    }
}