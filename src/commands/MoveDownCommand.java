package commands;

import entities.Player;

public class MoveDownCommand implements Command {
    private Player player;
    private int oldX, oldY;

    public MoveDownCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        oldX = player.getX();
        oldY = player.getY();
        player.moveDown();
    }

    @Override
    public void undo() {
        player.setPosition(oldX, oldY);
    }

    @Override
    public String getName() {
        return "MoveDown";
    }
}
