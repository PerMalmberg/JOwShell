// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package owshell2mqtt;

import owshell2mqtt.items.DataTypes.DataType;
import owshell2mqtt.items.StructureInfo;
import owshell2mqtt.system.IExecute;
import owshell2mqtt.system.ShellExecute;

import java.util.ArrayList;
import java.util.List;

public class OwRead {
	private String myHost;
	private IExecute myExec;

	public OwRead(IExecute exec, String host) {
		myExec = exec;
		myHost = host;
	}

	public boolean read(String pathToProperty, StructureInfo info)
	{
		List<String> cmd = new ArrayList<>();
		cmd.add("owread");
		cmd.add("-s");
		cmd.add(myHost);
		if( info.getType() == DataType.b) {
			cmd.add("--hex");
		}
		cmd.add(pathToProperty);

		return myExec.execute(1, cmd.toArray(new String[cmd.size()])) == 0;
	}

	public List<String> getData() {
		return myExec.getOutput();
	}
}
