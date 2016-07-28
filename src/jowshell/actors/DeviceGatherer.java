// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.actors;

import jowshell.items.OwData;
import jowshell.items.OwDevice;
import jowshell.items.OwDirectory;
import jowshell.items.OwItem;

import java.util.HashMap;

public class DeviceGatherer implements IItemAcceptor {
	private final HashMap<String, OwDevice> myDevices = new HashMap<>();

	@Override
	public boolean accept(OwItem item) {
		return true;
	}

	@Override
	public boolean accept(OwDirectory dir) {
		return true;
	}

	@Override
	public boolean accept(OwDevice device) {
		myDevices.put(device.getName(), device);
		return true;
	}

	@Override
	public boolean accept(OwData data) {
		return true;
	}

	public HashMap<String, OwDevice> getDevices() {
		return myDevices;
	}
}
