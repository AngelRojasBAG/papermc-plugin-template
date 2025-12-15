//ruta del archivo: app/src/main/java/cl/angel/ge/wizard/WizardService.java
package cl.angel.ge.wizard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import java.util.List;

public final class WizardService {

    public enum Step {
        ITEM, QUANTITY, PRICE
    }

    public enum Mode {
        BUY, SELL
    }

    public static final class State {
        public Mode mode;
        public Step step = Step.ITEM;

        public String itemKey; // MarketItemKey serializado (por ahora string)
        public int quantity;
        public double unitPrice;
        public String searchQuery;
        public int page = 0;
        public List<Material> searchResults;

    }

    private final Map<UUID, State> states = new HashMap<>();

    public State start(UUID playerId, Mode mode) {
        State s = new State();
        s.mode = mode;
        states.put(playerId, s);
        return s;
    }

    public State get(UUID playerId) {
        return states.get(playerId);
    }

    public void clear(UUID playerId) {
        states.remove(playerId);
    }
}
