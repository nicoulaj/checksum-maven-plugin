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
import org.codehaus.plexus.digest.Md5Digester;
import org.codehaus.plexus.digest.Sha1Digester;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO.
 *
 * @author Julien Nicoulaud <julien.nicoulaud@gmail.com>
 * @see org.codehaus.plexus.digest.Digester
 * @since 0.1
 */
public class DigesterFactory
{
    /**
     * TODO.
     */
    private static DigesterFactory instance;

    /**
     * TODO.
     */
    protected Map<String, Digester> digesters = new HashMap<String, Digester>( 7 );

    /**
     * TODO.
     *
     * @see #getInstance()
     */
    private DigesterFactory()
    {
    }

    /**
     * TODO.
     *
     * @return TODO
     */
    public static DigesterFactory getInstance()
    {
        if ( instance == null )
        {
            instance = new DigesterFactory();
        }
        return instance;
    }

    /**
     * TODO.
     *
     * @param algorithm TODO
     * @return TODO
     * @throws NoSuchAlgorithmException TODO
     * @see org.codehaus.plexus.digest.Digester TODO
     */
    public Digester getDigester( String algorithm ) throws NoSuchAlgorithmException
    {
        Digester digester = digesters.get( algorithm );

        if ( digester == null )
        {
            if ( "CRC32".equalsIgnoreCase( algorithm ) )
            {
                digester = new CRC32Digester();
            }
            else if ( "MD2".equalsIgnoreCase( algorithm ) )
            {
                digester = new Md2Digester();
            }
            else if ( "MD5".equalsIgnoreCase( algorithm ) )
            {
                digester = new Md5Digester();
            }
            else if ( "SHA-1".equalsIgnoreCase( algorithm ) )
            {
                digester = new Sha1Digester();
            }
            else if ( "SHA-256".equalsIgnoreCase( algorithm ) )
            {
                digester = new Sha256Digester();
            }
            else if ( "SHA-384".equalsIgnoreCase( algorithm ) )
            {
                digester = new Sha384Digester();
            }
            else if ( "SHA-512".equalsIgnoreCase( algorithm ) )
            {
                digester = new Sha512Digester();
            }
            else
            {
                throw new NoSuchAlgorithmException();
            }

            digesters.put( algorithm, digester );
        }

        return digester;
    }
}
