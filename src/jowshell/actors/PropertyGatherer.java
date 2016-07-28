// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.actors;

import jowshell.items.OwData;
import jowshell.items.OwDevice;
import jowshell.items.OwDirectory;
import jowshell.items.OwItem;

import java.util.HashMap;

public class PropertyGatherer implements IItemAcceptor {
	private final HashMap<String, OwData> myData = new HashMap<>();

	@Override
	public boolean accept(OwItem item) {
		return true;
	}

	@Override
	public boolean accept(OwDirectory dir) {
		// Recurse into directories
		for( OwItem item : dir.getChildren().values()) {
			item.visit(this);
		}
		return true;
	}

	@Override
	public boolean accept(OwDevice device) {
		return true;
	}

	@Override
	public boolean accept(OwData data) {
		myData.put(data.getFullPropertyName(), data);
		return true;
	}

	public HashMap<String,OwData> getData(){
		return myData;
	}
}
