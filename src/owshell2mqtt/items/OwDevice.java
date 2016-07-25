// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.items;

import owshell2mqtt.actors.IItemActor;

public class OwDevice extends OwDirectory {

	public OwDevice(String fullPath) {
		super(fullPath);
	}

	@Override
	protected boolean traverseTreeWithActor(IItemActor actor)
	{
		return !actor.act(this) || traverseTree(actor);
	}
}
