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
package net.nicoulaj.maven.plugins.checksum.execution;

/**
 * An exception that occured while running an {@link Execution}.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @see Execution#run()
 * @since 0.1
 */
public class ExecutionException extends Exception
{
    /**
     * Build a new instance of {@link ExecutionException}.
     *
     * @param message the message describing the error.
     */
    public ExecutionException( String message )
    {
        super( message );
    }
}
