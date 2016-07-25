// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.items;

import owshell2mqtt.OwRead;
import owshell2mqtt.actors.IItemActor;
import owshell2mqtt.system.IExecute;

import java.util.List;

public class OwData extends OwItem {
	private StructureInfo structInfo = null;

	public OwData(String fullPath, String host) {
		super(fullPath, host);
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
			OwRead read = new OwRead(exec, myHost);
			if (read.read(getFullPath(), structInfo)) {
				List<String> readData = read.getData();
				if (readData.size() > 0) {
					res = readData.get(0);
				} else {
					// Empty data
					res = null;
				}
			}
		}
		return res;
	}

	private boolean getDataDetails(IExecute exec) {
		boolean res = false;

		if (structInfo == null) {
			// Read structure info for this data item
			if (exec.execute(1, "owread", "-s", myHost, "/structure/" + getFamilyFromLastDeviceInPath() + "/" + getFullPropertyName()) == 0) {
				String info = exec.getOutput().get(0);
				try {
					structInfo = new StructureInfo(info);
					res = true;
				} catch (IllegalArgumentException e) {
					// Nada
				}
			}
		}

		return res;
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
