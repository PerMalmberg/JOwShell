// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import jowshell.items.OwData;
import jowshell.items.OwDevice;
import jowshell.system.ICommandExecution;
import jowshell.system.IExecute;
import jowshell.system.ShellExecute;
import logging.ILogger;

import java.util.Arrays;
import java.util.HashMap;

class JOwShell implements ILogger, ICommandExecution {
	private ShellExecute exec;

	public static void main(String[] args) {
		JOwShell jow = new JOwShell();
		jow.exec(args);
	}

	public void exec(String[] args) {
		exec = new ShellExecute(this);
		System.out.println("JOwShell: Java wrapper for ow-shell (OWFS shell commands)");

		if (args.length == 2) {
			Discovery discovery = new Discovery(args[0], this, this);
			if (discovery.discoverTree()) {
				HashMap<String, OwDevice> device = discovery.getNetwork().getAllDevices();
				if (args[1].equals("-")) {
					device.values().forEach(this::printData);
				} else {
					OwDevice dev = discovery.getNetwork().getAllDevices().get(args[1]);
					if (dev != null) {
						printData(dev);
					} else {
						System.out.println("The specified device does not exist (check casing)");
					}
				}
				System.exit(0);
			} else {
				error("Discovery failed");
				System.exit(1);
			}
		} else {
			System.out.println("Usage: JOwShell <host> <pathToDevice | ->");
			System.out.println("where - means all devices");
		}
	}

	private void printData(OwDevice d) {
		HashMap<String, OwData> data = d.getData();

		System.out.println("==== Printing readable attributes for device "+ d.getName() + "====== ");
		exec.setTimeout(2000); // Some read operations takes longer, such as temperature conversions.

		for (String name : data.keySet()) {
			OwData owData = data.get(name);
			if (owData.isReadable(this)) {
				String readData = owData.read(this);
				System.out.println(owData.getFullPath() + ":" + readData);
			}
		}
	}

	@Override
	public void debug(String msg) {
		System.out.println("[DEBUG]" + msg);
	}

	@Override
	public void error(String msg) {
		System.out.println("[ERROR] " + msg);
	}

	@Override
	public void error(Exception ex) {
		error(ex.getMessage());
		error(Arrays.toString(ex.getStackTrace()));
	}

	@Override
	public String getOwRead() {
		// Assume command is on the path
		return "owread";
	}

	@Override
	public String getOwDir() {
		// Assume command is on the path
		return "owdir";
	}

	@Override
	public String getOwWrite() {
		// Assume command is on the path
		return "owwrite";
	}

	@Override
	public IExecute getExec() {
		return exec;
	}
}