package commands;

public class QuitCommand implements Command {

    @Override
    public void execute() {
        System.out.println("\n\nGame ended by player");
        System.exit(0);
    }

    @Override
    public void undo() {
        // Cannot undo quit
    }

    @Override
    public String getName() {
        return "Quit";
    }
}
