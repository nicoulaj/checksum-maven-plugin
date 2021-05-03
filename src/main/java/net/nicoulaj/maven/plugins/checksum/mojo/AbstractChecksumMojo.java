/*
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2021 checksum-maven-plugin contributors
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

import net.nicoulaj.maven.plugins.checksum.Constants;
import net.nicoulaj.maven.plugins.checksum.artifacts.ArtifactAttacher;
import net.nicoulaj.maven.plugins.checksum.artifacts.ArtifactListener;
import net.nicoulaj.maven.plugins.checksum.execution.Execution;
import net.nicoulaj.maven.plugins.checksum.execution.ExecutionException;
import net.nicoulaj.maven.plugins.checksum.execution.FailOnErrorExecution;
import net.nicoulaj.maven.plugins.checksum.execution.NeverFailExecution;
import net.nicoulaj.maven.plugins.checksum.execution.target.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.shared.utils.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Base class for {@code checksum-maven-plugin} mojos.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.1
 */
abstract class AbstractChecksumMojo
    extends AbstractMojo
{

    /**
     * Fail if no files no process.
     */
    private boolean defaultFailIfNoFiles;

    /**
     * Fail if no files no process.
     */
    private final boolean failIfNoAlgorithms;

    /**
     * Fail if no files no process.
     */
    private final boolean failIfNoTargets;

    /**
     * The Maven project.
     *
     * @since 1.0
     */
    @Parameter( property = "project", required = true, readonly = true )
    protected MavenProject project;

    /**
     * The Maven Project Helper.
     *
     * Used to attach checksums as build artifacts.
     *
     * @since 1.3
     */
    @Component
    protected MavenProjectHelper projectHelper;

    /**
     * The list of checksum algorithms used.
     *
     * <p>
     * Default value is MD5 and SHA-1.
     *
     * <p>
     * Allowed values are Cksum, CRC32, BLAKE2B-160, BLAKE2B-256, BLAKE2B-384,
     * BLAKE2B-512, GOST3411, GOST3411-2012-256, GOST3411-2012-512, KECCAK-224, KECCAK-256, KECCAK-288, KECCAK-384,
     * KECCAK-512, MD2, MD4, MD5, RIPEMD128, RIPEMD160, RIPEMD256, RIPEMD320, SHA, SHA-1, SHA-224, SHA-256, SHA3-224,
     * SHA3-256, SHA3-384, SHA3-512, SHA-384, SHA-512, SHA-512/224, SHA-512/256, SKEIN-1024-1024, SKEIN-1024-384,
     * SKEIN-1024-512, SKEIN-256-128, SKEIN-256-160, SKEIN-256-224, SKEIN-256-256, SKEIN-512-128, SKEIN-512-160,
     * SKEIN-512-224, SKEIN-512-256, SKEIN-512-384, SKEIN-512-512, SM3, TIGER and WHIRLPOOL.
     *
     * <p>
     * Use the following syntax:
     * <pre>&lt;algorithms&gt;
     *   &lt;algorithm&gt;MD5&lt;/algorithm&gt;
     *   &lt;algorithm&gt;SHA-1&lt;/algorithm&gt;
     * &lt;/algorithms&gt;</pre>
     *
     * @since 1.0
     */
    @Parameter
    protected List<String> algorithms = Arrays.asList( Constants.DEFAULT_EXECUTION_ALGORITHMS );

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
     * Should the checksums be attached as build artifacts.
     *
     * @since 1.3
     */
    @Parameter( property = "attachChecksums", defaultValue = "false")
    protected boolean attachChecksums;

    /**
     * Indicates whether the build will print checksums in the build log.
     *
     * @since 1.0
     */
    @Parameter( defaultValue = "false" )
    protected boolean quiet;

    /**
     * Indicates whether the build will output relative path information as well.
     *
     * @since 1.3
     */
    @Parameter( defaultValue = "false" )
    protected boolean includeRelativePath;

    /**
     * Sub path to use as the root of the relative path when including relative path in xml/csv files
     *
     * @since 1.3
     */
    @Parameter( defaultValue = "" )
    protected String relativeSubPath = "";

    /**
     * Constructor.
     *
     * @param failIfNoFiles fail if no files to process
     * @param failIfNoAlgorithms fail if no algorithms to process
     * @param failIfNoTargets fail if no targets to process
     */
    AbstractChecksumMojo(boolean failIfNoFiles, boolean failIfNoAlgorithms, boolean failIfNoTargets) {
        this.defaultFailIfNoFiles = failIfNoFiles;
        this.failIfNoAlgorithms = failIfNoAlgorithms;
        this.failIfNoTargets = failIfNoTargets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
        throws MojoFailureException
    {
        // Prepare an execution.
        Execution execution = ( failOnError ) ? new FailOnErrorExecution() : new NeverFailExecution( getLog() );
        execution.setAlgorithms( algorithms );
        execution.setFiles( getFilesToProcess() );
        execution.setFailIfNoFiles(isFailIfNoFiles());
        execution.setFailIfNoAlgorithms(failIfNoAlgorithms);
        execution.setFailIfNoTargets(failIfNoTargets);
        if ( !quiet )
        {
            execution.addTarget( new MavenLogTarget( getLog() ) );
        }
        if ( includeRelativePath )
        {
                execution.setSubPath( relativeSubPath );
        } else {
                execution.setSubPath( null );
        }
        if ( isIndividualFiles() )
        {
            File outputDirectory = null;
            if ( StringUtils.isNotEmpty( getIndividualFilesOutputDirectory() ) )
            {
                outputDirectory = FileUtils.resolveFile( new File( project.getBuild().getDirectory() ),
                                                         getIndividualFilesOutputDirectory() );
            }
            execution.addTarget( new OneHashPerFileTarget( encoding, outputDirectory, createArtifactListeners(), isAppendFilename()) );
        }
        if ( isCsvSummary() )
        {
            execution.addTarget( new CsvSummaryFileTarget(
                FileUtils.resolveFile( new File( project.getBuild().getDirectory() ), getCsvSummaryFile() ),
                encoding, createArtifactListeners()) );
        }
        if ( isXmlSummary() )
        {
            execution.addTarget( new XmlSummaryFileTarget(
                FileUtils.resolveFile( new File( project.getBuild().getDirectory() ), getXmlSummaryFile() ),
                encoding, createArtifactListeners()) );
        }
        if ( isShasumSummary() )
        {
            execution.addTarget( new ShasumSummaryFileTarget(
                FileUtils.resolveFile( new File( project.getBuild().getDirectory() ), getShasumSummaryFile() ), encoding, createArtifactListeners()) );
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

    private Iterable<? extends ArtifactListener> createArtifactListeners() {
        if (!attachChecksums) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new ArtifactAttacher(project, projectHelper));
    }

    /**
     * Build the list of files from which digests should be generated.
     *
     * @return the list of files that should be processed.
     */
    protected abstract List<ChecksumFile> getFilesToProcess();

    protected abstract boolean isIndividualFiles();

    protected boolean isAppendFilename(){
        return false;
    }

    protected abstract String getIndividualFilesOutputDirectory();

    protected abstract boolean isCsvSummary();

    protected abstract String getCsvSummaryFile();

    protected abstract boolean isXmlSummary();

    protected abstract String getXmlSummaryFile();

    protected abstract boolean isShasumSummary();

    protected abstract String getShasumSummaryFile();

    protected boolean isFailIfNoFiles(){
        return defaultFailIfNoFiles;
    }
}
