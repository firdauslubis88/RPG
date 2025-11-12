package commands;

import entities.Player;

public class MoveRightCommand implements Command {
    private Player player;
    private int oldX, oldY;

    public MoveRightCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        oldX = player.getX();
        oldY = player.getY();
        player.moveRight();
    }

    @Override
    public void undo() {
        player.setPosition(oldX, oldY);
    }

    @Override
    public String getName() {
        return "MoveRight";
    }
}
