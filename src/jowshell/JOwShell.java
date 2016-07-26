// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import jowshell.items.OwData;
import jowshell.items.OwDevice;
import jowshell.system.ShellExecute;
import logging.ILogger;

import java.util.Arrays;
import java.util.HashMap;

class JOwShell  implements ILogger {
	private ShellExecute exec;

	public static void main(String[] args) {
		JOwShell jow = new JOwShell();
		jow.exec(args);
	}

	public void exec(String[] args)
	{
		exec = new ShellExecute(this);
		System.out.println("JOwShell: Java frontend for ow-shell (OWFS shell commands)");

		if( args.length == 1) {
			Discovery discovery = new Discovery(args[0], exec, this);
			if (discovery.discoverTree()) {
				HashMap<String, OwDevice> device = discovery.getNetwork().getAllDevices();
				device.values().forEach(this::printData);
				System.exit(0);
			} else {
				error("Discovery failed");
				System.exit(1);
			}
		}
		else {
			System.out.println("Usage: JOwShell <host>");
		}
	}

	private void printData(OwDevice d) {
		HashMap<String, OwData> data = d.getData();

		System.out.println( "====" + d.getName() + "====== ");
		exec.setTimeout(2000); // Some read operations takes longer, such as temperature conversions.

		for (String name : data.keySet()) {
			OwData owData = data.get(name);
			String readData = owData.read(exec);
			System.out.println(owData.getFullPath() + ":" + readData );
		}
	}

	@Override
	public void debug(String msg) {
		System.out.println( "[DEBUG]" + msg);
	}

	@Override
	public void error(String msg) {
		System.out.println( "[ERROR] " + msg);
	}

	@Override
	public void error(Exception ex) {
		error(ex.getMessage());
		error(Arrays.toString(ex.getStackTrace()));
	}
}