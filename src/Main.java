import java.util.Random;
import java.util.Scanner;

public class Main {
    static int plants_amount = 2;
    static int preys_amount = 1;
    static int predators_amount = 1;

    public static void main(String[] args) {
        Agent agent = new Agent(agentType.PLANT, 5, 10);
        Environment env = new Environment(20, 20);
        spawn(agentType.PLANT, plants_amount, env);
        spawn(agentType.PREY, preys_amount, env);
        spawn(agentType.PREDATOR, predators_amount, env);
        for (int i = 1; i > 0; i++){
            env.proceed();
            env.draw();
            System.out.println("Шаг: " + i);
            //new Scanner(System.in).nextLine();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
                case PREY -> agnt = new Prey(type, x, y, 100, 150);
                case PREDATOR -> agnt = new Predator(type, x, y, 100, 150);
            }
            env.add(agnt);
        }
    }
}
