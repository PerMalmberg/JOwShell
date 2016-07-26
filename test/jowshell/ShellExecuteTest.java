// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import org.junit.Test;
import jowshell.system.IExecute;
import jowshell.system.ShellExecute;

import static org.junit.Assert.*;

public class ShellExecuteTest {

	@Test
	public void testExecute() throws Exception {
		IExecute se = new ShellExecute();
		assertEquals(1, se.execute(1, "java"));
		assertEquals(null, se.getLastException());
	}

	@Test
	public void testNewArguments() throws Exception {
		IExecute se = new ShellExecute();
		se.setTimeout(1000);
		assertEquals(0, se.execute(1, "java", "-version"));
		assertTrue(se.getOutput().toString().contains("java version"));
		assertEquals(null, se.getLastException());
		assertEquals(0, se.execute(1, "java", "-help"));
		assertFalse(se.getOutput().toString().contains("java version"));
		assertTrue(se.getOutput().toString().contains("Usage:"));
		assertEquals(null, se.getLastException());
	}

	@Test
	public void testUnknownCommand() throws Exception {
		IExecute se = new ShellExecute();
		assertEquals(1, se.execute(1, "qwerty123"));
		assertNotNull(se.getLastException());
	}
}
