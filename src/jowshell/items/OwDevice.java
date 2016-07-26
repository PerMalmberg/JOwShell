// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.items;

import jowshell.actors.IItemActor;
import jowshell.actors.PropertyGatherer;
import logging.ILogger;

import java.util.HashMap;

public class OwDevice extends OwDirectory {

	public OwDevice(String fullPath, String host, ILogger logger) {
		super(fullPath, host, logger);
	}

	@Override
	protected boolean traverseTreeWithActor(IItemActor actor) {
		return !actor.act(this) || traverseTree(actor);
	}

	public HashMap<String, OwData> getData() {
		PropertyGatherer props = new PropertyGatherer();

		for (OwItem child : myChild.values()) {
			child.traverseTreeWithActor(props);
		}

		return props.getData();
	}
}
