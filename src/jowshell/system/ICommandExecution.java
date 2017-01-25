// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.system;

public interface ICommandExecution {
	String getOwRead();
	String getOwDir();
	String getOwWrite();
	IExecute getExec();
}
