// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShellExecute {
	private List<String> myCommand;
	private Exception myLastException = null;
	private final List<String> myOutput = new ArrayList<>();

	public ShellExecute(String command, String... args) {
		myCommand = new ArrayList<>();
		myCommand.add(command);
		List<String> arg = new ArrayList<>();
		for (String s : args) {
			if (s != null) {
				arg.add(s);
			}
		}
		myCommand.addAll(arg);
	}

	/**
	 * Executes the command
	 *
	 * @param timeoutMilliSecs   Timeout, in milliseconds
	 * @param defaultReturnValue Return value, if operation fails.
	 * @return The return value from the command, or the default return value on failure.
	 */
	public int execute(int timeoutMilliSecs, int defaultReturnValue) {
		myLastException = null;
		myOutput.clear();

		int res = defaultReturnValue;

		StreamReader std = null;

		try {
			ProcessBuilder pb = new ProcessBuilder();
			pb.command(myCommand);
			pb.redirectErrorStream(true);
			Process p = pb.start();

			std = new StreamReader(p.getInputStream());

			std.start();

			if (p.waitFor(timeoutMilliSecs, TimeUnit.MILLISECONDS)) {
				res = p.exitValue();
			}
		} catch (Exception ex) {
			myLastException = ex;
		} finally {
			if (std != null) {
				try {
					std.join();
					myOutput.addAll(std.getOutput());

				} catch (InterruptedException e) {
					// Do nothing
				}
			}
		}

		return res;
	}

	/**
	 * Executes the command with the new arguments
	 *
	 * @param timeoutMilliSecs   Timeout, in milliseconds
	 * @param defaultReturnValue Return value, if operation fails.
	 * @return The return value from the command, or the default return value on failure.
	 */
	public int execute(int timeoutMilliSecs, int defaultReturnValue, String... newArguments) {
		List<String> newCommand = new ArrayList<>();
		newCommand.add(myCommand.get(0));
		for (String s : newArguments) {
			if (s != null) {
				newCommand.add(s);
			}
		}

		myCommand = newCommand;
		return execute(timeoutMilliSecs, defaultReturnValue);
	}

	public Exception getLastException() {
		return myLastException;
	}

	public List<String> getOutput() {
		return myOutput;
	}
}
