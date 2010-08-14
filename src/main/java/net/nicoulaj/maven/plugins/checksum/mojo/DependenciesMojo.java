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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * TODO add Javadoc comment.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @goal dependencies
 * @phase verify
 * @inheritByDefault false
 * @requiresProject true
 * @since 0.1
 */
public class DependenciesMojo extends AbstractMojo
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
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
//        for ( Artifact artifact : ( Set<Artifact> ) project.getDependencyArtifacts() )
//        {
//            getLog().info( artifact.getFile().getPath() );
//        }
    }
}
