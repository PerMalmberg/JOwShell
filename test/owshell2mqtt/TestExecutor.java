// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import owshell2mqtt.system.IExecute;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestExecutor implements IExecute {
	private final String myRoot;
	private List<String> myOutput;

	public TestExecutor(String root) {
		myRoot = root;
	}


	@Override
	public int execute(int defaultReturnValue, String... commandArray) {
		int res = defaultReturnValue;

		List<String> cmd = new ArrayList<>();
		Collections.addAll(cmd, commandArray);

		if (cmd.get(0).contains("owdir")) {
			res = readDir(cmd) ? 0 : defaultReturnValue;
		} else if (cmd.get(0).contains("owread")) {
			res = readData(cmd) ? 0 : defaultReturnValue;
		}

		return res;
	}

	private boolean readData(List<String> cmd) {
		myOutput = new ArrayList<>();
		return false;
	}

	private boolean readDir(List<String> cmd) {
		boolean res = false;
		myOutput = new ArrayList<>();

		String path = findPath(cmd);
		if (path != null) {
			Path p = Paths.get(myRoot, path);
			File f = p.toFile();
			if (f.exists()) {
				File[] children = f.listFiles();
				if( children != null ) {
					for (File c : children) {
						// Replace \ with /
						String currentPath = c.getPath().replace("\\", "/");
						// Remove the path to the test data from the path
						currentPath = currentPath.replace(myRoot, "");
						// Simulate the --dir flag by adding trailing "/" to directories
						myOutput.add(c.isDirectory() ? currentPath + "/" : currentPath);
					}
					res = true;
				}
			}
		}


		return res;
	}

	private String findPath(List<String> cmd) {
		String path = null;

		for (int i = 0; path == null && i < cmd.size(); ++i) {
			if (cmd.get(i).startsWith("/")) {
				path = cmd.get(i);
			}
		}

		return path;
	}

	@Override
	public void setTimeout(int timeoutInMilliSeconds) {

	}

	@Override
	public Exception getLastException() {
		return null;
	}

	public List<String> getOutput() {
		return myOutput;
	}
}
