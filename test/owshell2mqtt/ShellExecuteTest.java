// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ShellExecuteTest {

	@Test
	public void testExecute() throws Exception {
		ShellExecute se = new ShellExecute("java", "-version");
		assertEquals(0, se.execute(1000, 1));
		assertEquals(null, se.getLastException());
	}

	@Test
	public void testNewArguments() throws Exception {
		ShellExecute se = new ShellExecute("java", "-version");
		assertEquals(0, se.execute(1000, 1));
		assertTrue(se.getOutput().toString().contains("java version"));
		assertEquals(null, se.getLastException());
		assertEquals(0, se.execute(1000, 1, "-help"));
		assertFalse(se.getOutput().toString().contains("java version"));
		assertTrue(se.getOutput().toString().contains("Usage:"));
		assertEquals(null, se.getLastException());
	}

	@Test
	public void testUnknownCommand() throws Exception {
		ShellExecute se = new ShellExecute("qwerty123");
		assertEquals(1, se.execute(1000, 1));
		assertNotNull(se.getLastException());
	}
}
