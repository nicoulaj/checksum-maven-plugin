/*-
 * ====================================================================
 * checksum-maven-plugin
 * ====================================================================
 * Copyright (C) 2010 - 2016 Julien Nicoulaud <julien.nicoulaud@gmail.com>
 * ====================================================================
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
 * ====================================================================
 */

package net.nicoulaj.maven.plugins.checksum.execution.target;


import net.nicoulaj.maven.plugins.checksum.artifacts.ArtifactListener;
import net.nicoulaj.maven.plugins.checksum.digest.DigesterFactory;
import net.nicoulaj.maven.plugins.checksum.mojo.ChecksumFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;

/**
 * An {@link ExecutionTarget} that writes digests to separate files.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.0
 */
public class OneHashPerFileTarget
    implements ExecutionTarget
{
    /**
     * Encoding to use for generated files.
     */
    protected final String encoding;

    /**
     * The files output directory.
     */
    protected final File outputDirectory;

    /**
     * List of listeners which are notified every time a checksum file is created.
     *
     * @since 1.3
     */
    protected final Iterable<? extends ArtifactListener> artifactListeners;

    /**
     * Append the filename to the hash file
     *
     * @since 1.4
     */
    protected boolean appendFilename = false;

    /**
     * Build a new instance of {@link OneHashPerFileTarget}.
     * @param encoding        the encoding to use for generated files.
     * @param outputDirectory the files output directory.
     * @param artifactListeners listeners which are notified every time a CSV file is created
     */
    public OneHashPerFileTarget(String encoding, File outputDirectory, Iterable<? extends ArtifactListener> artifactListeners)
    {
        this.encoding = encoding;
        this.outputDirectory = outputDirectory;
        this.artifactListeners = artifactListeners;
    }

    /**
     * Build a new instance of {@link OneHashPerFileTarget}.
     *  @param encoding        the encoding to use for generated files.
     * @param outputDirectory the files output directory.
     * @param artifactListeners listeners which are notified every time a CSV file is created
     * @param appendFilename add filename in generated file
     */
    public OneHashPerFileTarget(String encoding, File outputDirectory, Iterable<? extends ArtifactListener> artifactListeners, boolean appendFilename)
    {
        this.encoding = encoding;
        this.outputDirectory = outputDirectory;
        this.artifactListeners = artifactListeners;
        this.appendFilename = appendFilename;
    }

    /**
     * Build a new instance of {@link OneHashPerFileTarget}.
     *
     * @param encoding the encoding to use for generated files.
     * @param artifactListeners listeners which are notified every time a CSV file is created
     */
    public OneHashPerFileTarget(String encoding, Iterable<? extends ArtifactListener> artifactListeners)
    {
        this( encoding, null, artifactListeners);
    }

    /**
     * {@inheritDoc}
     */
    public void init()
        throws ExecutionTargetInitializationException
    {
        // Make sure the output directory exists or can be created.
        if ( outputDirectory != null )
        {
            if ( outputDirectory.exists() && !outputDirectory.isDirectory() )
            {
                throw new ExecutionTargetInitializationException(
                    "'" + outputDirectory.getPath() + "' already exists and is not a directory." );
            }
            else
            {
                outputDirectory.mkdirs();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void write( String digest, ChecksumFile file, String algorithm )
        throws ExecutionTargetWriteException
    {
        try
        {
            File outputFileDirectory = ( outputDirectory != null ) ? outputDirectory : file.getFile().getParentFile();
            String fileExtension = DigesterFactory.getInstance().getFileDigester(algorithm).getFileExtension();
            String outputFileName = file.getFile().getName() + fileExtension;
            File outputFile = new File(outputFileDirectory.getPath(), outputFileName);
            StringBuilder digestToPrint = new StringBuilder(digest);
            if(appendFilename){
                digestToPrint.append("  ");
                digestToPrint.append(file.getFile().getName());
            }
            Files.write(outputFile.toPath(), digestToPrint.toString().getBytes(encoding), StandardOpenOption.CREATE);

            for (ArtifactListener artifactListener : artifactListeners) {
                artifactListener.artifactCreated(outputFile, fileExtension, file.getExtension(), file.getClassifier());
            }
        }
        catch ( IOException | NoSuchAlgorithmException e )
        {
            throw new ExecutionTargetWriteException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void close(String subPath)
    {
        // Nothing to do
    }
}
