package net.nicoulaj.plugins.checksum;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * TODO.
 *
 * @goal checksum
 */
public class ChecksumMojo extends AbstractMojo
{
  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  //protected MavenProject project;

  /**
   * Files for which a checksum should be computed.
   *
   * @parameter
   * @required
   */
  private File[] files;

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
