package UnoEngine.Strategies;

import UnoEngine.Enums.Action;
import UnoEngine.Enums.Penalty;
import UnoEngine.Strategies.ActionStrategies.ActionStrategy;
import UnoEngine.Strategies.PenaltyStrategies.PenaltyStrategy;

import java.util.HashMap;
import java.util.Map;

public class StrategyRegistry {
    Map<Penalty , PenaltyStrategy> penaltyStrategies;
    Map<Action , ActionStrategy> actionStrategies;

    public StrategyRegistry() {
        penaltyStrategies = new HashMap<>();
        actionStrategies =  new HashMap<>();
    }
    public void addPenaltyStrategy(Penalty penalty , PenaltyStrategy penaltyStrategy){
        penaltyStrategies.put(penalty, penaltyStrategy);
    }
    public void addActionStrategy(Action action , ActionStrategy actionStrategy){
        actionStrategies.put(action, actionStrategy);
    }

    public PenaltyStrategy getPenaltyStrategy(Penalty penalty){
        return penaltyStrategies.get(penalty);
    }
    public ActionStrategy getActionStrategy(Action action){
        return actionStrategies.get(action);
    }

}
