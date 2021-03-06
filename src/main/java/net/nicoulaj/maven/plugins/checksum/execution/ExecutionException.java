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
package net.nicoulaj.maven.plugins.checksum.execution;

/**
 * Thrown when an error occurs while using an {@link net.nicoulaj.maven.plugins.checksum.execution.Execution}.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @see Execution#run()
 * @since 1.0
 * @version $Id: $Id
 */
public class ExecutionException
    extends Exception
{
    /**
     * Build a new instance of {@link net.nicoulaj.maven.plugins.checksum.execution.ExecutionException}.
     *
     * @param message the message describing the error.
     */
    public ExecutionException( String message )
    {
        super( message );
    }
}
