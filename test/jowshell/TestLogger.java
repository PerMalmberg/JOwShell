// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import logging.ILogger;

import java.util.Arrays;

public class TestLogger implements ILogger {
	@Override
	public void debug(String msg) {
		System.out.println(msg);
	}

	@Override
	public void error(String msg) {
		System.out.println(msg);
	}

	@Override
	public void error(Exception ex) {
		System.out.println(ex.getMessage());
		System.out.println(Arrays.toString(ex.getStackTrace()));
	}
}
