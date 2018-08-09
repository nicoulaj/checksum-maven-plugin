/*
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2017 checksum-maven-plugin contributors
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

import org.apache.maven.model.FileSet;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Compute specified files checksum digests and store them in individual files
 * and/or a summary file.
 *
 * The files are not filtered.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.0
 */
@Mojo(
    name = FilesMojo.NAME,
    defaultPhase = LifecyclePhase.VERIFY,
    requiresProject = true,
    inheritByDefault = false,
    threadSafe = true )
public class FilesMojo
    extends AbstractChecksumMojo
{
    /**
     * The mojo name.
     */
    public static final String NAME = "files";

    /**
     * The default file inclusion pattern.
     *
     * @see #getFilesToProcess()
     */
    protected static final String[] DEFAULT_INCLUDES = { "**/**" };

    /**
     * The list of files to process.
     *
     * <p> Use the following syntax:
     * <pre>&lt;fileSets&gt;
     *   &lt;fileSet&gt;
     *     &lt;directory&gt;...&lt;/directory&gt;
     *     &lt;includes&gt;
     *       &lt;include&gt;...&lt;/include&gt;
     *     &lt;/includes&gt;
     *     &lt;excludes&gt;
     *       &lt;exclude&gt;...&lt;/exclude&gt;
     *     &lt;/excludes&gt;
     *   &lt;/fileSet&gt;
     * &lt;/fileSets&gt;</pre>
     * </p>
     *
     * @since 1.1
     */
    @Parameter( required = true )
    protected List<FileSet> fileSets;

    /**
     * Indicates whether the build will store checksums in separate files (one file per algorithm per artifact).
     *
     * @since 1.0
     */
    @Parameter( defaultValue = "true" )
    protected boolean individualFiles;

    /**
     * The directory where output files will be stored. Leave unset to have each file next to the source file.
     *
     * @since 1.0
     */
    @Parameter
    protected String individualFilesOutputDirectory;

    /**
     * Indicates whether the build will store checksums to a single CSV summary file.
     *
     * @since 1.0
     */
    @Parameter( defaultValue = "true" )
    protected boolean csvSummary;

    /**
     * The name of the summary file created if the option is activated.
     *
     * @see #csvSummary
     * @since 1.0
     */
    @Parameter( defaultValue = "checksums.csv" )
    protected String csvSummaryFile;

    /**
     * Indicates whether the build will store checksums to a single XML summary file.
     *
     * @since 1.0
     */
    @Parameter( defaultValue = "false" )
    protected boolean xmlSummary;

    /**
     * The name of the summary file created if the option is activated.
     *
     * @see #xmlSummary
     * @since 1.0
     */
    @Parameter( defaultValue = "checksums.xml" )
    protected String xmlSummaryFile;

    /**
     * Indicates whether the build will store checksums to a single shasum summary file.
     *
     * @since 1.3
     */
    @Parameter( defaultValue = "false" )
    protected boolean shasumSummary;

    /**
     * The name of the summary file created if the option is activated.
     *
     * @see #shasumSummary
     * @since 1.3
     */
    @Parameter( defaultValue = "checksums.sha" )
    protected String shasumSummaryFile;

    /**
     * Fail if no file found to calculate checksum.
     *
     * @since 1.7
     */
    @Parameter( defaultValue = "true" )
    protected boolean failIfNoFiles;

    /**
     * Constructor.
     */
    public FilesMojo() {
        super(true, true, true);
    }

    /**
     * Build the list of files from which digests should be generated.
     *
     * @return the list of files that should be processed.
     */
    @Override
    protected List<ChecksumFile> getFilesToProcess()
    {
        final List<ChecksumFile> filesToProcess = new ArrayList<ChecksumFile>();
        for ( final FileSet fileSet : fileSets )
        {
            final DirectoryScanner scanner = new DirectoryScanner();
            final String fileSetDirectory = (new File( fileSet.getDirectory() ) ).getPath();

            scanner.setBasedir( fileSetDirectory );
            String[] includes;
            if ( fileSet.getIncludes() != null && !fileSet.getIncludes().isEmpty() )
            {
                final List<String> fileSetIncludes = fileSet.getIncludes();
                includes = fileSetIncludes.toArray( new String[fileSetIncludes.size()] );
            }
            else
            {
                includes = DEFAULT_INCLUDES;
            }
            scanner.setIncludes( includes );

            if ( fileSet.getExcludes() != null && !fileSet.getExcludes().isEmpty() )
            {
                final List<String> fileSetExcludes = fileSet.getExcludes();
                scanner.setExcludes( fileSetExcludes.toArray( new String[fileSetExcludes.size()] ) );
            }

            scanner.addDefaultExcludes();

            scanner.scan();

            for ( String filePath : scanner.getIncludedFiles() )
            {
                filesToProcess.add( new ChecksumFile( (new File( fileSetDirectory ) ).getPath(), new File( fileSetDirectory, filePath ) ) );
            }
            }

        return filesToProcess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isIndividualFiles()
    {
        return individualFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getIndividualFilesOutputDirectory()
    {
        return individualFilesOutputDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCsvSummary()
    {
        return csvSummary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCsvSummaryFile()
    {
        return csvSummaryFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isXmlSummary()
    {
        return xmlSummary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getXmlSummaryFile()
    {
        return xmlSummaryFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isShasumSummary()
    {
        return shasumSummary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getShasumSummaryFile()
    {
        return shasumSummaryFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFailIfNoFiles(){
        return failIfNoFiles;
    }
}
