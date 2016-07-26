// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import jowshell.system.IExecute;
import logging.ILogger;

public class Discovery {
	private final IExecute myExec;
	private final ILogger myLogger;

	private final Network myNetwork;

	public Discovery(String host, IExecute execute, ILogger logger) {
		myExec = execute;
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
