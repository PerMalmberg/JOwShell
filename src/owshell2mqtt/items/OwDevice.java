// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.items;

import owshell2mqtt.actors.IItemActor;

public class OwDevice extends OwDirectory {

	public OwDevice(String fullPath) {
		super(fullPath);
	}

	@Override
	public int countDevices()
	{
		// Count ourselves.
		return 1;
	}

	@Override
	protected boolean traverseTreeWithActor(IItemActor actor)
	{
		return !actor.act(this) || traverseTree(actor);
	}
}
