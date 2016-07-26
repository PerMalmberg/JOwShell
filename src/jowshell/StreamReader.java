// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StreamReader extends Thread {
	private final InputStream is;
	private final ArrayList<String> myOutput = new ArrayList<>();

	public StreamReader(InputStream is) {
		this.is = is;
	}

	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line = br.readLine();
			while (line != null) {
				myOutput.add(line);
				line = br.readLine();
			}
		} catch (IOException ioe) {
			// Do nothing
		}
	}

	public List<String> getOutput() {
		return myOutput;
	}
}