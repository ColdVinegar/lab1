package com.example.ui_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Agent {
    agentType type;
    int health = 15;
    int splitLimit = 20;
    int x, y;
    boolean moved = false;

    Agent(agentType type, int x, int y){
        this.type = type;
        this.x = x;
        this.y = y;
    }
    Agent(agentType type, int x, int y, int health, int lim){
        this.type = type;
        this.x = x;
        this.y = y;
        this.health = health;
        this.splitLimit = lim;
    }

    List<int[]> lookNear(Environment env){
        List<int[]> res = new ArrayList<>();
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                if (i == 0 || j == 0) {
                    int X = this.x + i;
                    int Y = this.y + j;
                    if (X >= 0 && Y >= 0 && X < env.length && Y < env.height) {
                        if (env.world[Y][X] == null) {
                            res.add(new int[]{X, Y});
                        }
                    }
                }
            }
        }
        return res;
    }

    void proceed(Environment env){
        switch (this.type){
            case PLANT -> this.health++;
            default -> this.health--;
        }
        if (this.health >= this.splitLimit){
            List<int[]> available = lookNear(env);
            if (!available.isEmpty()){
                Random random = new Random();
                int rnd = random.nextInt(available.size());
                switch (this.type){
                    case PLANT -> env.add(new Plant(this.type, available.get(rnd)[0], available.get(rnd)[1], (int) this.health/2, this.splitLimit));
                    case PREY -> env.add(new Prey(this.type, available.get(rnd)[0], available.get(rnd)[1], (int) this.health/2, this.splitLimit));
                    case PREDATOR -> env.add(new Predator(this.type, available.get(rnd)[0], available.get(rnd)[1], (int) this.health/2, this.splitLimit));
                }
                this.health /= 2;
            }
        }
    }

    int[] decide(int[] target, List<int[]> available){
        int deltaX = this.x-target[0], deltaY = this.y-target[1];

        int[] newpoint = {this.x, this.y};
        List<int[]> X_avail = new ArrayList<>(List.of()), Y_avail = new ArrayList<>(List.of());

        for (int[] avail: available){
            if (avail[0] != this.x) X_avail.add(avail);
            if (avail[1] != this.y) Y_avail.add(avail);
        }

        if ((Math.abs(deltaX) > Math.abs(deltaY) && !X_avail.isEmpty()) || (Math.abs(deltaX) < Math.abs(deltaY) && Y_avail.isEmpty())
                || ((Math.abs(deltaX) == Math.abs(deltaY) && !Y_avail.isEmpty()
                && (Math.abs(deltaY)/deltaY) != (Math.abs((this.y-Y_avail.getFirst()[1]))/(this.y-Y_avail.getFirst()[1]))
                && (Math.abs(deltaY)/deltaY) != (Math.abs((this.y-Y_avail.getLast()[1]))/(this.y-Y_avail.getLast()[1]))))){
            int newX = this.x;
            if (deltaX == 0){
                Random random = new Random();
                int rnd = random.nextInt(X_avail.size());
                newX = X_avail.get(rnd)[0];
            }
            else if (X_avail.size() == 2 || (!X_avail.isEmpty() && (Math.abs(deltaX)/deltaX) == (Math.abs((this.x-X_avail.getFirst()[0]))/(this.x-X_avail.getFirst()[0])))) newX -= (Math.abs(deltaX)/deltaX);
            newpoint[0] = newX;
        }
        else{
            int newY = this.y;
            if (deltaY == 0){
                Random random = new Random();
                int rnd = random.nextInt(Y_avail.size());
                newY = Y_avail.get(rnd)[1];
            }
            else if (Y_avail.size() == 2 || (!Y_avail.isEmpty() && (Math.abs(deltaY)/deltaY) == (Math.abs((this.y-Y_avail.getFirst()[1]))/(this.y-Y_avail.getFirst()[1])))) newY -= (Math.abs(deltaY)/deltaY);
            newpoint[1] = newY;
        }
        return newpoint;
    }

    void move(Environment env, int[] target){
        if (this.x == target[0] && this.y == target[1]) return;

        if (env.world[target[1]][target[0]] != null) {
            this.health += env.world[target[1]][target[0]].health;
            env.remove(target[0], target[1]);
        }
        if (this.x-target[0] < 0 || this.y-target[1] < 0) this.moved = true;
        env.move(new int[]{this.x, this.y}, target);

        this.x = target[0];
        this.y = target[1];
    }

    boolean isAlive(Environment env){
        if (this.health == 0){
            env.remove(this.x, this.y);
            return false;
        }
        return true;
    }
}

enum agentType{
    PLANT,
    PREY,
    PREDATOR
}