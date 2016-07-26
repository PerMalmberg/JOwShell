// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.actors;

import jowshell.items.OwData;
import jowshell.items.OwDevice;
import jowshell.items.OwDirectory;
import jowshell.items.OwItem;

public interface IItemActor {
	/**
	 * Performs an operation on the device.
	 *
	 * @param item The item
	 * @return true if further operations should be performed, false if not.
	 */
	boolean act(OwItem item);

	/**
	 * Performs an operation on the device.
	 *
	 * @param dir The directory
	 * @return true if further operations should be performed, false if not.
	 */
	boolean act(OwDirectory dir);

	/**
	 * Performs an operation on the device.
	 *
	 * @param device The device
	 * @return true if further operations should be performed, false if not.
	 */
	boolean act(OwDevice device);

	/**
	 * Performs an operation on the data.
	 *
	 * @param data The data
	 * @return true if further operations should be performed, false if not.
	 */
	boolean act(OwData data);
}
