import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plant extends Agent{



    Plant(agentType type, int x, int y) {
        super(type, x, y, 1, 10);
    }
    Plant(agentType type, int x, int y, int health, int lim){
        super(type, x, y, health, lim);
    }
}
