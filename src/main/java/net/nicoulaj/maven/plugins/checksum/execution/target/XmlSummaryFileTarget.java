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

import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link net.nicoulaj.maven.plugins.checksum.execution.target.ExecutionTarget} that writes digests to an XML file.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.0
 */
public class XmlSummaryFileTarget implements ExecutionTarget
{
    /**
     * The number of spaces used to indent the output file.
     */
    public static final int XML_INDENTATION_SIZE = 2;

    /**
     * The association file => (algorithm,hashcode).
     */
    protected Map<File, Map<String, String>> filesHashcodes;

    /**
     * The target file where the summary is written.
     */
    protected File summaryFile;

    /**
     * Build a new instance of {@link net.nicoulaj.maven.plugins.checksum.execution.target.XmlSummaryFileTarget}.
     *
     * @param summaryFile the file to which the summary should be written.
     */
    public XmlSummaryFileTarget( File summaryFile )
    {
        this.summaryFile = summaryFile;
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {
        filesHashcodes = new HashMap<File, Map<String, String>>();
    }

    /**
     * {@inheritDoc}
     */
    public void write( String digest, File file, String algorithm )
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
    public void close() throws ExecutionTargetCloseException
    {
        // Open the target file.
        Writer outputStream;
        try
        {
            outputStream = new OutputStreamWriter( new FileOutputStream( summaryFile ) );
        }
        catch ( FileNotFoundException e )
        {
            throw new ExecutionTargetCloseException( e.getMessage() );
        }

        // Output hashcodes formatted in XML.        
        PrettyPrintXMLWriter xmlWriter = new PrettyPrintXMLWriter( outputStream, StringUtils.repeat( " ", XML_INDENTATION_SIZE ) );
        xmlWriter.startElement( "files" );
        for ( File file : filesHashcodes.keySet() )
        {
            xmlWriter.startElement( "file" );
            xmlWriter.addAttribute( "name", file.getName() );
            Map<String, String> fileHashcodes = filesHashcodes.get( file );
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
        }
        catch ( IOException e )
        {
            throw new ExecutionTargetCloseException( e.getMessage() );
        }
    }
}
