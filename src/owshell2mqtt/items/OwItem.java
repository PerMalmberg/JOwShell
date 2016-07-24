// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.items;

import owshell2mqtt.system.IExecute;
import owshell2mqtt.actors.IItemActor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class OwItem {
	private final Pattern myAddressWithFamilyPattern = Pattern.compile("^([a-zA-Z0-9]{2}\\.[a-zA-Z0-9]{12})$");
	protected final HashMap<String, OwItem> myChild = new HashMap<>();
	private final String myFullPath;

	public OwItem(String fullPath) {
		myFullPath = fullPath;
	}

	public String getFullPath() {
		return myFullPath;
	}

	public void clear() {
		myChild.clear();
	}

	public void add(OwItem item) {
		myChild.put(item.getName(), item);
	}

	public String getName() {
		Path p = Paths.get(myFullPath);
		return p.getFileName().toString();
	}

	public void discover(IExecute execute, String host) {
		myChild.values().forEach(item -> discover(execute, host));
	}

	protected boolean isPathToDevice(String fullPath) {
		Path p = Paths.get(fullPath);
		return matchesDeviceAddressWithFamily(p.getFileName().toString());
	}

	protected boolean matchesDeviceAddressWithFamily(String address) {
		address = removeLeading(address, '/');
		Matcher m = myAddressWithFamilyPattern.matcher(address);
		return m.matches();
	}

	protected String removeLeading(String s, char c) {
		if (s.startsWith(String.valueOf(c))) {
			s = s.substring(1, s.length());
		}
		return s;
	}

	public HashMap<String, OwItem> getChildren() {
		return myChild;
	}

	public int countDevices() {
		int i = 0;

		for (OwItem item : myChild.values()) {
			i += item.countDevices();
		}

		return i;
	}

	protected boolean traverseTreeWithActor(IItemActor actor) {
		return !actor.act(this) || traverseTree(actor);
	}

	protected boolean traverseTree(IItemActor actor) {
		OwItem[] children = myChild.values().toArray(new OwItem[myChild.values().size()]);

		boolean done = false;

		for( int i = 0; !done && i < children.length; ++i ) {
			done = children[i].traverseTreeWithActor(actor);
		}

		return done;
	}
}