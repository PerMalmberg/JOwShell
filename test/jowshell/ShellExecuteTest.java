// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import jowshell.logging.ILogger;
import org.junit.Test;
import jowshell.system.IExecute;
import jowshell.system.ShellExecute;

import static org.junit.Assert.*;

public class ShellExecuteTest implements ILogger {

	@Test
	public void testExecute() throws Exception {
		IExecute se = new ShellExecute(this);
		assertEquals(1, se.execute(1, "java"));
		assertEquals(null, se.getLastException());
	}

	@Test
	public void testNewArguments() throws Exception {
		IExecute se = new ShellExecute(this);
		se.setTimeout(1000);
		assertEquals(0, se.execute(1, "java", "-version"));
		assertTrue(se.getError().toString().contains("java version"));
		assertEquals(null, se.getLastException());
		assertEquals(0, se.execute(1, "java", "-help"));
		assertFalse(se.getOutput().toString().contains("java version"));
		assertTrue(se.getError().toString().contains("Usage:"));
		assertEquals(null, se.getLastException());
	}

	@Test
	public void testUnknownCommand() throws Exception {
		IExecute se = new ShellExecute(this);
		assertEquals(1, se.execute(1, "qwerty123"));
		assertNotNull(se.getLastException());
	}

	@Override
	public void debug(String msg) {

	}

	@Override
	public void error(String msg) {

	}

	@Override
	public void error(Exception ex) {

	}
}
