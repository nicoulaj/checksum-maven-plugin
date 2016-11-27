/**
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2016 checksum-maven-plugin contributors
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
package net.nicoulaj.maven.plugins.checksum.test.unit.digest;


import net.nicoulaj.maven.plugins.checksum.digest.CksumFileDigester;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Special testcase for cksum {@link CksumFileDigester} of the empty file
 *
 */
public class CksumFileDigesterTest  {

    @Test
    public void testEmptyFileCksum() throws Exception {
        File empty_file = File.createTempFile("empty_file", null);

        CksumFileDigester cksumDigester=new CksumFileDigester();
        String cksumEmpty = cksumDigester.calculate(empty_file);

        Assert.assertEquals("4294967295", cksumEmpty);

    }
}
