// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import owshell2mqtt.items.OwDevice;
import owshell2mqtt.system.ShellExecute;

import java.util.HashMap;

class OWShell2MQTT {
	public static void main(String[] args) {
		ShellExecute exec = new ShellExecute();
		Discovery discovery = new Discovery("localhost", exec);
		if (discovery.discoverTree()) {
			HashMap<String, OwDevice> device = discovery.getNetwork().getAllDevices();
			device.values().forEach(item -> System.out.println(item.getName()));
			System.exit(0);
		} else {
			System.out.println("Discovery failed");
			System.exit(1);
		}

	}
}