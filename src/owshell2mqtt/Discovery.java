// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import owshell2mqtt.system.IExecute;

public class Discovery {
	private final IExecute myExec;

	private final Network myNetwork = new Network();
	private final String myHost;


	public Discovery(String host, IExecute execute) {
		myHost = host;
		myExec = execute;
	}

	public boolean discoverTree() {
		myNetwork.clear();

		myNetwork.discover(myExec, myHost);
		return myNetwork.hasDevices();
	}

	public Network getNetwork() {
		return myNetwork;
	}
}
