// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import org.junit.Test;
import owshell2mqtt.items.OwDevice;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DiscoveryTest {

	@Test
	public void ReadRoot() throws Exception {
		Discovery disc = new Discovery("localhost", new TestExecutor("./testdata/root"));
		assertTrue(disc.discoverTree());
		Network net = disc.getNetwork();
		assertTrue( net.getChildren().containsKey("1D.2D100D000000"));
		assertTrue( net.getChildren().containsKey("1D.AFE80E000000"));
		assertTrue( net.getChildren().containsKey("1F.1AE303000000"));
		assertTrue( net.getChildren().containsKey("1F.58E303000000"));
		assertTrue( net.getChildren().containsKey("1F.66AE03000000"));
		assertTrue( net.getChildren().containsKey("81.BE0929000000"));

		OwDevice dev = net.findDevice("10.B4C060010800");
		assertNotNull(dev);
		dev = net.findDevice("2624CAB5000000");
		assertNotNull(dev);

		assertNotNull(net.getAllDevices().get("1F.58E303000000"));

	}
}
