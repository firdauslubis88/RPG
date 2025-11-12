package commands;

import entities.Player;

public class MoveUpCommand implements Command {
    private Player player;
    private int oldX, oldY;

    public MoveUpCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        oldX = player.getX();
        oldY = player.getY();
        player.moveUp();
    }

    @Override
    public void undo() {
        player.setPosition(oldX, oldY);
    }

    @Override
    public String getName() {
        return "MoveUp";
    }
}
