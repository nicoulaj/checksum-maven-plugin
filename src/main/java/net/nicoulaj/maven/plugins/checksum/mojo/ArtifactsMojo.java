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
package net.nicoulaj.maven.plugins.checksum.mojo;

import net.nicoulaj.maven.plugins.checksum.execution.Execution;
import net.nicoulaj.maven.plugins.checksum.execution.ExecutionException;
import net.nicoulaj.maven.plugins.checksum.execution.FailOnErrorExecution;
import net.nicoulaj.maven.plugins.checksum.execution.NeverFailExecution;
import net.nicoulaj.maven.plugins.checksum.execution.target.CsvSummaryFileTarget;
import net.nicoulaj.maven.plugins.checksum.execution.target.MavenLogTarget;
import net.nicoulaj.maven.plugins.checksum.execution.target.OneHashPerFileTarget;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Compute project artifacts checksum digests and store them in individual files and/or a summary file.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @goal artifacts
 * @phase verify
 * @requiresProject true
 * @inheritByDefault false
 * @since 0.1
 */
public class ArtifactsMojo extends AbstractMojo
{
    /**
     * The Maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @since 0.1
     */
    protected MavenProject project;

    /**
     * The list of checksum algorithms used.
     *
     * <p>Default value is MD5 and SHA-1.<br/>Allowed values are CRC32, MD2, MD5, SHA-1, SHA-256, SHA-384 and
     * SHA-512.</p>
     *
     * <p> Use the following syntax:
     * <pre>&lt;algorithms&gt;
     *   &lt;algorithm&gt;MD5&lt;algorithm&gt;
     *   &lt;algorithm&gt;SHA-1&lt;algorithm&gt;
     * &lt;/algorithms&gt;</pre>
     * </p>
     *
     * @parameter
     * @since 0.1
     */
    protected List<String> algorithms = Arrays.asList( new String[]{"MD5", "SHA-1"} );

    /**
     * Indicates whether the build will fail if there are errors.
     *
     * @parameter default-value="true"
     * @since 0.1
     */
    protected boolean failOnError;

    /**
     * Indicates whether the build will store checksums in separate files (one file per algorithm per artifact).
     *
     * @parameter default-value="true"
     * @since 0.1
     */
    protected boolean individualFiles;

    /**
     * Indicates whether the build will print checksums in the build log.
     *
     * @parameter default-value="false"
     * @since 0.1
     */
    protected boolean quiet;

    /**
     * Indicates whether the build will store checksums to a single summary file.
     *
     * @parameter default-value="false"
     * @since 0.1
     */
    protected boolean summaryFile;

    /**
     * The name of the summary file created if the option is activated.
     *
     * @parameter default-value="artifacts-checksums.csv"
     * @see #summaryFile
     * @since 0.1
     */
    protected String summaryFileName;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // Prepare an execution.
        Execution execution = ( failOnError ) ? new FailOnErrorExecution() : new NeverFailExecution( getLog() );
        execution.setAlgorithms( algorithms );
        execution.setFiles( getFilesToProcess() );
        if ( !quiet )
        {
            execution.addTarget( new MavenLogTarget( getLog() ) );
        }
        if ( individualFiles )
        {
            execution.addTarget( new OneHashPerFileTarget() );
        }
        if ( summaryFile )
        {
            execution.addTarget( new CsvSummaryFileTarget( project.getBuild().getDirectory() +
                                                           File.separator + summaryFileName ) );
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

    /**
     * Build the list of files from which digests should be generated.
     *
     * <p>The list is composed of the project main and attached artifacts.</p>
     *
     * @return the list of files that should be processed.
     * @see #hasValidFile(org.apache.maven.artifact.Artifact)
     */
    protected List<File> getFilesToProcess()
    {
        List<File> files = new LinkedList<File>();

        // Add project main artifact.
        if ( hasValidFile( project.getArtifact() ) )
        {
            files.add( project.getArtifact().getFile() );
        }

        // Add projects attached.
        if ( project.getAttachedArtifacts() != null )
        {
            for ( Artifact artifact : ( List<Artifact> ) project.getAttachedArtifacts() )
            {
                if ( hasValidFile( artifact ) )
                {
                    files.add( artifact.getFile() );
                }
            }
        }
        return files;
    }

    /**
     * Decide wether the artifact file should be processed.
     *
     * <p>Excludes the project POM file and any file outside the build directory, because this could lead to writing
     * files on the user local repository for example.</p>
     *
     * @param artifact the artifact to check.
     * @return true if the artifact should be included in the files to process.
     */
    protected boolean hasValidFile( Artifact artifact )
    {
        // Make sure the file exists.
        boolean hasValidFile = artifact != null && artifact.getFile() != null && artifact.getFile().exists();

        // Exclude project POM file.
        hasValidFile = hasValidFile && !artifact.getFile().getPath().equals( project.getFile().getPath() );

        // Exclude files outside of build directory.
        hasValidFile = hasValidFile && artifact.getFile().getPath().startsWith( project.getBuild().getDirectory() );

        return hasValidFile;
    }
}
