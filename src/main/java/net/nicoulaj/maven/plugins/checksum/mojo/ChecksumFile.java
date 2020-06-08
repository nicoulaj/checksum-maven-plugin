/*
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2018 checksum-maven-plugin contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
 * @since 1.3
 */
public class ChecksumFile
{
	protected final String basePath;

    protected final File file;

    protected final String extension;

    protected final String classifier;

	public ChecksumFile(String basePath, File file, String extension, String classifier)
	{
		this.basePath = basePath;
        this.file = file;
        this.extension = extension;
        this.classifier = classifier;
	}

	public String getBasePath()
	{
		return basePath;
	}

    public File getFile()
    {
        return file;
    }

    public String getExtension()
    {
        return extension;
    }

    public String getClassifier()
    {
        return classifier;
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
