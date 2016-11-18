/*-
 * ====================================================================
 * checksum-maven-plugin
 * ====================================================================
 * Copyright (C) 2010 - 2016 Julien Nicoulaud <julien.nicoulaud@gmail.com>
 * ====================================================================
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
 * ====================================================================
 */

package net.nicoulaj.maven.plugins.checksum.execution;

import net.nicoulaj.maven.plugins.checksum.digest.DigesterException;
import net.nicoulaj.maven.plugins.checksum.digest.DigesterFactory;
import net.nicoulaj.maven.plugins.checksum.digest.FileDigester;
import net.nicoulaj.maven.plugins.checksum.execution.target.ExecutionTarget;
import net.nicoulaj.maven.plugins.checksum.execution.target.ExecutionTargetCloseException;
import net.nicoulaj.maven.plugins.checksum.execution.target.ExecutionTargetInitializationException;
import net.nicoulaj.maven.plugins.checksum.execution.target.ExecutionTargetWriteException;
import net.nicoulaj.maven.plugins.checksum.mojo.ChecksumFile;

import java.security.NoSuchAlgorithmException;

/**
 * An implementation of {@link net.nicoulaj.maven.plugins.checksum.execution.Execution} that throws exceptions when it
 * encounters errors.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @see net.nicoulaj.maven.plugins.checksum.execution.NeverFailExecution
 * @since 1.0
 */
public class FailOnErrorExecution
    extends AbstractExecution
{
    /**
     * {@inheritDoc}
     */
    public void run()
        throws ExecutionException
    {
        // Check parameters are initialized.
        checkParameters();

        // Initialize targets.
        for ( ExecutionTarget target : getTargets() )
        {
            try
            {
                target.init();
            }
            catch ( ExecutionTargetInitializationException e )
            {
                throw new ExecutionException( e.getMessage() );
            }
        }

        // Process files.
        for ( ChecksumFile file : files )
        {
            for ( String algorithm : getAlgorithms() )
            {
                try
                {
                    // Calculate the hash for the file/algo
                    FileDigester digester = DigesterFactory.getInstance().getFileDigester( algorithm );
                    String hash = digester.calculate( file.getFile() );

                    // Write it to each target defined
                    for ( ExecutionTarget target : getTargets() )
                    {
                        try
                        {
                            target.write( hash, file, algorithm );
                        }
                        catch ( ExecutionTargetWriteException e )
                        {
                            throw new ExecutionException( e.getMessage() );
                        }
                    }
                }
                catch ( NoSuchAlgorithmException e )
                {
                    throw new ExecutionException( "Unsupported algorithm " + algorithm + "." );
                }
                catch ( DigesterException e )
                {
                    throw new ExecutionException(
                        "Unable to calculate " + algorithm + " hash for " + file.getFile().getName() + ": " + e.getMessage() );
                }
            }
        }

        // Close targets.
        for ( ExecutionTarget target : getTargets() )
        {
            try
            {
                target.close( subPath );
            }
            catch ( ExecutionTargetCloseException e )
            {
                throw new ExecutionException( e.getMessage() );
            }
        }
    }
}
