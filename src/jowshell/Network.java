// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import jowshell.actors.DeviceGatherer;
import jowshell.items.OwDevice;
import jowshell.items.OwDirectory;
import jowshell.system.ICommandExecution;
import jowshell.system.IExecute;
import logging.ILogger;

import java.util.HashMap;
import java.util.List;

public class Network extends OwDirectory {
	private HashMap<String, OwDevice> myAllDevices = new HashMap<>();

	public Network(String host, ILogger logger) {
		super(null, "/", "NoFamily", host, logger);
	}

	public boolean hasDevices() {
		return myAllDevices.size() > 0;
	}

	public void clear() {
		myAllDevices.clear();
		super.clear();
	}

	public void discover(ICommandExecution cmdExec) {
		// Read the root of the network
		if (executeDir(cmdExec, "/")) {
			IExecute exec = cmdExec.getExec();
			createItems(exec.getOutput(), cmdExec);
			// Start recursive discovery of devices
			myChild.values().forEach(item -> item.discover(cmdExec));

			// Make a one-time traversal to get all the found devices.
			DeviceGatherer gatherer = new DeviceGatherer();
			traverseTree(gatherer);
			myAllDevices = gatherer.getDevices();
		}
	}

	public HashMap<String, OwDevice> getAllDevices() {
		return myAllDevices;
	}

	private void createItems(List<String> data, ICommandExecution cmdExec) {
		for (String s : data) {
			// We only want to read the parts of the network that exists
			// directly under the root or under a 1-Wire switch.
			if (isPathToDevice(s, cmdExec)) {
				createItemFromPath(s, cmdExec);
			}
		}
	}
}
