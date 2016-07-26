// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.items;

import jowshell.actors.IItemActor;
import jowshell.items.DataTypes.DataType;
import jowshell.system.IExecute;
import logging.ILogger;

import java.util.ArrayList;
import java.util.List;

public class OwData extends OwItem {
	private StructureInfo structInfo = null;

	public OwData(String fullPath, String host, ILogger logger) {
		super(fullPath, host, logger);
	}

	@Override
	protected boolean traverseTreeWithActor(IItemActor actor) {
		return !actor.act(this) || traverseTree(actor);
	}

	/**
	 * Reads the data
	 *
	 * @param exec The executor
	 * @return The data as a string, or null on failure.
	 */
	public String read(IExecute exec) {
		String res = null;

		if (getDataDetails(exec)) {
			if (readData(exec, structInfo)) {
				List<String> readData = exec.getOutput();
				if (readData.size() > 0) {
					res = readData.get(0);
				} else {
					myLogger.debug("No data from device");
					res = null;
				}
			}
		}
		return res;
	}

	private boolean readData(IExecute exec, StructureInfo structInfo) {

		List<String> cmd = new ArrayList<>();
		cmd.add("owread");
		cmd.add("-s");
		cmd.add(myHost);
		if (structInfo.getType() == DataType.b) {
			cmd.add("--hex");
		}
		cmd.add(getFullPath());

		return exec.execute(1, cmd.toArray(new String[cmd.size()])) == 0;
	}

	private boolean getDataDetails(IExecute exec) {
		if (structInfo == null) {
			myLogger.debug("Reading structure info for " + getFullPath());
			// Read structure info for this data item
			if (exec.execute(1, "owread", "-s", myHost, "/structure/" + getFamilyFromLastDeviceInPath() + "/" + getFullPropertyName()) == 0) {
				String info = exec.getOutput().get(0);
				try {
					structInfo = new StructureInfo(info);
				} catch (IllegalArgumentException e) {
					myLogger.error(e);
				}
			}
		}

		return structInfo != null;
	}

	public String getFullPropertyName() {
		String[] part = getFullPath().split("/");

		StringBuilder sb = new StringBuilder();

		boolean found = false;

		for (int i = part.length - 1; !found && i >= 0; --i) {
			if (matchesDeviceAddressWithFamily(part[i])) {
				found = true;
			} else {
				if (sb.length() > 0) {
					sb.insert(0, "/");
					sb.insert(0, part[i]);
				} else {
					sb.append(part[i]);
				}
			}
		}

		return sb.toString();
	}

}
