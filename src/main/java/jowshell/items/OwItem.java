// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.items;

import jowshell.actors.IItemAcceptor;
import jowshell.system.ICommandExecution;
import jowshell.system.IExecute;
import jowshell.logging.ILogger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class OwItem {
	protected final HashMap<String, OwItem> myChild = new HashMap<>();
	private final String myFullPath;
	protected final String myHost;
	protected ILogger myLogger;

	public OwItem(String fullPath, String host, ILogger logger) {
		myFullPath = fullPath;
		myHost = host;
		myLogger = logger;
	}

	public String getFullPath() {
		return myFullPath;
	}

	public void clear() {
		myChild.clear();
	}

	public void add(OwItem item) {
		myChild.put(item.getName(), item);
	}

	public String getName() {
		Path p = Paths.get(myFullPath);
		return p.getFileName().toString();
	}

	public void discover(ICommandExecution execute) {
		myChild.values().forEach(item -> discover(execute));
	}

	protected boolean isPathToDevice(String fullPath, ICommandExecution cmdExec) {
		// Since a device may be reported as an alias, we can't rely on the Family.Address to be present in the path.
		boolean isDevice = false;

		// A device has both address and alias properties.
		String addressPath = Paths.get(fullPath, "address").toString().replace("\\", "/");
		String aliasPath = Paths.get(fullPath, "alias").toString().replace("\\", "/");

		if (executeDir(cmdExec, fullPath)) {
			IExecute exec = cmdExec.getExec();
			isDevice = exec.getOutput().contains(addressPath) && exec.getOutput().contains(aliasPath);
		}

		return isDevice;
	}

	protected boolean executeDir(ICommandExecution cmdExec, String fullPath) {
		IExecute exec = cmdExec.getExec();
		return exec.execute(1, cmdExec.getOwDir(), "--dir", "-s", myHost, fullPath) == 0;
	}

	protected boolean executeRead(ICommandExecution cmdExec, boolean isHex) {
		List<String> cmd = new ArrayList<>();
		cmd.add(cmdExec.getOwRead());
		return executeIO(cmdExec, isHex, cmd);
	}

	protected boolean executeWrite(ICommandExecution cmdExec, boolean isHex) {
		List<String> cmd = new ArrayList<>();
		cmd.add(cmdExec.getOwWrite());
		return executeIO(cmdExec, isHex, cmd);
	}

	private boolean executeIO(ICommandExecution cmdExec, boolean isHex, List<String> cmd) {
		cmd.add("-s");
		cmd.add(myHost);
		if (isHex) {
			cmd.add("--hex");
		}
		cmd.add(getFullPath());

		IExecute exec = cmdExec.getExec();
		return exec.execute(1, cmd.toArray(new String[cmd.size()])) == 0;
	}

	public HashMap<String, OwItem> getChildren() {
		return myChild;
	}

	protected boolean traverseTreeWithActor(IItemAcceptor actor) {
		return !visit(actor) || traverseTree(actor);
	}

	/**
	 * Traverses the tree with the provided actor.
	 *
	 * @param actor The actor
	 * @return true if the actor says to stop traversal, otherwise false.
	 */
	protected boolean traverseTree(IItemAcceptor actor) {
		OwItem[] children = myChild.values().toArray(new OwItem[myChild.values().size()]);

		boolean done = false;

		for (int i = 0; !done && i < children.length; ++i) {
			done = children[i].traverseTreeWithActor(actor);
		}

		return done;
	}

	public abstract OwDevice getParentDevice();

	public abstract boolean visit(IItemAcceptor acceptor);
}
