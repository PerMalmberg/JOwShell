// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import owshell2mqtt.items.OwData;
import owshell2mqtt.items.OwDevice;
import owshell2mqtt.system.ShellExecute;

import java.util.HashMap;

class JOwShell {
	public static void main(String[] args) {
		ShellExecute exec = new ShellExecute();
		Discovery discovery = new Discovery("localhost", exec);
		if (discovery.discoverTree()) {
			HashMap<String, OwDevice> device = discovery.getNetwork().getAllDevices();
			//device.values().forEach(JOwShell::printData);
			printData(device.get("26.24CAB5000000"));
			System.exit(0);
		} else {
			System.out.println("Discovery failed");
			System.exit(1);
		}

	}

	private static void printData(OwDevice d) {
		HashMap<String, OwData> data = d.getData();

		System.out.println( "====" + d.getName() + "====== ");
		ShellExecute exec = new ShellExecute();
		exec.setTimeout(2000); // Need extra time for temperature conversions.

		for (String name : data.keySet()) {
			OwData owData = data.get(name);
			String readData = owData.read(exec);
			System.out.println(owData.getFullPath() + ":" + readData );
		}

	}
}