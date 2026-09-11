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
        int[] target = lookAround(env);
        int deltaX = this.x-target[0], deltaY = this.y-target[1];
        if (deltaX == 0 && deltaY == 0) return;
        System.out.println("Health: "+this.health);
        System.out.println("Target: "+target[0]+", "+target[1]+", dX "+deltaX+", xY "+deltaY);
        System.out.println("Move from: "+this.x+", "+this.y);
        if (Math.abs(deltaX) > Math.abs(deltaY)){
            int newX = this.x - (Math.abs(deltaX)/deltaX);
            if (env.world[this.y][newX] != null) this.health += env.world[this.y][newX].health;
            env.world[this.y][newX] = env.world[this.y][this.x];
            env.world[this.y][this.x] = null;
            this.x = newX;
        }
        else{
            int newY = this.y - (Math.abs(deltaY)/deltaY);
            if (env.world[newY][this.x] != null) this.health += env.world[newY][this.x].health;
            env.world[newY][this.x] = env.world[this.y][this.x];
            env.world[this.y][this.x] = null;
            this.y = newY;
        }
        System.out.println("To: "+this.x+", "+this.y);
        if (deltaX < 0 || deltaY < 0) this.moved = true;
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
        if (dist < 5){
            return nearestPrey;
        }
        if (!available.isEmpty()){
            Random random = new Random();
            int rnd = random.nextInt(available.size());
            return available.get(rnd);
        }
        return new int[]{this.x, this.y};
    }
}
