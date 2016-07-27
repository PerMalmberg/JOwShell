// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.items;

import jowshell.actors.IItemActor;
import jowshell.items.DataTypes.DataType;
import jowshell.system.ICommandExecution;
import jowshell.system.IExecute;
import logging.ILogger;

import java.util.ArrayList;
import java.util.List;

public class OwData extends OwItem {
	private StructureInfo structInfo = null;
	private final OwDirectory myParent;

	public OwData(OwDirectory parent, String fullPath, String host, ILogger logger) {
		super(fullPath, host, logger);
		myParent = parent;
	}

	@Override
	protected boolean traverseTreeWithActor(IItemActor actor) {
		return !actor.act(this) || traverseTree(actor);
	}

	@Override
	public OwDevice getParentDevice() {
		return myParent.getParentDevice();
	}

	/**
	 * Reads the data
	 *
	 * @param exec The executor
	 * @return The data as a string, or null on failure.
	 */
	public String read(ICommandExecution exec) {
		String res = null;

		if (getDataDetails(exec)) {
			if (readData(exec, structInfo)) {
				List<String> readData = exec.getExec().getOutput();
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

	private boolean readData(ICommandExecution exec, StructureInfo structInfo) {
		boolean res = false;

		if (structInfo.isReadable()) {
			res = executeRead(exec, structInfo.getType() == DataType.b);
		} else {
			myLogger.error(getFullPath() + " is not readable.");
		}

		return res;
	}

	public boolean write(ICommandExecution cmdExec, String value) {
		boolean res = false;
		if (getDataDetails(cmdExec)) {
			if (structInfo.isWriteable()) {
				IExecute exec = cmdExec.getExec();
				if (exec.execute(1, cmdExec.getOwWrite(), getFullPath(), value) == 0) {
					res = true;
				}
				else {
					myLogger.error("Failed to write to " + getFullPath());
				}
			} else {
				myLogger.error(getFullPath() + " it is not writable");
			}
		}

		return res;
	}

	private boolean getDataDetails(ICommandExecution cmdExec) {
		if (structInfo == null) {
			myLogger.debug("Reading structure info for " + getFullPath());
			// Read structure info for this data item
			IExecute exec = cmdExec.getExec();
			String path = "/structure/" + myParent.getFamily() + "/" + getFullPropertyName();

			if (exec.execute(1, cmdExec.getOwRead(), "-s", myHost, path) == 0) {
				String info = exec.getOutput().get(0);
				try {
					structInfo = new StructureInfo(info);
				} catch (IllegalArgumentException e) {
					myLogger.error(e);
				}
			}
			else {
				myLogger.error("Could not read data details for " + getFullPath());
			}
		}

		return structInfo != null;
	}

	public String getFullPropertyName() {
		String parentPath = getParentDevice().getFullPath();
		String propertyPath = getFullPath().replace(parentPath, "");
		return propertyPath;
	}

	public boolean isReadable(ICommandExecution cmdExec) {
		boolean res = false;
		if (getDataDetails(cmdExec)) {
			res = structInfo.isReadable();
		}
		return res;
	}

	public boolean isWritable(ICommandExecution cmdExec) {
		boolean res = false;
		if (getDataDetails(cmdExec)) {
			res = structInfo.isWriteable();
		}
		return res;
	}

}
