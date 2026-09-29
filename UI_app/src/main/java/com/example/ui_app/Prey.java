package com.example.ui_app;

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
        move(env, lookAround(env));
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
                        double newdist = Math.sqrt(Math.pow((this.y-Y), 2)+Math.pow((this.x-X), 2));
                        if (newdist < dist){
                            nearestPlant[0] = X;
                            nearestPlant[1] = Y;
                            dist = newdist;
                        }
                        if (newdist == 1.0) available.add(new int[]{X, Y});
                    } else if (env.world[Y][X].type == agentType.PREDATOR) {
                        predators.add(new int[]{X, Y});
                    }
                }
            }
        }

        int[] target = {0, 0};

        if (!available.isEmpty()){
            if (!predators.isEmpty()){
                int[] v_res = {0, 0};  //ЗДЕСЬ НАЧИНАЕТСЯ ПОЛНАЯ ХРЕНЬ С ВЕКТОРАМИ, НО ОНА ОЧЕНЬ ИНТЕРЕСНАЯ, МНЕ НРАВИЦА

                for (int[] p: predators){
                    v_res[0] += this.x-p[0];
                    v_res[1] += this.y-p[1];
                }

                v_res[0] += this.x;
                v_res[1] += this.y;

                if (predators.size() == 2 && predators.getFirst()[0]*v_res[1] == predators.getFirst()[1]*v_res[0]){
                    int[] v_90 = {-1*v_res[1], v_res[0]};
                    int[] v_270 = {v_res[1], -1*v_res[0]};

                    int[] v_res_90 = v_90;
                    int[] v_res_270 = v_270;

                    for (int[] avail: available){
                        v_res_90[0] += avail[0];
                        v_res_270[0] += avail[0];
                        v_res_90[1] += avail[1];
                        v_res_270[1] += avail[1];
                    }

                    double dist90 = Math.sqrt(Math.pow((this.y-v_res_90[1]), 2)+Math.pow((this.x-v_res_90[0]), 2));
                    double dist270 = Math.sqrt(Math.pow((this.y-v_res_270[1]), 2)+Math.pow((this.x-v_res_270[0]), 2));

                    if (dist90 > dist270) target = v_90;
                    else target = v_270;
                }
                else target = v_res;
            }
            else {
                if (dist < 5) {
                    target = nearestPlant;
                } else {
                    Random random = new Random();
                    int rnd = random.nextInt(available.size());
                    target = available.get(rnd);
                }
            }
        }

        if ((target[0] == 0 && target[1] == 0) || (target[0] == this.x && target[1] == this.y)) return target = new int[]{this.x, this.y};
        else return decide(target, available);
    }
}
