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
package net.nicoulaj.maven.plugins.checksum.digester;

import org.codehaus.plexus.digest.AbstractDigester;

/**
 * TODO.
 *
 * @author Julien Nicoulaud <julien.nicoulaud@gmail.com>
 * @see <a href="http://download-llnw.oracle.com/javase/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java
 *      Cryptography Architecture API specification and reference</a>
 * @since 0.1
 */
public class Sha384Digester extends AbstractDigester
{
    /**
     * TODO.
     */
    public Sha384Digester()
    {
        super( new StreamingSha384Digester() );
    }

    /**
     * TODO.
     *
     * @return TODO
     */
    public String getFilenameExtension()
    {
        return ".sha384";
    }
}
