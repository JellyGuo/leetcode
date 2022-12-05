import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

//2353 设计食物评分系统
class FoodRatings {

    class FoodWithRate {
        String food;
        Integer rate;

        public FoodWithRate(String food, Integer rate) {
            this.food = food;
            this.rate = rate;
        }
    }

    Map<String, PriorityQueue<FoodWithRate>> c_f_map;
    Map<String, String> f_c_map;
    Map<String, FoodWithRate> f_idx_map;

    public FoodRatings(String[] foods, String[] cuisines, int[] ratings) {
        c_f_map = new HashMap<>();
        f_c_map = new HashMap<>();
        f_idx_map = new HashMap<>();
        int n = foods.length;
        for (int i = 0; i < n; i++) {
            f_c_map.put(foods[i], cuisines[i]);
            FoodWithRate fr = new FoodWithRate(foods[i], ratings[i]);
            f_idx_map.put(foods[i], fr);
            PriorityQueue<FoodWithRate> priorityQueue = c_f_map.getOrDefault(cuisines[i], new PriorityQueue<>((o1, o2) -> o1.rate.equals(o2.rate) ? o1.food.compareTo(o2.food) : o2.rate - o1.rate));
            priorityQueue.offer(fr);
            c_f_map.put(cuisines[i], priorityQueue);
        }
    }

    public void changeRating(String food, int newRating) {
        FoodWithRate fr = f_idx_map.get(food);
        PriorityQueue<FoodWithRate> priorityQueue = c_f_map.get(f_c_map.get(food));
        priorityQueue.remove(fr);
        fr.rate = newRating;
        priorityQueue.offer(fr);
    }

    public String highestRated(String cuisine) {
        return c_f_map.get(cuisine).peek().food;
    }
}