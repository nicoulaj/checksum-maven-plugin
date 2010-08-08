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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.util.List;

/**
 * TODO.
 *
 * @author Julien Nicoulaud <julien.nicoulaud@gmail.com>
 * @goal artifacts
 * @phase install
 * @inheritByDefault false
 * @requiresProject true
 * @since 0.1
 */
public class ArtifactsMojo extends AbstractChecksumMojo
{
    /**
     * {@inheritDoc}
     *
     * @throws MojoExecutionException TODO
     * @throws MojoFailureException   TODO
     */
    protected void prepareExecution() throws MojoExecutionException, MojoFailureException
    {
        // Add project main artifact to the files to be processed.
        if ( project.getArtifact() != null &&
             project.getArtifact().getFile() != null &&
             project.getArtifact().getFile().getPath().startsWith( project.getBuild().getDirectory() ) &&
             !project.getArtifact().getFile().getPath().equals( project.getFile().getPath() ) )
        {
            filesToProcess.add( project.getArtifact().getFile() );
        }

        // Add projects attached artifacts to the files to be processed.
        if ( project.getAttachedArtifacts() != null )
        {
            for ( Artifact artifact : (List<Artifact>) project.getAttachedArtifacts() )
            {
                if ( artifact != null && artifact.getFile() != null )
                {
                    filesToProcess.add( artifact.getFile() );
                }
            }
        }
    }
}
