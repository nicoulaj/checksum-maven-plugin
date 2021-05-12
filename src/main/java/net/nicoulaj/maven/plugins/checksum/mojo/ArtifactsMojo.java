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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.artifact.filter.collection.ArtifactIdFilter;
import org.apache.maven.shared.artifact.filter.collection.ClassifierFilter;
import org.apache.maven.shared.artifact.filter.collection.GroupIdFilter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Compute project artifact's checksum digests and store them in individual files and/or a summary file.
 * Optionally attaches the checksum files as additional project artifacts.
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.0
 */
@Mojo(
    name = ArtifactsMojo.NAME,
    defaultPhase = LifecyclePhase.VERIFY,
    requiresProject = true,
    inheritByDefault = false,
    threadSafe = true )
public class ArtifactsMojo
    extends AbstractChecksumMojo
{

    /**
     * The mojo name.
     */
    public static final String NAME = "artifacts";

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
    @Parameter( defaultValue = "false" )
    protected boolean csvSummary;

    /**
     * The name of the summary file created if the option is activated.
     *
     * @see #csvSummary
     * @since 1.0
     */
    @Parameter( defaultValue = "artifacts-checksums.csv" )
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
    @Parameter( defaultValue = "artifacts-checksums.xml" )
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
    @Parameter( defaultValue = "artifacts-checksums.sha" )
    protected String shasumSummaryFile;

    /**
     * Append the artifact filename in the generated checksum file.
     * This parameter require the parameter individualFiles be equal to true.
     *
     * @see #individualFiles
     * @since 1.4
     */
    @Parameter( defaultValue = "false" )
    protected boolean appendFilename;

    /**
     * Whether to skip checksum generation for the project's main artifact.
     */
    @Parameter( defaultValue = "false" )
    protected boolean excludeMainArtifact;

    /**
     * Comma-separated list of classifiers. For all secondary project artifacts having any of the given classifiers checksums are generated.
     * If not set all secondary project artifacts are considered.
     */
    @Parameter
    protected String includeClassifiers;

    /**
     * Comma-separated list of classifiers. For all secondary project artifacts having any of the given classifiers checksums are not generated.
     * Takes precedence over {@link #includeClassifiers}.
     */
    @Parameter
    protected String excludeClassifiers;

    /**
     * Comma-separated list of includes. For all secondary project artifacts matching any of the given includes checksums are generated.
     * If not set all secondary project artifacts are considered.
     *
     * @since 1.11
     */
    @Parameter
    protected String groupdIDincludes;

    /**
     * Comma-separated list of excludes. For all secondary project artifacts matching any of the given excludes checksums are not generated.
     * Takes precedence over {@link #groupdIDincludes}.
     *
     * @since 1.11
     */
    @Parameter
    protected String groupdIDexcludes;

    /**
     * Comma-separated list of includes. For all secondary project artifacts matching any of the given includes checksums are generated.
     * If not set all secondary project artifacts are considered.
     *
     * @since 1.11
     */
    @Parameter
    protected String artifactIDincludes;

    /**
     * Comma-separated list of excludes. For all secondary project artifacts matching any of the given excludes checksums are not generated.
     * Takes precedence over {@link #artifactIDincludes}.
     *
     * @since 1.11
     */
    @Parameter
    protected String artifactIDexcludes;

    /**
     * Constructor.
     */
    public ArtifactsMojo() {
        super(false, true, true);
    }

    /**
     * Build the list of files from which digests should be generated.
     *
     * <p>The list is composed of the project main and attached artifacts.</p>
     *
     * @return the list of files that should be processed.
     * @see #hasValidFile(org.apache.maven.artifact.Artifact)
     */
    @Override
    protected List<ChecksumFile> getFilesToProcess()
    {
        List<ChecksumFile> files = new LinkedList<>();

        // Add project main artifact.
        if ( !excludeMainArtifact && hasValidFile( project.getArtifact() ) )
        {
            files.add( new ChecksumFile( "", project.getArtifact().getFile(), project.getArtifact().getArtifactHandler().getExtension(), null ) );
        }

        // Add projects attached.
        if ( project.getAttachedArtifacts() != null )
        {
            Set<Artifact> filteredArtifacts = new HashSet<>(project.getAttachedArtifacts());
            ClassifierFilter classifierFilter = new ClassifierFilter( includeClassifiers, excludeClassifiers );
            filteredArtifacts = classifierFilter.filter( filteredArtifacts );
            ArtifactIdFilter artifactIDFilter = new ArtifactIdFilter( artifactIDincludes, artifactIDexcludes );
            filteredArtifacts = artifactIDFilter.filter( filteredArtifacts );
            GroupIdFilter groupIdFilter = new GroupIdFilter( groupdIDincludes, groupdIDexcludes );
            filteredArtifacts = groupIdFilter.filter( filteredArtifacts );
            for ( Artifact artifact : filteredArtifacts )
            {
                if ( hasValidFile( artifact ) )
                {
                    files.add( new ChecksumFile( "", artifact.getFile(), artifact.getArtifactHandler().getExtension(), artifact.getClassifier() ) );
                }
            }
        }
        return files;
    }

    /**
     * Decide whether the artifact file should be processed.
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
    protected boolean isAppendFilename(){
        return this.appendFilename;
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


}
