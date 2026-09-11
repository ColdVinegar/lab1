public class Environment {
    private static final String TAB = " ".repeat(2);
    int height = 50, length = 50;
    Agent [][] world;

    Environment(){
        this.world = new Agent[height][length];
    }
    Environment(int height, int length){
        this.height = height;
        this.length = length;
        this.world = new Agent[height][length];
    }

    void add(Agent agent){
        world[agent.y][agent.x] = agent;
    }

    void remove(Agent agent){
        world[agent.y][agent.x] = null;
    }

    void draw(){
        for (Agent[] agents: this.world){
            StringBuilder line = new StringBuilder();
            for (Agent agent: agents){
                if (agent == null){
                    line.append("▪ ").append(TAB);
                    continue;
                }
                String sym = switch (agent.type){
                    case PLANT -> "🌱";
                    case PREY -> "🐨";
                    case PREDATOR -> "🐯";
                };
                line.append(sym).append(TAB);
            }
            System.out.println(line.toString().strip());
        }
    }
    void proceed(){
        for (Agent[] agents: world){
            for (Agent agent: agents){
                if (agent != null){
                    agent.proceed(this);
                }
            }
        }
    }
}
