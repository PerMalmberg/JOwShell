// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import owshell2mqtt.actors.DeviceGatherer;
import owshell2mqtt.items.OwDevice;
import owshell2mqtt.items.OwDirectory;
import owshell2mqtt.system.IExecute;

import java.util.HashMap;
import java.util.List;

public class Network extends OwDirectory {
	private HashMap<String, OwDevice> myAllDevices = new HashMap<>();

	public Network() {
		super("/");
	}

	public boolean hasDevices() {
		return countDevices() > 0;
	}

	public void clear() {
		myAllDevices.clear();
		super.clear();
	}

	public void discover(IExecute exec, String host) {
		// Read the root of the network
		if (exec.execute(1, "owdir", "-s", host, "/") == 0) {
			createItems(exec.getOutput());
			// Start recursive discovery of devices
			myChild.values().forEach(item -> item.discover(exec, host));

			// Make a one-time traversal to get all the found devices.
			DeviceGatherer gatherer = new DeviceGatherer();
			traverseTree(gatherer);
			myAllDevices = gatherer.getDevices();
		}
	}

	public HashMap<String, OwDevice> getAllDevices() {
		return myAllDevices;
	}

	private void createItems(List<String> data) {
		for (String s : data) {
			// We only want to read the parts of the network that exists
			// directly under the root or under a 1-Wire switch.
			if (isPathToDevice(s)) {
				createItemFormPath(s);
			}
		}
	}
}
