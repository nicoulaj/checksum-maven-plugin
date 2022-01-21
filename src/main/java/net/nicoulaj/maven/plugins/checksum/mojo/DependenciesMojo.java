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
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.shared.artifact.filter.collection.ArtifactIdFilter;
import org.apache.maven.shared.artifact.filter.collection.GroupIdFilter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Compute project dependencies checksum digests and store them in a summary file.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.0
 * @version $Id: $Id
 */
@Mojo(
    name = DependenciesMojo.NAME,
    defaultPhase = LifecyclePhase.VERIFY,
    requiresProject = true,
    inheritByDefault = false,
    requiresDependencyResolution = ResolutionScope.RUNTIME,
    threadSafe = true )
public class DependenciesMojo
    extends AbstractChecksumMojo
{
    /**
     * The mojo name.
     */
    public static final String NAME = "dependencies";

    /**
     * Indicates whether the build will store checksums in separate files (one file per algorithm per artifact).
     *
     * @since 1.0
     */
    @Parameter( defaultValue = "false" )
    protected boolean individualFiles;

    /**
     * The directory where output files will be stored. Leave unset to have each file next to the source file.
     *
     * @since 1.0
     */
    @Parameter( defaultValue = "${project.build.directory}" )
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
    @Parameter( defaultValue = "dependencies-checksums.csv" )
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
    @Parameter( defaultValue = "dependencies-checksums.xml" )
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
    @Parameter( defaultValue = "dependencies-checksums.sha" )
    protected String shasumSummaryFile;

    /**
     * The dependency scopes to include.
     *
     * <p>Allowed values are compile, test, runtime, provided and system.<br>All scopes are included by default.
     *
     * <p> Use the following syntax:
     * <pre>&lt;scopes&gt;
     *   &lt;scope&gt;compile&lt;scope&gt;
     *   &lt;scope&gt;runtime&lt;scope&gt;
     * &lt;/scopes&gt;</pre>
     *
     * @since 1.0
     */
    @Parameter
    protected List<String> scopes;

    /**
     * The dependency types to include.
     *
     * <p>All types are included by default.
     *
     * <p> Use the following syntax:
     * <pre>&lt;types&gt;
     *   &lt;type&gt;jar&lt;type&gt;
     *   &lt;type&gt;zip&lt;type&gt;
     * &lt;/types&gt;</pre>
     *
     * @since 1.0
     */
    @Parameter
    protected List<String> types;

    /**
     * Transitive dependencies or only direct dependencies.
     *
     * @since 1.0
     */
    @Parameter( property = "transitive", defaultValue = "false" )
    protected boolean transitive;

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
    public DependenciesMojo() {
        super(false, true, true);
    }

    /**
     * {@inheritDoc}
     *
     * Build the list of files from which digests should be generated.
     *
     * <p>The list is composed of the project dependencies.</p>
     */
    @Override
    protected List<ChecksumFile> getFilesToProcess()
    {
        List<ChecksumFile> files = new LinkedList<>();

        Set<Artifact> artifacts = transitive ?  project.getArtifacts() : project.getDependencyArtifacts();
        ArtifactIdFilter artifactIDFilter = new ArtifactIdFilter( artifactIDincludes, artifactIDexcludes );
        Set<Artifact> filteredArtifacts = artifactIDFilter.filter(artifacts);
        GroupIdFilter groupIdFilter = new GroupIdFilter( groupdIDincludes, groupdIDexcludes );
        filteredArtifacts = groupIdFilter.filter( filteredArtifacts );
        for ( Artifact artifact : filteredArtifacts )
        {
            if ( ( scopes == null || scopes.contains( artifact.getScope() ) ) && ( types == null || types.contains(
                artifact.getType() ) ) &&  artifact.getFile() != null )
            {
                files.add( new ChecksumFile( "", artifact.getFile(), artifact.getType(), artifact.getClassifier() ) );
            }
        }

        return files;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isIndividualFiles()
    {
        return individualFiles;
    }

    /** {@inheritDoc} */
    @Override
    protected String getIndividualFilesOutputDirectory()
    {
        return individualFilesOutputDirectory;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isCsvSummary()
    {
        return csvSummary;
    }

    /** {@inheritDoc} */
    @Override
    protected String getCsvSummaryFile()
    {
        return csvSummaryFile;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isXmlSummary()
    {
        return xmlSummary;
    }

    /** {@inheritDoc} */
    @Override
    protected String getXmlSummaryFile()
    {
        return xmlSummaryFile;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isShasumSummary()
    {
        return shasumSummary;
    }

    /** {@inheritDoc} */
    @Override
    protected String getShasumSummaryFile()
    {
        return shasumSummaryFile;
    }

}
