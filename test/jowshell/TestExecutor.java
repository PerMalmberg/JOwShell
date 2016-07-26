// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import jowshell.system.IExecute;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestExecutor implements IExecute {
	private final String myRoot;
	private List<String> myOutput;
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

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
		boolean res = false;

		myOutput = new ArrayList<>();

		byte[] data = null;

		String path = findPath(cmd);
		if( path != null) {
			Path p = Paths.get(myRoot, path);
			File f = p.toFile();
			if (f.exists()) {
				try (FileInputStream fi = new FileInputStream(f)) {
					data = new byte[(int) f.length()];
					fi.read(data);
				} catch (Exception e) {
					// Nada
					data = null;
				}
			}

			boolean asHex = cmd.contains("--hex");
			if (data != null) {
				if (asHex) {
					myOutput.add(bytesToHex(data));
				} else {
					myOutput.add(new String(data));
				}
				res = true;
			}
		}

		return res;
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
				if (children != null) {
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

	@Override
	public List<String> getError() {
		return null;
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
