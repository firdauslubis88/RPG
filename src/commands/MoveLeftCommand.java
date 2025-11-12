package commands;

import entities.Player;

public class MoveLeftCommand implements Command {
    private Player player;
    private int oldX, oldY;

    public MoveLeftCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        oldX = player.getX();
        oldY = player.getY();
        player.moveLeft();
    }

    @Override
    public void undo() {
        player.setPosition(oldX, oldY);
    }

    @Override
    public String getName() {
        return "MoveLeft";
    }
}
