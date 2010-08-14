/*
 * Copyright 2010 Julien Nicoulaud <julien.nicoulaud@gmail.com>
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
package net.nicoulaj.maven.plugins.checksum.test.integration;

import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.util.Map;

/**
 * Utility object used by post-build scripts.
 *
 * <p>See {@code src/main/test-integration/projects/.../postbuild.groovy} scripts.</p>
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.1
 */
public class PostBuildScriptHelper
{
    /**
     * TODO.
     */
    public static final String BUILD_LOG_FILE = "build.log";

    /**
     * TODO.
     */
    protected File baseDirectory;

    /**
     * TODO.
     */
    protected File localRepositoryPath;

    /**
     * TODO.
     */
    protected Map context;

    /**
     * TODO.
     *
     * @param baseDirectory       TODO.
     * @param localRepositoryPath TODO.
     * @param context             TODO.
     */
    public PostBuildScriptHelper( File baseDirectory, File localRepositoryPath, Map context )
    {
        this.baseDirectory = baseDirectory;
        this.localRepositoryPath = localRepositoryPath;
        this.context = context;
    }

    /**
     * TODO.
     *
     * @return TODO.
     * @throws Exception TODO.
     */
    public String getBuildLog() throws Exception
    {
        return FileUtils.fileRead( new File( baseDirectory, BUILD_LOG_FILE ) );
    }

    /**
     * TODO.
     *
     * @param path TODO.
     * @throws Exception TODO.
     */
    public void assertFileExists( String path ) throws Exception
    {
        if ( !new File( baseDirectory, path ).exists() )
        {
            throw new Exception( "The file " + path + " is missing." );
        }
    }

    /**
     * TODO.
     *
     * @param path TODO.
     * @throws Exception TODO.
     */
    public void assertFileIsNotEmpty( String path ) throws Exception
    {
        File file = new File( baseDirectory, path );
        if ( !file.isFile() )
        {
            throw new Exception( "The file " + path + " is missing." );
        }
        else
        {
            if ( FileUtils.fileRead( file ).length() == 0 )
            {
                throw new Exception( "The file " + path + " is empty." );
            }
        }
    }

    /**
     * TODO.
     *
     * @param search TODO.
     * @throws Exception TODO.
     */
    public void assertBuildLogContains( String search ) throws Exception
    {
        if ( !getBuildLog().contains( search ) )
        {
            throw new Exception( "The build log does not contain '" + search + "'." );
        }
    }
}
