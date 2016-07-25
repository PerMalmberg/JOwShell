// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.actors;

import owshell2mqtt.items.OwData;
import owshell2mqtt.items.OwDevice;
import owshell2mqtt.items.OwDirectory;
import owshell2mqtt.items.OwItem;

public class SingleDeviceFinder implements IItemActor {

	private final String myFamilyDotAddress;
	private OwDevice myDev = null;

	public SingleDeviceFinder(String familyDotAddress) {
		myFamilyDotAddress = familyDotAddress;
	}

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
		if( device.getName().equals(myFamilyDotAddress) ) {
			myDev = device;
		}

		// If found, return false to stop processing
		return myDev == null;
	}

	@Override
	public boolean act(OwData data) {
		return true;
	}

	public OwDevice getDevice() {
		return myDev;
	}
}
