package paulevs.bnb.command;

@FunctionalInterface
public interface BNBCommand {
	void execute(Object commandSource, String[] args);
}
