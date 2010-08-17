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
public abstract class AbstractDigester implements Digester
{
    protected final String algorithm;

    protected AbstractDigester( String algorithm )
    {
        this.algorithm = algorithm;
    }

    public String getAlgorithm()
    {
        return algorithm;
    }

    public String getFilenameExtension()
    {
        return "." + algorithm.toLowerCase().replace( "-", "" );
    }

    /**
     * {@inheritDoc}
     */
    public void verify( File file, String checksum ) throws DigesterException
    {
        if ( !checksum.equalsIgnoreCase( calc( file ) ) )
        {
            throw new DigesterException( "Checksum failed" );
        }
    }
}
