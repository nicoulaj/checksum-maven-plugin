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
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * TODO.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.0
 */
public class MessageDigester extends AbstractDigester
{
    private static final byte[] DIGITS = "0123456789abcdef".getBytes();

    private static final int STREAMING_BUFFER_SIZE = 32768;

    protected final MessageDigest md;

    protected MessageDigester( String algorithm ) throws NoSuchAlgorithmException
    {
        super( algorithm );
        md = MessageDigest.getInstance( algorithm );
    }

    public String calc( File file ) throws DigesterException
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream( file );
            md.reset();

            byte[] buffer = new byte[STREAMING_BUFFER_SIZE];
            int size = fis.read( buffer, 0, STREAMING_BUFFER_SIZE );
            while ( size >= 0 )
            {
                md.update( buffer, 0, size );
                size = fis.read( buffer, 0, STREAMING_BUFFER_SIZE );
            }

            return encode( md.digest() );
        }
        catch ( IOException e )
        {
            throw new DigesterException( "Unable to calculate the " + getAlgorithm() +
                                         " hashcode for " + file.getAbsolutePath() + ": " + e.getMessage(), e );
        }
        finally
        {
            try
            {
                fis.close();
            }
            catch ( IOException e )
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    protected static String encode( byte[] data )
    {
        int l = data.length;

        byte[] raw = new byte[l * 2];

        for ( int i = 0, j = 0; i < l; i++ )
        {
            raw[j++] = DIGITS[( 0xF0 & data[i] ) >>> 4];
            raw[j++] = DIGITS[0x0F & data[i]];
        }

        return new String( raw );
    }

}
