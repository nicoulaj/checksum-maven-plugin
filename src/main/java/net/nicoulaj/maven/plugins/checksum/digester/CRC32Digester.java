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

import org.codehaus.plexus.digest.Digester;
import org.codehaus.plexus.digest.DigesterException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * TODO.
 *
 * @author Julien Nicoulaud <julien.nicoulaud@gmail.com>
 * @since 0.1
 */
public class CRC32Digester implements Digester
{
    /**
     * TODO.
     *
     * @return TODO
     */
    public String getAlgorithm()
    {
        return "CRC32";
    }

    /**
     * TODO.
     *
     * @return TODO
     */
    public String getFilenameExtension()
    {
        return ".crc32";
    }

    /**
     * TODO.
     *
     * @param file TODO
     * @return TODO
     * @throws DigesterException TODO
     */
    public String calc( File file ) throws DigesterException
    {
        CheckedInputStream cis;
        try
        {
            cis = new CheckedInputStream( new FileInputStream( file ), new CRC32() );
        }
        catch ( FileNotFoundException e )
        {
            throw new DigesterException( e );
        }

        byte[] buf = new byte[128];
        try
        {
            while ( cis.read( buf ) >= 0 )
            {
                continue;
            }
        }
        catch ( IOException e )
        {
            throw new DigesterException( e );
        }

        return Long.toString( cis.getChecksum().getValue() );
    }

    /**
     * TODO.
     *
     * @param file     TODO
     * @param checksum TODO
     * @throws DigesterException TODO
     */
    public void verify( File file, String checksum ) throws DigesterException
    {
        throw new DigesterException( "Not implemented." );
    }
}
