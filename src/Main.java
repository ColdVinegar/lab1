import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static int plants_amount = 100;
    static int preys_amount = 200;
    static int predators_amount = 200;

    static int plants_health = 1;
    static int plants_limit = 10;
    static int preys_health = 15;
    static int preys_limit = 20;
    static int predators_health = 15;
    static int predators_limit = 20;

    static int experiment_avv_amount = 1;

    public static void main(String[] args) {
        Environment env = new Environment(50, 50);

        /* TESTING
        for (plants_health = 1; plants_health < 10; plants_health++){
            for (plants_limit = plants_health+1; plants_limit < 30; plants_limit++){
                for (preys_health = 10; preys_health < 100; preys_health++){
                    for (preys_limit = preys_health+1; preys_limit < 200; preys_limit++){
                        for (plants_amount = 10; plants_amount < 100; plants_amount+=5){
                            for (preys_amount = 10; preys_amount < 200; preys_amount+=5){
                                for (predators_amount = 10; predators_amount < 200; predators_amount+=5){
                                    int steps = experiment(env);
                                    System.out.printf("%d %d %d %d %d %d %d %d %d %d\n", steps, plants_health, plants_limit, preys_health,
                                    preys_limit, predators_health, predators_limit,
                                    plants_amount, preys_amount, predators_amount);

                                    try (PrintWriter out = new PrintWriter(new FileWriter("log2.txt", true))) {
                                        out.printf("%d %d %d %d %d %d %d %d%n",
                                                steps, plants_health, plants_limit,
                                                preys_health, preys_limit,
                                                predators_health, predators_limit,
                                                plants_amount, preys_amount, predators_amount);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }*/

        int steps = experiment(env);
    }


    static int experiment(Environment env){
        int summ = 0;
        for (int i = 0; i < experiment_avv_amount; i++){
            env.clear();
            spawn(agentType.PLANT, plants_amount, env);
            spawn(agentType.PREY, preys_amount, env);
            spawn(agentType.PREDATOR, predators_amount, env);
            int steps = 0;
            while (env.plants > 1 && env.preys > 1 && env.predators > 1){
                steps++;
                env.proceed();
                env.draw();
                System.out.printf("Step %d\n\n", steps);
                if (steps == 10_000) break;
            }
            summ += steps;
        }
        return summ/experiment_avv_amount;
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
                case PLANT -> agnt = new Plant(type, x, y, plants_health, plants_limit);
                case PREY -> agnt = new Prey(type, x, y, preys_health, preys_limit);
                case PREDATOR -> agnt = new Predator(type, x, y, predators_health, predators_limit);
            }
            env.add(agnt);
        }
    }
}
