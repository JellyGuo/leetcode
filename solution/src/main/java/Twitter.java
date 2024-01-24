import java.util.*;

//355. 设计推特
public class Twitter {
    Map<Integer, Set<Integer>> userFollows;
    Map<Integer, Set<Integer>> userTweets;
    Map<Integer, Integer> tweetTime;
    int timestamp;

    public Twitter() {
        userFollows = new HashMap<>();
        userTweets = new HashMap<>();
        tweetTime = new HashMap<>();
    }

    public void postTweet(int userId, int tweetId) {
        Set<Integer> tweets = userTweets.computeIfAbsent(userId, k -> new HashSet<>());
        tweets.add(tweetId);
        tweetTime.put(tweetId, timestamp++);
    }

    public List<Integer> getNewsFeed(int userId) {
        Set<Integer> follows = userFollows.getOrDefault(userId, new HashSet<>());
        follows.add(userId);
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> o2[1]-o1[1]);
        for (int follow : follows) {
            for (int tweet : userTweets.getOrDefault(follow, new HashSet<>())) {
                pq.offer(new int[]{tweet, tweetTime.get(tweet)});
            }
        }
        List<Integer> res = new ArrayList<>();
        int cnt = 10;
        while (!pq.isEmpty() && cnt-- > 0) {
            res.add(pq.poll()[0]);
        }
        return res;
    }

    public void follow(int followerId, int followeeId) {
        Set<Integer> follows = userFollows.computeIfAbsent(followerId, k -> new HashSet<>());
        follows.add(followeeId);
    }

    public void unfollow(int followerId, int followeeId) {
        Set<Integer> follows = userFollows.computeIfAbsent(followerId, k -> new HashSet<>());
        follows.remove(followeeId);
    }
}
