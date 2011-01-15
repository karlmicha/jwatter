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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilTest
{
	@Test
	public void testGetUrlFilenameEmpty ()
	{
		assertEquals("", StringUtil.getUrlFilename(""));
	}

	@Test
	public void testGetUrlFilenameNoUrl ()
	{
		assertEquals("javascript:window.open('newwindow')", StringUtil
				.getUrlFilename("javascript:window.open('newwindow')"));
	}

	@Test
	public void testGetUrlFilenameUrl ()
	{
		assertEquals("filename.html", StringUtil
				.getUrlFilename("http://www.test.com/path/filename.html"));
	}

	@Test
	public void testGetUrlFilenameUrlQuery ()
	{
		assertEquals(
				"filename.php?q=search&hl=en",
				StringUtil
						.getUrlFilename("http://www.test.com/path/filename.php?q=search&hl=en"));
	}

	@Test
	public void testGetUrlFilenameUrlNoPath ()
	{
		assertEquals("", StringUtil.getUrlFilename("http://www.test.com/"));
	}

	@Test
	public void testGetUrlFilenameUrlDirectory ()
	{
		assertEquals("path", StringUtil
				.getUrlFilename("http://www.test.com/path/"));
	}
}
