// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.actors;

import owshell2mqtt.items.OwDevice;
import owshell2mqtt.items.OwDirectory;
import owshell2mqtt.items.OwItem;

import java.util.HashMap;

public class DeviceGatherer implements IItemActor {
	private final HashMap<String, OwDevice> myDevices = new HashMap<>();

	@Override
	public boolean act(OwItem item) {
		return true;
	}

	@Override
	public boolean act(OwDirectory dir) {
		return true;
	}

	@Override
	public boolean act(OwDevice device) {
		myDevices.put(device.getName(), device);
		return true;
	}

	public HashMap<String, OwDevice> getDevices() {
		return myDevices;
	}
}
