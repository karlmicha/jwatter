/*
Copyright 2011 Karl-Michael Schneider

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.jwatter.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class StringUtil
{
	protected static final Pattern CLASSNAME_PATTERN =
			Pattern.compile("^[A-Z][A-Za-z0-9]*$");

	public static String transform ( String s, boolean titleCase,
			boolean makeIdentifier, int truncate )
	{
		char[] characters = s.toCharArray(); // transform string in-place
		int j = 0; // index into transformed string
		int length = 0; // length of transformed string
		boolean startWord = titleCase; // only relevant for title case
		for ( int i = 0 ; i < characters.length ; )
		{
			// cut off transformed string
			if ( truncate > 0 && length >= truncate ) break;

			// get next code point from source string
			int c = Character.codePointAt(characters, i);

			// transformed character
			int t = c;

			boolean isWordChar = titleCase && Character.isLetterOrDigit(c);
			boolean isIdentifierChar =
					makeIdentifier
							&& (i == 0	? Character.isJavaIdentifierStart(c)
										: Character.isJavaIdentifierPart(c));

			if ( isWordChar || isIdentifierChar )
			{
				// make first character in word uppercase
				if ( startWord )
				{
					t = Character.toUpperCase(t);
					startWord = false;
				}
			}
			else
			{
				// next character starts a new word (if it is a word character)
				startWord = true;
			}

			// skip the character if making identifier and it is not an
			// identifier character
			if ( !makeIdentifier || isIdentifierChar )
			{
				// copy character (deleted one or more characters previously)
				if ( j < i )
				{
					Character.toChars(t, characters, j);
				}

				// advance transformed string index
				j += Character.charCount(t);
			}

			// advance source string index
			i += Character.charCount(c);
		}
		return new String(characters, 0, j);
	}

	public static String titleCase ( String s )
	{
		return transform(s, true, false, 0);
	}

	public static String titleCaseTruncate ( String s, int maxlen )
	{
		return transform(s, true, false, maxlen);
	}

	public static String makeIdentifier ( String s )
	{
		return transform(s, true, true, 0);
	}

	public static String makeIdentifier ( String s, int maxlen )
	{
		return transform(s, true, true, maxlen);
	}

	public static String firstLowercase ( String s )
	{
		if ( s.equals("") )
		{
			return "";
		}
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	/**
	 * Extract the filename (and query, if applicable) from a URL.
	 * 
	 * @param s
	 *            a string
	 * @return the filename, and possibly the query, in s if s represents a URL;
	 *         else s
	 */
	public static String getUrlFilename ( String s )
	{
		try
		{
			URL url = new URL(s);
			String query = url.getQuery();
			return new File(url.getPath()).getName()
					+ (query != null ? "?" + query : "");
		}
		catch ( MalformedURLException e )
		{
			return s;
		}
	}

	public static boolean isValidClassName ( String s )
	{
		return CLASSNAME_PATTERN.matcher(s).matches();
	}

	public static boolean containsNonLatin1Characters ( String s )
	{
		for ( int offset = 0 ; offset < s.length() ; )
		{
			int c = s.codePointAt(offset);
			if ( c > 255 ) return true;
			offset += Character.charCount(c);
		}
		return false;
	}

	public static String getEncoding ( String s )
	{
		return containsNonLatin1Characters(s) ? "UTF-8" : "ISO-8859-1";
	}
}
