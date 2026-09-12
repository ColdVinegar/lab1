import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Predator extends Agent{
    Predator(agentType type, int x, int y) {
        super(type, x, y);
    }
    Predator(agentType type, int x, int y, int health, int lim){
        super(type, x, y, health, lim);
    }

    @Override
    void proceed(Environment env){
        if (!isAlive(env)) return;
        super.move(env, lookAround(env));
        super.proceed(env);
    }

    private int[] lookAround(Environment env){
        int[] nearestPrey = {-1, -1};
        double dist = 5;
        List<int[]> available = new ArrayList<>();
        for (int i = -2; i <= 2; i++){
            for (int j = -2; j <= 2; j++){
                if (i == 0 && j == 0) continue;
                int X = this.x + i;
                int Y = this.y + j;
                if (X >= 0 && Y >= 0 && X < env.length && Y < env.height) {
                    if ((i == 0 || j == 0) && Math.abs(i+j) == 1 && env.world[Y][X] == null) {
                        available.add(new int[]{X, Y});
                        continue;
                    }
                    if (env.world[Y][X] == null) continue;
                    if (env.world[Y][X].type == agentType.PREY) {
                        System.out.println("Found PREY! We at pos: "+this.x+", "+this.y);
                        double newdist = Math.sqrt(Math.pow((this.y-Y), 2)+Math.pow((this.x-X), 2));
                        System.out.println("He at pos: "+X+", "+Y+" ("+newdist+")");
                        if (newdist < dist){
                            nearestPrey[0] = X;
                            nearestPrey[1] = Y;
                            dist = newdist;
                        }
                    }
                }
            }
        }

        int[] target;

        if (!available.isEmpty()){
            if (dist < 5){
                target = nearestPrey;
            }
            else {
                Random random = new Random();
                int rnd = random.nextInt(available.size());
                target = available.get(rnd);
            }
        }
        else return target = new int[]{this.x, this.y};

        int deltaX = this.x-target[0], deltaY = this.y-target[1];

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
        return newpoint;
    }
}
