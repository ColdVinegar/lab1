package com.example.ui_app;

import java.util.Arrays;

public class Environment {
    int height = 50, length = 50;
    Agent [][] world;
    int plants = 0, preys = 0, predators = 0;

    Environment(){
        this.world = new Agent[height][length];
    }
    Environment(int height, int length){
        this.height = height;
        this.length = length;
        this.world = new Agent[height][length];
    }

    void add(Agent agent){
        switch (agent.type){
            case PLANT -> this.plants++;
            case PREY -> this.preys++;
            case PREDATOR -> this.predators++;
        }
        world[agent.y][agent.x] = agent;
    }

    void remove(int x, int y){
        Agent agent = this.world[y][x];
        switch (agent.type){
            case PLANT -> this.plants--;
            case PREY -> this.preys--;
            case PREDATOR -> this.predators--;
        }
        world[agent.y][agent.x] = null;
    }

    void move(int[] from, int[] to){
        this.world[to[1]][to[0]] = this.world[from[1]][from[0]];
        this.remove(from[0], from[1]);
        switch (world[to[1]][to[0]].type){
            case PLANT -> this.plants++;
            case PREY -> this.preys++;
            case PREDATOR -> this.predators++;
        }
    }

    void clear(){
        for (Agent[] agents : this.world) Arrays.fill(agents, null);
        this.plants = 0;
        this.preys = 0;
        this.predators = 0;
    }

    void proceed(){
        for (Agent[] agents: world){
            for (Agent agent: agents){
                if (agent != null){
                    if (!agent.moved) agent.proceed(this);
                    else agent.moved = false;
                }
            }
        }
    }
}
