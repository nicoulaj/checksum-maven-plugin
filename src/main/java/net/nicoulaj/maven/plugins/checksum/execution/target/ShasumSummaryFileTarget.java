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
import net.nicoulaj.maven.plugins.checksum.mojo.ChecksumFile;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * An {@link ExecutionTarget} that writes digests to a {@code shasum} file
 * compatible with "{@code shasum -b -a <algo> <files> > sha.sum}". For
 * compatibility with {@code shasum} only SHA-1, SHA-224, SHA-256, SHA-384,
 * SHA-512, SHA-512224 and SHA-512256 are supported. Only one algorithm may be
 * used at a time.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @author Mike Duigou
 * @since 1.3
 */
public class ShasumSummaryFileTarget
    implements ExecutionTarget
{
    /**
     * The line separator character.
     */
    public static final String LINE_SEPARATOR = System.getProperty( "line.separator" );
    /**
     * The shasum field separator (and file type binary identifier)
     */
    public static final String SHASUM_FIELD_SEPARATOR = " *";

    /**
     * The shasum binary character.
     */
    public static final String SHASUM_BINARY_FILE = "*";

    /**
     * The association file =&gt; (algorithm,hashcode).
     */
    protected Map<ChecksumFile, Map<String, String>> filesHashcodes;

    /**
     * The set of algorithms encountered.
     */
    protected SortedSet<String> algorithms;

    /**
     * The target file where the summary is written.
     */
    protected File summaryFile;

    /**
     * List of listeners which are notified every time a sum file is created.
     *
     * @since 1.3
     */
    protected final Iterable<? extends ArtifactListener> artifactListeners;

    /**
     * Build a new instance of {@link ShasumSummaryFileTarget}.
     *
     * @param summaryFile the file to which the summary should be written.
     * @param artifactListeners listeners which are notified every time a CSV file is created
     */
    public ShasumSummaryFileTarget( File summaryFile, Iterable<? extends ArtifactListener> artifactListeners )
    {
        this.summaryFile = summaryFile;
        this.artifactListeners = artifactListeners;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init()
    {
        filesHashcodes = new HashMap<ChecksumFile, Map<String, String>>();
        algorithms = new TreeSet<String>();
   }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write( String digest, ChecksumFile file, String algorithm )
    {
        // Initialize an entry for the file if needed.
        if ( !filesHashcodes.containsKey( file ) )
        {
            filesHashcodes.put( file, new HashMap<String, String>() );
        }

        // Store the algorithm => hashcode mapping for this file.
        Map<String, String> fileHashcodes = filesHashcodes.get( file );
        fileHashcodes.put( algorithm, digest );

        // Store the algorithm.
        algorithms.add( algorithm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close(final String subPath)
        throws ExecutionTargetCloseException
    {
        StringBuilder sb = new StringBuilder();

        if (algorithms.size() != 1)
            throw new ExecutionTargetCloseException("Must use only one type of hash");

        // shasum entires are traditionally written in sorted order (per globing argument)
        @SuppressWarnings("unchecked")
        Map.Entry<ChecksumFile, Map<String, String>>[] entries = filesHashcodes.entrySet().toArray((Map.Entry<ChecksumFile, Map<String, String>>[]) new Map.Entry[0] );
        Arrays.sort(entries, new Comparator<Map.Entry<ChecksumFile, Map<String, String>>>() {
            @Override
            public int compare(Map.Entry<ChecksumFile, Map<String, String>> o1, Map.Entry<ChecksumFile, Map<String, String>> o2) {
                ChecksumFile f1 = o1.getKey();
                ChecksumFile f2 = o2.getKey();
                return f1.getRelativePath(f1, subPath).compareTo(f2.getRelativePath(f2, subPath));
            }
        });

        // Write a line for each file.
        for ( Map.Entry<ChecksumFile, Map<String, String>> entry : entries )
        {
            ChecksumFile file = entry.getKey();
            Map<String, String> fileHashcodes = entry.getValue();
            for ( String algorithm : algorithms )
            {
                if ( fileHashcodes.containsKey( algorithm ) )
                {
                    sb.append( fileHashcodes.get( algorithm ) );
                }
            }
            sb.append(SHASUM_FIELD_SEPARATOR)
                    .append( file.getRelativePath(entry.getKey(), subPath))
                    .append( LINE_SEPARATOR );
        }

        // Make sure the parent directory exists.
        FileUtils.mkdir( summaryFile.getParent() );

        // Write the result to the summary file.
        try
        {
            FileUtils.fileWrite( summaryFile.getPath(), "US-ASCII", sb.toString() );
             for (ArtifactListener artifactListener : artifactListeners) {
                artifactListener.artifactCreated(summaryFile, "sum");
            }
       }
        catch ( IOException e )
        {
            throw new ExecutionTargetCloseException( e.getMessage() );
        }
    }
}
