// Copyright (c) 2016 Per Malmberg
// Licensed under MIT, see LICENSE file.

package jowshell.items;

// http://owfs.org/index.php?page=structure-directory

import jowshell.items.DataTypes.Access;
import jowshell.items.DataTypes.Changeability;
import jowshell.items.DataTypes.DataType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StructureInfo {
	private DataType myType;
	private int myIndex;
	private int myElements;
	private Access myAccess;
	private int mySize;
	private Changeability myChangeability;

	private final Pattern pattern = Pattern.compile("^([Diuflabydtgp]),(-?\\d*),(-?\\d*),(rw|wo|ro|oo),(\\d*),(v|s|f|t),$");

	public StructureInfo(String data) throws IllegalArgumentException
	{
		Matcher m = pattern.matcher(data);
		if( !m.matches() ) {
			throw new IllegalArgumentException( "The provided data ('" + data + "' does not match the expected format" );
		}

		myType = DataType.valueOf(m.group(1));
		myIndex = Integer.parseInt(m.group(2));
		myElements = Integer.parseInt(m.group(3));
		myAccess = Access.valueOf(m.group(4));
		mySize = Integer.parseInt(m.group(5));
		myChangeability = Changeability.valueOf(m.group(6));
	}

	public DataType getType() {
		return myType;
	}

	public int getIndex() {
		return myIndex;
	}

	public int getElements() {
		return myElements;
	}

	public Access getAccess() {
		return myAccess;
	}

	public int getSize() {
		return mySize;
	}

	public Changeability getChangeability() {
		return myChangeability;
	}

	public boolean isReadable() {
		return getAccess() == Access.rw || getAccess() == Access.ro;
	}

	public boolean isWriteable() {
		return getAccess() == Access.rw || getAccess() == Access.wo;
	}
}
