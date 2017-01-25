// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import jowshell.system.ICommandExecution;
import jowshell.logging.ILogger;

public class Discovery {
	private final ICommandExecution myExec;
	private final ILogger myLogger;

	private final Network myNetwork;

	public Discovery(String host, ICommandExecution cmdExecute, ILogger logger) {
		myExec = cmdExecute;
		myLogger = logger;
		myNetwork = new Network(host, myLogger);
	}

	public boolean discoverTree() {
		myNetwork.clear();

		myNetwork.discover(myExec);
		return myNetwork.hasDevices();
	}

	public Network getNetwork() {
		return myNetwork;
	}
}
