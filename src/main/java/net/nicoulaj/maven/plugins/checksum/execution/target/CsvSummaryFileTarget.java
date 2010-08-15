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
package net.nicoulaj.maven.plugins.checksum.execution.target;

import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * An {@link ExecutionTarget} that writes digests to a CSV file.
 *
 * FIXME This should output a CSV doc with one column per algorithm.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.1
 */
public class CsvSummaryFileTarget implements ExecutionTarget
{
    /**
     * The target file where the summary is written.
     */
    protected File summaryFile;

    /**
     * Build a new instance of {@link CsvSummaryFileTarget}.
     *
     * @param summaryFile the file to which to summary should be written.
     */
    public CsvSummaryFileTarget( String summaryFile )
    {
        this.summaryFile = new File( summaryFile );
    }

    /**
     * {@inheritDoc}
     */
    public void init() throws ExecutionTargetInitializationException
    {
        // Write the file header
        try
        {
            FileUtils.fileWrite( summaryFile.getPath(), "# File, Algorithm, Digest" );
        }
        catch ( IOException e )
        {
            throw new ExecutionTargetInitializationException( "Could not write to " + summaryFile.getPath() );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void write( String digest, File file, String algorithm ) throws ExecutionTargetWriteException
    {
        try
        {
            FileUtils.fileAppend( summaryFile.getPath(), "\n" + file.getName() + "," + algorithm + "," + digest );
        }
        catch ( IOException e )
        {
            throw new ExecutionTargetWriteException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void close()
    {
        // Nothing to do
    }
}
