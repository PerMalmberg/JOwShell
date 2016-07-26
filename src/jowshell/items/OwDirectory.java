// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.items;

import jowshell.actors.IItemActor;
import jowshell.system.ICommandExecution;
import jowshell.system.IExecute;
import logging.ILogger;

public class OwDirectory extends OwItem {

	public OwDirectory(String fullPath, String host, ILogger logger) {
		super(fullPath, host, logger);
	}

	protected void createItemFormPath(String fullPath) {
		if (isPathToDevice(fullPath)) {
			add(new OwDevice(fullPath, myHost, myLogger));
		} else if (fullPath.endsWith("/")) {
			add(new OwDirectory(fullPath, myHost, myLogger));
		} else {
			add(new OwData(fullPath, myHost, myLogger));
		}
	}

	@Override
	public void discover(ICommandExecution cmdExec) {
		IExecute exec = cmdExec.getExec();
		if (exec.execute(1, cmdExec.getOwDir(), "--dir", "-s", myHost, getFullPath()) == 0) {
			exec.getOutput().forEach(this::createItemFormPath);
			myChild.values().forEach(item -> item.discover(cmdExec));
		}
	}

	@Override
	protected boolean traverseTreeWithActor(IItemActor actor) {
		return !actor.act(this) || traverseTree(actor);
	}
}
