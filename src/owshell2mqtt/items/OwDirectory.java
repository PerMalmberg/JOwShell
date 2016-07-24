// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.items;

import owshell2mqtt.system.IExecute;
import owshell2mqtt.actors.IItemActor;
import owshell2mqtt.actors.SingleDeviceFinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OwDirectory extends OwItem {
	private final Pattern myAddressPattern = Pattern.compile("^([a-zA-Z0-9]{14})$");

	public OwDirectory(String fullPath) {
		super(fullPath);
	}

	protected void createItemFormPath(String fullPath) {
		if (isPathToDevice(fullPath)) {
			add(new OwDevice(fullPath));
		} else if (fullPath.endsWith("/")) {
			add(new OwDirectory(fullPath));
		} else {
			add(new OwData(fullPath));
		}
	}

	@Override
	public void discover(IExecute execute, String host) {
		if (execute.execute(1, "owdir", "--dir", "-s", host, getFullPath()) == 0) {
			execute.getOutput().forEach(this::createItemFormPath);
			myChild.values().forEach(item -> item.discover(execute, host));
		}
	}

	public OwDevice findDevice(String address) {
		String toFind = null;
		if (isDeviceAddress(address)) {
			toFind = address.substring(0, 2) + "." + address.substring(2, address.length());
		} else if (matchesDeviceAddressWithFamily(address)) {
			toFind = address;
		}

		OwDevice dev = null;

		if( toFind != null) {
			SingleDeviceFinder finder = new SingleDeviceFinder(toFind);
			traverseTree( finder );
			dev = finder.getDevice();
		}

		return dev;
	}

	@Override
	protected boolean traverseTreeWithActor(IItemActor actor)
	{
		return !actor.act(this) || traverseTree(actor);
	}

	private boolean isDeviceAddress(String address) {
		Matcher m = myAddressPattern.matcher(address);
		return m.matches();
	}
}
