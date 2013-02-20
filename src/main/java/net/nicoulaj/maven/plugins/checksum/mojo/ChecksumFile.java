/*
 * Copyright 2010-2012 Julien Nicoulaud <julien.nicoulaud@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nicoulaj.maven.plugins.checksum.mojo;

import java.io.File;

/**
 * Represent a file and the base directory it came from.
 *
 * @author <a href="mailto:jerome.lacube@gmail.com">Jerome Lacube</a>
 * @since 1.4
 */
public class ChecksumFile
{
	protected String basePath;
	
	protected File file;

	public ChecksumFile(String basePath, File file)
	{
		this.basePath = basePath;
		this.file = file;
	}
	
	public String getBasePath()
	{
		return basePath;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public String getRelativePath(ChecksumFile file, String subPath)
	{
		String filePath = file.getFile().getName();
		
		if ( subPath != null ) {
			if ( file.getBasePath() != null && !file.getBasePath().isEmpty() )
			{
				String basePath = file.getBasePath();
				
				if ( !basePath.endsWith( System.getProperty("file.separator") ) )
					basePath = basePath + System.getProperty("file.separator");
				
				filePath = file.getFile().getPath().replace(basePath, "");
				
				if ( !subPath.isEmpty() )
				{
					if ( !subPath.endsWith( System.getProperty("file.separator") ) )
						subPath = subPath + System.getProperty("file.separator");
					
					filePath = filePath.replace(subPath, "");
				}
			}
		}
		
		return filePath;
	}
}