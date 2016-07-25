// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import owshell2mqtt.system.IExecute;

public class Discovery {
	private final IExecute myExec;

	private final Network myNetwork;

	public Discovery(String host, IExecute execute) {
		myExec = execute;
		myNetwork = new Network(host);
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
