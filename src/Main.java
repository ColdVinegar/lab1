import java.util.Random;
import java.util.Scanner;

public class Main {
    static int plants_amount = 5;
    static int preys_amount = 0;
    static int predators_amount = 0;

    public static void main(String[] args) {
        Agent agent = new Agent(agentType.PLANT, 5, 10);
        Environment env = new Environment(20, 20);
        spawn(agentType.PLANT, plants_amount, env);
        spawn(agentType.PREY, preys_amount, env);
        spawn(agentType.PREDATOR, predators_amount, env);
        for (int i = 1; i < 30; i++){
            System.out.flush();
            env.draw();
            env.proceed();
            System.out.println("Шаг: " + i);
            new Scanner(System.in).nextLine();
        }
    }

    static void spawn(agentType type, int count, Environment env){
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                x = random.nextInt(env.length);
                y = random.nextInt(env.height);
            }while (env.world[y][x] != null);
            Agent agnt = null;
            switch (type){
                case PLANT -> agnt = new Plant(type, x, y);
                case PREY -> agnt = new Prey(type, x, y);
                case PREDATOR -> agnt = new Predator(type, x, y);
            }
            env.add(agnt);
        }
    }
}
