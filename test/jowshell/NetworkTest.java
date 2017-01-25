// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell;

import jowshell.items.OwData;
import jowshell.items.OwDevice;
import jowshell.system.ICommandExecution;
import jowshell.system.IExecute;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class NetworkTest implements ICommandExecution {
	private TestExecutor myTestExec = new TestExecutor("./testdata/root");

	@Test
	public void ReadRoot() throws Exception {
		Discovery disc = new Discovery("localhost", this, new TestLogger());
		assertTrue(disc.discoverTree());
		Network net = disc.getNetwork();
		assertTrue(net.getChildren().containsKey("1D.2D100D000000"));
		assertTrue(net.getChildren().containsKey("1D.AFE80E000000"));
		assertTrue(net.getChildren().containsKey("1F.1AE303000000"));
		assertTrue(net.getChildren().containsKey("1F.58E303000000"));
		assertTrue(net.getChildren().containsKey("1F.66AE03000000"));
		assertTrue(net.getChildren().containsKey("81.BE0929000000"));
	}

	@Test
	public void TestFindDevice() throws Exception {
		Discovery disc = new Discovery("localhost", this, new TestLogger());
		assertTrue(disc.discoverTree());
		Network net = disc.getNetwork();
		assertNotNull(net.getAllDevices().get("1F.58E303000000"));
	}

	@Test
	public void TestGetProperies() throws Exception {
		Discovery disc = new Discovery("localhost", this, new TestLogger());
		assertTrue(disc.discoverTree());
		Network net = disc.getNetwork();

		OwDevice dev = net.getAllDevices().get("10.B4C060010800");
		assertNotNull(dev);

		HashMap<String, OwData> props = dev.getData();
		OwData temp = props.get("temperature");
		String t = temp.read(this);
		assertEquals("       31.75", t);
	}

	@Test
	public void TestGetBinaryData() throws Exception {
		Discovery disc = new Discovery("localhost", this, new TestLogger());
		assertTrue(disc.discoverTree());
		Network net = disc.getNetwork();

		OwDevice dev = net.getAllDevices().get("1D.AFE80E000000");
		assertNotNull(dev);

		HashMap<String, OwData> props = dev.getData();
		OwData mem = props.get("memory");
		String data = mem.read(this);
		String dataAsHex = "466F6F0000FFFE0000FFFF0880FFFF0008FFF71000FFFF0000FFFD0000FFFD0408F7FF0000FFFF0001FDFF0000FFFF0001FFFFC000FFFF0000FFFF0000FF7F0000FFFF0000FDAF8400EFFD0000FFFF0000F7FF0000FFFF0000FDFF0800FDFF0000FFFF0000FFFF0000FF7F00007EEF0000FFFF0000FFFF0000DFDF4000FFFF0004FFFD0000FFFF0000FBFF00007FFF0000FFFF0000FFFF0000DFFF0000FFFF0000FFFF0000FFFF0400FFFF0000B3FF0000FFFF0002FFFF0101FFFF2400FFF70000FFFB0000FFFA0000FFFF0400FFFF0000FFFF0000FFFF0000EFFF0000FFFF8000FEBE0050FFFF0000FFFF0000FFBF0001FFFF0908FFFF2000BFFF8000FFFF0000FFFF0044FEFF0008FFFF8000FFFF0000FE7F0000FFFF0000FFFF0040FEFF0000FFFF0001FFFF2000FFEF0000FFEF0000FFFF0000FFFF0000FFF71000FFBF2000FF7F0000FBFF0800FFFF0050DFFF0000FFFF0000FFFF0000FEFF0000FFFF0000FFFF0000FFFF0080DBFF1000FFFF0000FFFF0002FFF70000FFFF2000FEFD0400FFFF0000FF7F2000FFFF8000FFFF0000F7FF0040FFFF0020FFFE0000FFFF00007FBF0000FFFF0000FFFF2000FFFF0000FFF78000FFFF0000FFFF0001FDFF1080FFFF0000FFFF8000FFFF0000FFFB000CFFFF0000FFFF0000FFFF0040FFFF0200FFFF0080FFFF0000FFFF0000FFFF0008FFFF0000FFFF0100FFFF0000FFFF00";
		assertEquals(dataAsHex, data);


	}

	@Override
	public String getOwRead() {
		return "owread";
	}

	@Override
	public String getOwDir() {
		return "owdir";
	}

	@Override
	public String getOwWrite() {
		return "owwrite";
	}

	@Override
	public IExecute getExec() {
		return myTestExec;
	}
}
