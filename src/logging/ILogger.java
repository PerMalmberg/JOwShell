// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package logging;

public interface ILogger {
	void debug(String msg);
	void error(String msg);
	void error(Exception ex);
}
