import java.util.Random;
import java.util.Scanner;

public class Main {
    static int plants_amount = 10;
    static int preys_amount = 10;
    static int predators_amount = 0;

    static int plants_health = 1;
    static int plants_limit = 10;
    static int agent_health = 15;
    static int agent_limit = 20;

    static int experiment_avv_amount = 100;

    public static void main(String[] args) {
        Environment env = new Environment(100, 100);

        for (plants_health = 1; plants_health < 10; plants_health++){
            for (plants_limit = plants_health+1; plants_limit < 30; plants_limit++){
                for (agent_health = 10; agent_health < 100; agent_health++){
                    for (agent_limit = agent_health+1; agent_limit < 200; agent_limit++){
                        for (plants_amount = 10; plants_amount < 100; plants_amount++){
                            for (preys_amount = 10; preys_amount < 200; preys_amount++){
                                for (predators_amount = 10; predators_amount < 200; predators_amount++){
                                    int steps = experiment(env);
                                    System.out.printf("%d %d %d %d %d %d %d %d\n", steps, plants_health, plants_limit, agent_health, agent_limit,
                                    plants_amount, preys_amount, predators_amount);
                                }
                            }
                        }
                    }
                }
            }
        }
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
                //env.draw();
                //System.out.printf("Step %d\n\n", steps);
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
                case PREY -> agnt = new Prey(type, x, y, agent_health, agent_limit);
                case PREDATOR -> agnt = new Predator(type, x, y, agent_health, agent_limit);
            }
            env.add(agnt);
        }
    }
}
