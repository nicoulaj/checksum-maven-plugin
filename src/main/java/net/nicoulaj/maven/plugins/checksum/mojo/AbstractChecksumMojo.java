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

import net.nicoulaj.maven.plugins.checksum.digester.DigesterFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.digest.Digester;
import org.codehaus.plexus.digest.DigesterException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO
 *
 * @author Julien Nicoulaud <julien.nicoulaud@gmail.com>
 * @since 0.1
 */
public abstract class AbstractChecksumMojo extends AbstractMojo
{
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    
    /**
     * TODO
     */
    protected List<File> filesToProcess = new LinkedList<File>();

    /**
     * TODO
     *
     * @parameter
     */
    protected String[] algorithms = new String[]{"MD5", "SHA-1", "CRC32"};

    /**
     * TODO
     */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        prepareExecution();

        if (filesToProcess.isEmpty())
        {
            getLog().warn("No file to generate checksums for.");
        }
        else
        {
            for (File file : filesToProcess)
            {
                getLog().debug("Processing file " + file.getName() + ".");

                for (String algorithm : algorithms)
                {
                    getLog().debug("Computing checksum for " + algorithm + " algorithm.");

                    Digester digester;
                    try
                    {
                        digester = DigesterFactory.getInstance().getDigester(algorithm);
                    }
                    catch (NoSuchAlgorithmException e)
                    {
                        throw new MojoExecutionException(e.getMessage());
                    }
                    try
                    {
                        // String targetFile;
                        // if (file.getPath().startsWith(project.getBuild().getDirectory())) {
                        //     targetFile = file.getPath() + digester.getFilenameExtension();
                        // } else {
                        //     targetFile = project.getBuild().getDirectory() + File.separator + file.getName() + digester.getFilenameExtension();
                        //     FileUtils.mkdir(project.getBuild().getDirectory());
                        // }
                        FileUtils.fileWrite(file.getPath() + digester.getFilenameExtension(), digester.calc(file));
                    }
                    catch (IOException e)
                    {
                        throw new MojoExecutionException(e.getMessage());
                    }
                    catch (DigesterException e)
                    {
                        throw new MojoExecutionException(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * TODO
     */
    abstract protected void prepareExecution() throws MojoExecutionException, MojoFailureException;
}
