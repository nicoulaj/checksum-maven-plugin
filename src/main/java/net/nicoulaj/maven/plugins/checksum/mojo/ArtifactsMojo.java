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
 * Calculate project artifacts checksums and store them in individual files and/or a summary file.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @goal artifacts
 * @phase verify
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
     */
    protected boolean failOnError;

    /**
     * Indicates whether the build will store checksums in separate files (one file per algorithm per artifact).
     *
     * @parameter default-value="true"
     */
    protected boolean outputToIndividualFiles;

    /**
     * Indicates whether the build will print checksums in the build log.
     *
     * @parameter default-value="true"
     */
    protected boolean outputToLog;

    /**
     * Indicates whether the build will store checksums to a single summary file.
     *
     * @parameter default-value="false"
     */
    protected boolean outputToSummaryFile;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // Prepare an execution
        Execution execution = ( failOnError ) ? new FailOnErrorExecution() : new NeverFailExecution( getLog() );
        execution.setAlgorithms( algorithms );
        execution.setFiles( getFilesToProcess() );
        if ( outputToLog )
        {
            execution.addTarget( new MavenLogTarget( getLog() ) );
        }
        if ( outputToIndividualFiles )
        {
            execution.addTarget( new OneHashPerFileTarget() );
        }
        if ( outputToSummaryFile )
        {
            execution.addTarget( new CsvSummaryFileTarget( project.getBuild().getDirectory() +
                                                           File.separator + "artifacts-checksums.csv" ) );
        }

        // Run the execution
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
     * TODO add Javadoc comment.
     *
     * @return TODO
     */
    protected List<File> getFilesToProcess()
    {
        List<File> files = new LinkedList<File>();

        // Add project main artifact to the files to be processed.
        if ( hasValidFile( project.getArtifact() ) )
        {
            files.add( project.getArtifact().getFile() );
        }

        // Add projects attached artifacts to the files to be processed.
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
     * TODO add Javadoc comment.
     *
     * @param artifact TODO
     * @return TODO
     */

    protected boolean hasValidFile( Artifact artifact )
    {
        return artifact != null &&
               artifact.getFile() != null &&
               !isProjectPomFile( artifact.getFile() ) &&
               isInProjectBuildDirectory( artifact.getFile() );
    }

    /**
     * TODO add Javadoc comment.
     *
     * @param file TODO
     * @return TODO
     */
    protected boolean isProjectPomFile( File file )
    {
        return file.exists() &&
               file.getPath().equals( project.getFile().getPath() );
    }

    /**
     * TODO add Javadoc comment.
     *
     * @param file TODO
     * @return TODO
     */
    protected boolean isInProjectBuildDirectory( File file )
    {
        return file.exists() &&
               file.getPath().startsWith( project.getBuild().getDirectory() );
    }
}
