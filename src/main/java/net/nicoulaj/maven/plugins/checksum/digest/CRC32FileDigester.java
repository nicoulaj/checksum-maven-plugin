/*
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2017 checksum-maven-plugin contributors
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
package net.nicoulaj.maven.plugins.checksum.digest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Implementation of {@link FileDigester} for the CRC32 algorithm.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @see FileDigester
 * @since 1.0
 */
public class CRC32FileDigester
    extends AbstractFileDigester {

    /**
     * The identifier of the algorithm supported by this implementation.
     */
    public static final String ALGORITHM = "CRC32";

    /**
     * Build a new instance of {@link CRC32FileDigester}.
     */
    public CRC32FileDigester() {
        super(ALGORITHM);
    }

    /**
     * {@inheritDoc}
     */
    public String calculate(File file, String salt)
        throws DigesterException {
        CheckedInputStream cis;
        try {
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
            if(!(null == salt)){
                if(!salt.isEmpty()){
                    bytes = ArrayUtils.addAll(bytes, salt.getBytes());
                }
            }
            InputStream bais = new ByteArrayInputStream(bytes);
            cis = new CheckedInputStream(bais, new CRC32());
        } catch (IOException e) {
            throw new DigesterException("Unable to read " + file.getPath() + ": " + e.getMessage());
        }

        byte[] buf = new byte[STREAMING_BUFFER_SIZE];
        try {
            while (cis.read(buf) >= 0) {
                continue;
            }
        } catch (IOException e) {
            throw new DigesterException(
                "Unable to calculate the " + getAlgorithm() + " hashcode for " + file.getPath() + ": "
                    + e.getMessage());
        }

        return Long.toString(cis.getChecksum().getValue());
    }
}
