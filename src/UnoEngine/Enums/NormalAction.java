package UnoEngine.Enums;

import UnoEngine.GameVariations.Game;
import UnoEngine.Player;

public enum NormalAction implements Action {
    SKIP {
        @Override
        public void applyActionHelper(Game game , Player targetPlayer) {
            targetPlayer.setPenalty(StandardPenalty.SKIP);
        }
    },
    REVERSE {
        @Override
        public void applyActionHelper(Game game , Player targetPlayer) {
            if(game.getGameDirection() == GameDirection.CLOCKWISE)
                game.setGameDirection(GameDirection.COUNTER_CLOCKWISE);
            else
                game.setGameDirection(GameDirection.CLOCKWISE);

            System.out.println("[Action]    Game direction got reversed");
            if(game.getNoOfPlayers() == 2 ) SKIP.applyActionHelper(game,targetPlayer);
        }
    },
    DRAW_2 {
        @Override
        public void applyActionHelper(Game game , Player targetPlayer) {
            targetPlayer.setPenalty(StandardPenalty.DRAW_2);
        }
    };

    @Override
    public void applyAction(Game game , Player targetPlayer) {
        applyActionHelper(game,targetPlayer);
    }

    public abstract void applyActionHelper(Game game, Player targetPlayer);

}
