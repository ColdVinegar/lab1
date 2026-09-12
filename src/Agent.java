import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Agent {
    agentType type;
    int health = 10;
    int splitLimit = 20;
    int x, y;
    boolean moved = false;

    Agent(agentType type, int x, int y){
        this.type = type;
        this.x = x;
        this.y = y;
    }
    Agent(agentType type, int x, int y, int health, int lim){
        this.type = type;
        this.x = x;
        this.y = y;
        this.health = health;
        this.splitLimit = lim;
    }

    List<int[]> lookNear(Environment env){
        List<int[]> res = new ArrayList<>();
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                if (i == 0 || j == 0) {
                    int X = this.x + i;
                    int Y = this.y + j;
                    if (X >= 0 && Y >= 0 && X < env.length && Y < env.height) {
                        if (env.world[Y][X] == null) {
                            res.add(new int[]{X, Y});
                        }
                    }
                }
            }
        }
        return res;
    }

    void proceed(Environment env){
        switch (this.type){
            case PLANT -> this.health++;
            default -> this.health--;
        }
        if (this.health >= this.splitLimit){
            List<int[]> available = lookNear(env);
            if (!available.isEmpty()){
                Random random = new Random();
                int rnd = random.nextInt(available.size());
                switch (this.type){
                    case PLANT -> env.add(new Plant(this.type, available.get(rnd)[0], available.get(rnd)[1], (int) this.health/2, this.splitLimit));
                    case PREY -> env.add(new Prey(this.type, available.get(rnd)[0], available.get(rnd)[1], (int) this.health/2, this.splitLimit));
                    case PREDATOR -> env.add(new Predator(this.type, available.get(rnd)[0], available.get(rnd)[1], (int) this.health/2, this.splitLimit));
                }
                this.health /= 2;
            }
        }
    }

    void move(int[] target, List<int[]> available){
        int deltaX = this.x-target[0], deltaY = this.y-target[1];
        if (deltaX == 0 && deltaY == 0) return;

        int[] newpoint = {this.x, this.y};
        int X_avail = 0, Y_avail = 0;

        for (int[] avail: available){
            if (avail[0] != this.x) X_avail++;
            if (avail[1] != this.y) Y_avail++;
        }

        if ((Math.abs(deltaX) > Math.abs(deltaY) && X_avail > 0) || (Math.abs(deltaX) < Math.abs(deltaY) && Y_avail == 0)){
            int newX = this.x;
            if (X_avail == 2) newX -= (Math.abs(deltaX)/deltaX);
            newpoint[0] = newX;
        }
        else{
            int newY = this.y;
            if (Y_avail == 2) newY -=(Math.abs(deltaY)/deltaY);
            newpoint[1] = newY;
        }

    }

    void move(Environment env, int[] target){
        if (env.world[target[1]][target[0]] != null) this.health += env.world[target[1]][target[0]].health;
        env.world[target[1]][target[0]] = env.world[this.y][this.x];
        env.world[this.y][this.x] = null;
        if (this.x-target[0] < 0 || this.y-target[1] < 0) this.moved = true;
        this.x = target[0];
        this.y = target[1];

        System.out.println("Health: "+this.health);
        System.out.println("Move from: "+this.x+", "+this.y);
        System.out.println("To: "+this.x+", "+this.y);
    }

    boolean isAlive(Environment env){
        if (this.health == 0){
            env.world[this.y][this.x] = null;
            return false;
        }
        return true;
    }
}

enum agentType{
    PLANT,
    PREY,
    PREDATOR
}