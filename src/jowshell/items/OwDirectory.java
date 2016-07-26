// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.items;

import jowshell.actors.IItemActor;
import jowshell.system.ICommandExecution;
import jowshell.system.IExecute;
import logging.ILogger;

import java.nio.file.Paths;

public class OwDirectory extends OwItem {
	protected String myCurrentParentFamily;
	protected OwDirectory myParent;

	public OwDirectory(OwDirectory parent, String fullPath, String currentParentFamily, String host, ILogger logger) {
		super(fullPath, host, logger);
		myParent = parent;
		myCurrentParentFamily = currentParentFamily;
	}

	protected void createItemFromPath(String fullPath, ICommandExecution cmdExec) {
		if (isPathToDevice(fullPath, cmdExec)) {
			String family = readFamily(fullPath, cmdExec);
			add(new OwDevice(this, fullPath, family, myHost, myLogger));
		} else if (fullPath.endsWith("/")) {
			add(new OwDirectory(this, fullPath, myCurrentParentFamily, myHost, myLogger));
		} else {
			add(new OwData(this, fullPath, myHost, myLogger));
		}
	}

	protected String readFamily(String fullPath, ICommandExecution cmdExec) {
		IExecute exec = cmdExec.getExec();
		String family = null;

		String familyPath = Paths.get(fullPath, "family").toString().replace("\\", "/");

		if (exec.execute(1, cmdExec.getOwRead(), "-s", myHost, familyPath) == 0) {
			family = exec.getOutput().get(0);
		}

		return family;
	}

	@Override
	public void discover(ICommandExecution cmdExec) {
		IExecute exec = cmdExec.getExec();
		// We'll be executing owdir on non-directories, but that is quicker than first reading the
		// StructureInfo to determine if it is actually a directory.
		if (executeDir(cmdExec, getFullPath())) {
			exec.getOutput().forEach(item -> createItemFromPath(item, cmdExec));
			myChild.values().forEach(item -> item.discover(cmdExec));
		}
	}

	@Override
	protected boolean traverseTreeWithActor(IItemActor actor) {
		return !actor.act(this) || traverseTree(actor);
	}

	@Override
	public OwDevice getParentDevice() {
		return myParent.getParentDevice();
	}

	public String getFamily() {
		return myCurrentParentFamily;
	}
}
