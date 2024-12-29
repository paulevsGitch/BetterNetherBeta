package paulevs.bnb.command;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;

import java.util.Arrays;

public class RetroCommandWrapper implements Command {
	private final BNBCommand command;
	private final String name;
	
	public RetroCommandWrapper(String name, BNBCommand command) {
		this.command = command;
		this.name = name;
	}
	
	@Override
	public void command(SharedCommandSource commandSource, String[] parameters) {
		command.execute(commandSource.getPlayer(), Arrays.copyOfRange(parameters, 1, parameters.length));
	}
	
	@Override
	public String name() {
		return name;
	}
	
	@Override
	public void manual(SharedCommandSource commandSource) {}
}
