// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.actors;

import jowshell.items.OwData;
import jowshell.items.OwDevice;
import jowshell.items.OwDirectory;
import jowshell.items.OwItem;

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
