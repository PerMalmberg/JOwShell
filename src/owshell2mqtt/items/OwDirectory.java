// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.items;

import owshell2mqtt.actors.IItemActor;
import owshell2mqtt.system.IExecute;

public class OwDirectory extends OwItem {

	public OwDirectory(String fullPath, String host) {
		super(fullPath, host);
	}

	protected void createItemFormPath(String fullPath) {
		if (isPathToDevice(fullPath)) {
			add(new OwDevice(fullPath, myHost));
		} else if (fullPath.endsWith("/")) {
			add(new OwDirectory(fullPath, myHost));
		} else {
			add(new OwData(fullPath, myHost));
		}
	}

	@Override
	public void discover(IExecute execute) {
		if (execute.execute(1, "owdir", "--dir", "-s", myHost, getFullPath()) == 0) {
			execute.getOutput().forEach(this::createItemFormPath);
			myChild.values().forEach(item -> item.discover(execute));
		}
	}

	@Override
	protected boolean traverseTreeWithActor(IItemActor actor) {
		return !actor.act(this) || traverseTree(actor);
	}
}
