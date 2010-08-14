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

import net.nicoulaj.maven.plugins.checksum.execution.target.ExecutionTarget;

import java.io.File;
import java.util.List;

/**
 * TODO add Javadoc comment.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.1
 */
public interface Execution
{
    /**
     * TODO add Javadoc comment.
     *
     * @return TODO add Javadoc comment.
     */
    List<File> getFiles();

    /**
     * TODO add Javadoc comment.
     *
     * @param files TODO add Javadoc comment.
     */
    void setFiles( List<File> files );

    /**
     * TODO add Javadoc comment.
     *
     * @param file TODO add Javadoc comment.
     */
    void addFile( File file );

    /**
     * TODO add Javadoc comment.
     *
     * @param file TODO add Javadoc comment.
     */
    void removeFile( File file );

    /**
     * TODO add Javadoc comment.
     *
     * @return TODO add Javadoc comment.
     */
    List<String> getAlgorithms();

    /**
     * TODO add Javadoc comment.
     *
     * @param algorithms TODO add Javadoc comment.
     */
    void setAlgorithms( List<String> algorithms );

    /**
     * TODO add Javadoc comment.
     *
     * @param algorithm TODO add Javadoc comment.
     */
    void addAlgorithm( String algorithm );

    /**
     * TODO add Javadoc comment.
     *
     * @param algorithm TODO add Javadoc comment.
     */
    void removeAlgorithm( String algorithm );

    /**
     * TODO add Javadoc comment.
     *
     * @return TODO add Javadoc comment.
     */
    List<ExecutionTarget> getTargets();

    /**
     * TODO add Javadoc comment.
     *
     * @param targets TODO add Javadoc comment.
     */
    void setTargets( List<ExecutionTarget> targets );

    /**
     * TODO add Javadoc comment.
     *
     * @param target TODO add Javadoc comment.
     */
    void addTarget( ExecutionTarget target );

    /**
     * TODO add Javadoc comment.
     *
     * @param target TODO add Javadoc comment.
     */
    void removeTarget( ExecutionTarget target );

    /**
     * Run the execution.
     *
     * @throws ExecutionException if an error happens while running the execution.
     */
    void run() throws ExecutionException;
}
