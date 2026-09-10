import java.util.Random;

public class Main {
    static int plants_amount = 5;
    static int preys_amount = 5;
    static int predators_amount = 5;

    public static void main(String[] args) {
        Agent agent = new Agent(agentType.PLANT, 5, 10);
        Environment env = new Environment(20, 20);
        spawn(agentType.PLANT, plants_amount, env);
        spawn(agentType.PREY, preys_amount, env);
        spawn(agentType.PREDATOR, predators_amount, env);
        env.draw();
    }

    static void spawn(agentType type, int count, Environment env){
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                x = random.nextInt(env.length);
                y = random.nextInt(env.height);
            }while (env.world[y][x] != null);
            env.add(new Agent(type, x, y));
        }
    }
}
