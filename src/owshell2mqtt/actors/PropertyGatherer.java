// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt.actors;

import owshell2mqtt.items.OwData;
import owshell2mqtt.items.OwDevice;
import owshell2mqtt.items.OwDirectory;
import owshell2mqtt.items.OwItem;

import java.util.HashMap;

public class PropertyGatherer implements IItemActor {
	private final HashMap<String, OwData> myData = new HashMap<>();

	@Override
	public boolean act(OwItem item) {
		return true;
	}

	@Override
	public boolean act(OwDirectory dir) {
		return true;
	}

	@Override
	public boolean act(OwDevice device) {
		return true;
	}

	@Override
	public boolean act(OwData data) {
		myData.put(data.getFullPropertyName(), data);
		return true;
	}

	public HashMap<String,OwData> getData(){
		return myData;
	}
}
