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

import net.nicoulaj.maven.plugins.checksum.Constants;
import net.nicoulaj.maven.plugins.checksum.execution.Execution;
import net.nicoulaj.maven.plugins.checksum.execution.ExecutionException;
import net.nicoulaj.maven.plugins.checksum.execution.FailOnErrorExecution;
import net.nicoulaj.maven.plugins.checksum.execution.NeverFailExecution;
import net.nicoulaj.maven.plugins.checksum.execution.target.CsvSummaryFileTarget;
import net.nicoulaj.maven.plugins.checksum.execution.target.MavenLogTarget;
import net.nicoulaj.maven.plugins.checksum.execution.target.OneHashPerFileTarget;
import net.nicoulaj.maven.plugins.checksum.execution.target.XmlSummaryFileTarget;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Compute specified files checksum digests and store them in individual files and/or a summary file.
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
    extends AbstractMojo
{
    /**
     * The mojo name.
     */
    public static final String NAME = "files";

    /**
     * The Maven project.
     *
     * @since 1.0
     */
    @Parameter( property = "project", required = true, readonly = true )
    protected MavenProject project;

    /**
     * The list of checksum algorithms used.
     * <p/>
     * <p>Default value is MD5 and SHA-1.<br/>Allowed values are CRC32, MD2, MD4, MD5, SHA-1, SHA-224, SHA-256, SHA-384,
     * SHA-512, RIPEMD128, RIPEMD160, RIPEMD256, RIPEMD320, GOST3411 and Tiger.</p>
     * <p/>
     * <p> Use the following syntax:
     * <pre>&lt;algorithms&gt;
     *   &lt;algorithm&gt;MD5&lt;algorithm&gt;
     *   &lt;algorithm&gt;SHA-1&lt;algorithm&gt;
     * &lt;/algorithms&gt;</pre>
     * </p>
     *
     * @since 1.0
     */
    @Parameter
    protected List<String> algorithms = Arrays.asList( Constants.DEFAULT_EXECUTION_ALGORITHMS );

    /**
     * The list of files to process.
     *
     * @since 1.0
     */
    @Parameter( required = true )
    protected List<File> files = new LinkedList<File>();

    /**
     * Indicates whether the build will fail if there are errors.
     *
     * @since 1.0
     */
    @Parameter( defaultValue = "true" )
    protected boolean failOnError;

    /**
     * Encoding to use for generated files.
     *
     * @since 1.0
     */
    @Parameter( property = "encoding", defaultValue = "${project.build.sourceEncoding}" )
    protected String encoding = Constants.DEFAULT_ENCODING;

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
     * Indicates whether the build will print checksums in the build log.
     *
     * @since 1.0
     */
    @Parameter( defaultValue = "false" )
    protected boolean quiet;

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
    @Parameter( defaultValue = "files-checksums.csv" )
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
    @Parameter( defaultValue = "files-checksums.xml" )
    protected String xmlSummaryFile;

    /**
     * {@inheritDoc}
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        // Prepare an execution.
        Execution execution = ( failOnError ) ? new FailOnErrorExecution() : new NeverFailExecution( getLog() );
        execution.setAlgorithms( algorithms );
        execution.setFiles( files );
        if ( !quiet )
        {
            execution.addTarget( new MavenLogTarget( getLog() ) );
        }
        if ( individualFiles )
        {
            File outputDirectory = null;
            if ( StringUtils.isNotEmpty( individualFilesOutputDirectory ) )
            {
                outputDirectory = FileUtils.resolveFile( new File( project.getBuild().getDirectory() ),
                                                         individualFilesOutputDirectory );
            }
            execution.addTarget( new OneHashPerFileTarget( encoding, outputDirectory ) );
        }
        if ( csvSummary )
        {
            execution.addTarget( new CsvSummaryFileTarget(
                FileUtils.resolveFile( new File( project.getBuild().getDirectory() ), csvSummaryFile ), encoding ) );
        }
        if ( xmlSummary )
        {
            execution.addTarget( new XmlSummaryFileTarget(
                FileUtils.resolveFile( new File( project.getBuild().getDirectory() ), xmlSummaryFile ), encoding ) );
        }

        // Run the execution.
        try
        {
            execution.run();
        }
        catch ( ExecutionException e )
        {
            getLog().error( e.getMessage() );
            throw new MojoFailureException( e.getMessage() );
        }
    }
}
