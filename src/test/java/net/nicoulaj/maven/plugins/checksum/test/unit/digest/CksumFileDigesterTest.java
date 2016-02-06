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