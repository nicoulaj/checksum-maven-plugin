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
package net.nicoulaj.maven.plugins.checksum.test;

import net.nicoulaj.maven.plugins.checksum.digester.CRC32Digester;
import net.nicoulaj.maven.plugins.checksum.digester.Md2Digester;
import net.nicoulaj.maven.plugins.checksum.digester.Sha256Digester;
import net.nicoulaj.maven.plugins.checksum.digester.Sha384Digester;
import net.nicoulaj.maven.plugins.checksum.digester.Sha512Digester;
import org.codehaus.plexus.digest.Digester;
import org.codehaus.plexus.digest.Md5Digester;
import org.codehaus.plexus.digest.Sha1Digester;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Tests for each implementation of {@link org.codehaus.plexus.digest.Digester}.
 *
 * @author Julien Nicoulaud <julien.nicoulaud@gmail.com>
 * @see org.codehaus.plexus.digest.Digester
 * @since 0.1
 */
@RunWith( Parameterized.class )
public class DigestersTest
{
    /**
     * The {@link org.codehaus.plexus.digest.Digester} tested.
     */
    private Digester digester;

    /**
     * Generate the list of arguments with which the test should be run.
     *
     * @return the list of tested {@link org.codehaus.plexus.digest.Digester} implementations.
     */
    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters()
    {
        Object[][] data = new Object[][]{{new CRC32Digester()},
                                        {new Md2Digester()},
                                        {new Md5Digester()},
                                        {new Sha1Digester()},
                                        {new Sha256Digester()},
                                        {new Sha384Digester()},
                                        {new Sha512Digester()}};
        return Arrays.asList( data );
    }

    /**
     * Build a new {@link ChecksumFactoryDigestersTest}.
     *
     * @param digester an instance of the tested {@link org.codehaus.plexus.digest.Digester} implementing class.
     */
    public DigestersTest( Digester digester )
    {
        this.digester = digester;
    }

    /**
     * Assert the algorithm name is not null/empty.
     */
    @Test
    public void testAlgorithmNameDefined()
    {
        String algorithmName = digester.getAlgorithm();
        Assert.assertNotNull( "The algorithm name is null.", algorithmName );
        Assert.assertTrue( "The algorithm name is empty.", algorithmName.length() > 0 );
    }

    /**
     * Assert the file name extension is not null/empty.
     */
    @Test
    public void testFilenameExtensionDefined()
    {
        String filenameExtension = digester.getFilenameExtension();
        Assert.assertNotNull( "The file name extension is null.", filenameExtension );
        Assert.assertTrue( "The file name extension is empty.", filenameExtension.length() > 0 );
    }
}
