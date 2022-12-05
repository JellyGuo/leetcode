//2069. 模拟行走机器人 II
class Robot {
    String[] directions = new String[]{"East", "North", "West", "South"};
    int width, height, loc;
    boolean moved;
    int[] infos;

    public Robot(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void step(int num) {
        moved = true;
        loc += num;
        loc = loc % (2 * (width - 1 + height - 1));
        move();
    }

    public int[] getPos() {
        if (moved) {
            return new int[]{infos[0],infos[1]};
        }
        return new int[]{0, 0};
    }

    public String getDir() {
        if (moved) {
            if(infos[0] == 0 && infos[1] == 0){
                return directions[3];
            }
            return directions[infos[2]];
        }
        return directions[0];
    }

    private void move() {
        if (loc <= width - 1) {
            infos = new int[]{loc, 0, 0};
        } else if (loc <= width - 1 + height - 1) {
            infos = new int[]{width - 1, loc - width + 1, 1};
        } else if (loc <= 2 * (width - 1) + height - 1) {
            infos = new int[]{width - 1 - (loc - height + 1 - width + 1), height - 1, 2};
        } else {
            infos = new int[]{0, (height - 1) - (loc - (2 * (width - 1) + (height - 1))), 3};
        }
    }
}