/*
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2018 checksum-maven-plugin contributors
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
package net.nicoulaj.maven.plugins.checksum.execution;

import net.nicoulaj.maven.plugins.checksum.execution.target.ExecutionTarget;
import net.nicoulaj.maven.plugins.checksum.mojo.ChecksumFile;

import java.util.LinkedList;
import java.util.List;

/**
 * Base class for {@link Execution} implementations.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @see net.nicoulaj.maven.plugins.checksum.execution.Execution
 * @since 1.0
 */
public abstract class AbstractExecution
    implements Execution
{
    /**
     * The list of files used for the execution.
     */
    protected List<ChecksumFile> files;

    /**
     * Part of relative path to exclude from file path
     */
	protected String subPath;

    /**
     * The list of algorithms used for execution.
     */
    protected List<String> algorithms;

    /**
     * The list of targets used for the execution.
     */
    protected List<ExecutionTarget> targets;

    /**
     * Fail if no files no process.
     */
    protected boolean failIfNoFiles = true;

    /**
     * Fail if no files no process.
     */
    protected boolean failIfNoAlgorithms = true;

    /**
     * Fail if no files no process.
     */
    protected boolean failIfNoTargets = true;

    /**
     * {@inheritDoc}
     */
    public List<ChecksumFile> getFiles()
    {
        if ( files == null )
        {
            files = new LinkedList<>();
        }
        return files;
    }

    /**
     * {@inheritDoc}
     */
    public void setFiles( List<ChecksumFile> files )
    {
        this.files = files;
    }

    /**
     * {@inheritDoc}
     */
    public String getSubPath()
    {
            return subPath;
    }

    /**
     * {@inheritDoc}
     */
    public void setSubPath( String subPath )
    {
            this.subPath = subPath;
    }

    /**
     * {@inheritDoc}
     */
    public void addFile( ChecksumFile file )
    {
        getFiles().add( file );
    }

    /**
     * {@inheritDoc}
     */
    public void removeFile( ChecksumFile file )
    {
        if ( files != null )
        {
            files.remove( file );
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getAlgorithms()
    {
        if ( algorithms == null )
        {
            algorithms = new LinkedList<>();
        }
        return algorithms;
    }

    /**
     * {@inheritDoc}
     */
    public void setAlgorithms( List<String> algorithms )
    {
        this.algorithms = algorithms;
    }

    /**
     * {@inheritDoc}
     */
    public void addAlgorithm( String algorithm )
    {
        getAlgorithms().add( algorithm );
    }

    /**
     * {@inheritDoc}
     */
    public void removeAlgorithm( String algorithm )
    {
        if ( algorithms != null )
        {
            algorithms.remove( algorithm );
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ExecutionTarget> getTargets()
    {
        if ( targets == null )
        {
            targets = new LinkedList<>();
        }
        return targets;
    }

    /**
     * {@inheritDoc}
     */
    public void addTarget( ExecutionTarget target )
    {
        getTargets().add( target );
    }

    /**
     * {@inheritDoc}
     */
    public void removeTarget( ExecutionTarget target )
    {
        if ( targets != null )
        {
            targets.remove( target );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setTargets( List<ExecutionTarget> targets )
    {
        this.targets = targets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFailIfNoFiles() {
        return failIfNoFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFailIfNoFiles(boolean failIfNoFiles) {
        this.failIfNoFiles = failIfNoFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFailIfNoAlgorithms() {
        return failIfNoAlgorithms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFailIfNoAlgorithms(boolean failIfNoAlgorithms) {
        this.failIfNoAlgorithms = failIfNoAlgorithms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFailIfNoTargets() {
        return failIfNoTargets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFailIfNoTargets(boolean failIfNoTargets) {
        this.failIfNoTargets = failIfNoTargets;
    }

    /**
     * {@inheritDoc}
     */
    public void checkParameters()
        throws ExecutionException
    {
        if ( isFailIfNoFiles() && ( files == null || files.isEmpty() ) )
        {
            throw new ExecutionException( "No file to process." );
        }
        if ( isFailIfNoAlgorithms() && ( algorithms == null || algorithms.isEmpty() ) )
        {
            throw new ExecutionException( "No checksum algorithm defined." );
        }
        if ( isFailIfNoTargets() && ( targets == null || targets.isEmpty() ) )
        {
            throw new ExecutionException( "No output target defined." );
        }
    }
}
