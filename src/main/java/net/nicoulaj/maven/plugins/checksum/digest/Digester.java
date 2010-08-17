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
package net.nicoulaj.maven.plugins.checksum.digest;

import java.io.File;

/**
 * TODO.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.0
 */
public interface Digester
{
    /**
     * Get the algorithm used for the checksum.
     *
     * @return the algorithm
     */
    String getAlgorithm();

    /**
     * The filename extension for this digester.
     *
     * @return the filename extension.
     */
    String getFilenameExtension();

    /**
     * Calculate a checksum for a file.
     *
     * @param file the file to calculate the checksum for
     * @return the current checksum.
     * @throws DigesterException if there was a problem computing the hashcode.
     */
    String calc( File file ) throws DigesterException;

    /**
     * Verify that a checksum is correct.
     *
     * @param file     the file to compute the checksum for
     * @param checksum the checksum to compare to
     * @throws DigesterException if there was a problem computing the hashcode.
     */
    void verify( File file, String checksum ) throws DigesterException;
}
