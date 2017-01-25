// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.items;

import jowshell.actors.IItemAcceptor;
import jowshell.actors.PropertyGatherer;
import jowshell.logging.ILogger;

import java.util.HashMap;

public class OwDevice extends OwDirectory {

	public OwDevice( OwDirectory parent, String fullPath, String family, String host, ILogger logger) {
		super( parent, fullPath, family, host, logger);
	}

	@Override
	protected boolean traverseTreeWithActor(IItemAcceptor actor) {
		return !actor.accept(this) || traverseTree(actor);
	}

	public HashMap<String, OwData> getData() {
		PropertyGatherer props = new PropertyGatherer();

		for (OwItem child : myChild.values()) {
			child.visit( props );
		}

		return props.getData();
	}

	@Override
	public OwDevice getParentDevice() {
		// Stop traversal when we reach a device.
		return this;
	}

	@Override
	public boolean visit(IItemAcceptor acceptor) {
		return acceptor.accept(this);
	}
}
