import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plant extends Agent{
    Plant(agentType type, int x, int y) {
        super(type, x, y, 1);
    }
    Plant(agentType type, int x, int y, int health){
        super(type, x, y, health);
    }

    @Override
    void proceed(Environment env){
        this.health++;
        if (this.health >= 10){
            List<int[]> available = lookAround(env, this.x, this.y);
            if (!available.isEmpty()){
                Random random = new Random();
                int rnd = random.nextInt(available.size());
                env.add(new Plant(agentType.PLANT, available.get(rnd)[0], available.get(rnd)[1], (int) this.health/2));
                this.health /= 2;
            }
        }
    }

    private List<int[]> lookAround(Environment env, int x, int y){
        List<int[]> res = new ArrayList<>();
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                if (i == 0 || j == 0) {
                    int X = x + i;
                    int Y = y + j;
                    if (X > 0 && Y > 0 && X < env.length && Y < env.height) {
                        if (env.world[Y][X] == null) {
                            res.add(new int[]{X, Y});
                        }
                    }
                }
            }
        }
        return res;
    }
}
