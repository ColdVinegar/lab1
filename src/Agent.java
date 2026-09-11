public class Agent {
    agentType type;
    int health = 10;
    int x, y;

    Agent(agentType type, int x, int y){
        this.type = type;
        this.x = x;
        this.y = y;
    }
    Agent(agentType type, int x, int y, int health){
        this.type = type;
        this.x = x;
        this.y = y;
        this.health = health;
    }

    void proceed(Environment env){

    }
}

enum agentType{
    PLANT,
    PREY,
    PREDATOR
}