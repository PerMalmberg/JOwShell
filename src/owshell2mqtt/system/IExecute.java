// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.system;

import java.util.List;

public interface IExecute {
	/**
	 * Executes the command with the new arguments
	 *
	 * @param defaultReturnValue Return value, if operation fails.
	 * @param commandArray       The command as an array of strings.
	 * @return The return value from the command, or the default return value on failure.
	 */
	int execute(int defaultReturnValue, String... commandArray);

	/**
	 * Sets the timeout when executing the command
	 *
	 * @param timeoutInMilliSeconds The timeout value, in milliseconds.
	 */
	void setTimeout(int timeoutInMilliSeconds);

	Exception getLastException();

	List<String> getOutput();
}
