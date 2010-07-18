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
package net.nicoulaj.plugins.checksum;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * bla bla bla.
 *
 * @goal checksum
 */
public class ChecksumMojo extends AbstractMojo
{

  /**
   * Files for which a checksum should be computed.
   *
   * @parameter
   * @required
   */
  private File[] files;

  /**
   * {@inheritDoc}
   *
   * @throws MojoExecutionException
   */
  public void execute() throws MojoExecutionException
  {
    try
    {
      for (File file: files){
        getLog().info("Checksum for " + file.getPath() + ": " + getMD5Checksum(file));
      }
    } catch (Exception e)
    {
      throw new MojoExecutionException("Failed computing checksum", e);
    }
  }

  /**
   * 
   * @param file
   * @return
   * @throws Exception
   */
  private String getMD5Checksum(File file) throws Exception
  {
    InputStream fis = new FileInputStream(file);
    byte[] buffer = new byte[1024];
    MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;
    do
    {
      numRead = fis.read(buffer);
      if (numRead > 0)
      {
        complete.update(buffer, 0, numRead);
      }
    } while (numRead != -1);
    fis.close();
    byte[] b = complete.digest();
    String result = "";
    for (int i = 0; i < b.length; i++)
    {
      result +=
        Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
  }
}
