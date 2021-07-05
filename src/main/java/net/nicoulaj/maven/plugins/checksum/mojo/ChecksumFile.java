/*
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2021 checksum-maven-plugin contributors
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
 * @version $Id: $Id
 */
public class ChecksumFile
{
	protected final String basePath;

    protected final File file;

    protected final String extension;

    protected final String classifier;

	/**
	 * <p>Constructor for ChecksumFile.</p>
	 *
	 * @param basePath a {@link java.lang.String} object
	 * @param file a {@link java.io.File} object
	 * @param extension a {@link java.lang.String} object
	 * @param classifier a {@link java.lang.String} object
	 */
	public ChecksumFile(String basePath, File file, String extension, String classifier)
	{
		this.basePath = basePath;
        this.file = file;
        this.extension = extension;
        this.classifier = classifier;
	}

	/**
	 * <p>Getter for the field <code>basePath</code>.</p>
	 *
	 * @return a {@link java.lang.String} object
	 */
	public String getBasePath()
	{
		return basePath;
	}

    /**
     * <p>Getter for the field <code>file</code>.</p>
     *
     * @return a {@link java.io.File} object
     */
    public File getFile()
    {
        return file;
    }

    /**
     * <p>Getter for the field <code>extension</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * <p>Getter for the field <code>classifier</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getClassifier()
    {
        return classifier;
    }

	/**
	 * <p>getRelativePath.</p>
	 *
	 * @param file a {@link net.nicoulaj.maven.plugins.checksum.mojo.ChecksumFile} object
	 * @param subPath a {@link java.lang.String} object
	 * @return a {@link java.lang.String} object
	 */
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
