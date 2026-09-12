import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Prey extends Agent{
    Prey(agentType type, int x, int y) {
        super(type, x, y);
    }
    Prey(agentType type, int x, int y, int health, int lim){
        super(type, x, y, health, lim);
    }

    @Override
    void proceed(Environment env){
        if (!isAlive(env)) return;
        super.move(env, lookAround(env));
        super.proceed(env);
    }

    private int[] lookAround(Environment env){
        int[] nearestPlant = {-1, -1};
        double dist = 5;
        List<int[]> available = new ArrayList<>();
        List<int[]> predators = new ArrayList<>();
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
                    if (env.world[Y][X].type == agentType.PLANT) {
                        System.out.println("Found PLANT! We at pos: "+this.x+", "+this.y);
                        double newdist = Math.sqrt(Math.pow((this.y-Y), 2)+Math.pow((this.x-X), 2));
                        System.out.println("He at pos: "+X+", "+Y+" ("+newdist+")");
                        if (newdist < dist){
                            nearestPlant[0] = X;
                            nearestPlant[1] = Y;
                            dist = newdist;
                        }
                    } else if (env.world[Y][X].type == agentType.PREDATOR) {
                        predators.add(new int[]{X, Y});
                    }
                }
            }
        }
        if (!predators.isEmpty()){

            return
        }
        if (dist < 5){
            return nearestPlant;
        }
        if (!available.isEmpty()){
            Random random = new Random();
            int rnd = random.nextInt(available.size());
            return available.get(rnd);
        }
        return new int[]{this.x, this.y};
    }
}
