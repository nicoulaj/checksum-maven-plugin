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
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link net.nicoulaj.maven.plugins.checksum.execution.target.ExecutionTarget} that writes digests to an XML file.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.0
 */
public class XmlSummaryFileTarget
    implements ExecutionTarget
{
    /**
     * The number of spaces used to indent the output file.
     */
    public static final int XML_INDENTATION_SIZE = 2;

    /**
     * Encoding to use for generated files.
     */
    protected String encoding;

    /**
     * The association file => (algorithm,hashcode).
     */
    protected Map<ChecksumFile, Map<String, String>> filesHashcodes;

    /**
     * The target file where the summary is written.
     */
    protected File summaryFile;

    /**
     * List of listeners which are notified every time a XML file is created.
     *
     * @since 1.3
     */
    protected final Iterable<? extends ArtifactListener> artifactListeners;

    /**
     * Build a new instance of {@link net.nicoulaj.maven.plugins.checksum.execution.target.XmlSummaryFileTarget}.
     *  @param summaryFile the file to which the summary should be written.
     * @param encoding    the encoding to use for generated files.
     * @param artifactListeners
     */
    public XmlSummaryFileTarget(File summaryFile, String encoding, Iterable<? extends ArtifactListener> artifactListeners)
    {
        this.summaryFile = summaryFile;
        this.encoding = encoding;
        this.artifactListeners = artifactListeners;
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        filesHashcodes = new HashMap<ChecksumFile, Map<String, String>>();
    }

    /**
     * {@inheritDoc}
     */
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
    }

    /**
     * {@inheritDoc}
     */
    public void close(final String subPath)
        throws ExecutionTargetCloseException
    {
        // Make sure the parent directory exists.
        FileUtils.mkdir( summaryFile.getParent() );

        // Open the target file.
        Writer outputStream = null;
        try
        {
            outputStream = new OutputStreamWriter( new FileOutputStream( summaryFile ), encoding );
        }
        catch ( FileNotFoundException e )
        {
            throw new ExecutionTargetCloseException( e.getMessage() );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new ExecutionTargetCloseException( e.getMessage() );
        }

        // Write in sorted order (per globing argument)
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

        // Output hashcodes formatted in XML.
        PrettyPrintXMLWriter xmlWriter =
            new PrettyPrintXMLWriter( outputStream, StringUtils.repeat( " ", XML_INDENTATION_SIZE ) );
        xmlWriter.startElement( "files" );
        for ( Map.Entry<ChecksumFile, Map<String, String>> entry : entries )
        {
            ChecksumFile file = entry.getKey();
            xmlWriter.startElement( "file" );
			xmlWriter.addAttribute( "name", file.getRelativePath(file, subPath) );
            Map<String, String> fileHashcodes = entry.getValue();
            for ( String algorithm : fileHashcodes.keySet() )
            {
                xmlWriter.startElement( "hashcode" );
                xmlWriter.addAttribute( "algorithm", algorithm );
                xmlWriter.writeText( fileHashcodes.get( algorithm ) );
                xmlWriter.endElement();
            }
            xmlWriter.endElement();
        }
        xmlWriter.endElement();

        // Close the target file.
        try
        {
            outputStream.close();
            for (ArtifactListener artifactListener : artifactListeners) {
                artifactListener.artifactCreated(summaryFile, "xml");
            }
        }
        catch ( IOException e )
        {
            throw new ExecutionTargetCloseException( e.getMessage() );
        }
    }
}
