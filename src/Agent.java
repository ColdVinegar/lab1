public class Agent {
    agentType type;
    int health = 10;
    int x, y;

    Agent(agentType type, int x, int y){
        this.type = type;
        this.x = x;
        this.y = y;
    }

}

enum agentType{
    PLANT,
    PREY,
    PREDATOR
}