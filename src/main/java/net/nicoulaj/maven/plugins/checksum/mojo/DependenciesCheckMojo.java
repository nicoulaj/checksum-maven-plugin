/*
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2018 checksum-maven-plugin contributors
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
package net.nicoulaj.maven.plugins.checksum.mojo;

import net.nicoulaj.maven.plugins.checksum.digest.DigesterException;
import net.nicoulaj.maven.plugins.checksum.digest.DigesterFactory;
import net.nicoulaj.maven.plugins.checksum.digest.FileDigester;
import net.nicoulaj.maven.plugins.checksum.execution.ExecutionException;
import net.nicoulaj.maven.plugins.checksum.execution.target.CsvSummaryFileTarget;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * checks the summary file (csv) against the project dependencies.
 *
 * @author <a href="mailto:gunnar.tjarks@governikus.de">Gunnar Tjarks</a>
 * @since 1.8
 */
@Mojo(
   name = DependenciesCheckMojo.NAME,
   defaultPhase = LifecyclePhase.VERIFY,
   requiresProject = true,
   inheritByDefault = false,
   requiresDependencyResolution = ResolutionScope.RUNTIME,
   threadSafe = true)
public class DependenciesCheckMojo extends AbstractMojo
{

  /**
   * The mojo name.
   */
  public static final String NAME = "check";

  /**
   * The Maven project.
   */
  @Parameter(property = "project", required = true, readonly = true)
  protected MavenProject project;

  /**
   * The name of the summary file.
   */
  @Parameter(property = "csvSummaryFile", defaultValue = "dependencies-checksums.csv")
  protected String csvSummaryFile;

    /**
     * The dependency scopes to include.
     *
     * <p>Allowed values are compile, test, runtime, provided and system.<br>All scopes are included by default.
     *
     * <p> Use the following syntax:
     * <pre>&lt;scopes&gt;
     *   &lt;scope&gt;compile&lt;scope&gt;
     *   &lt;scope&gt;runtime&lt;scope&gt;
     * &lt;/scopes&gt;</pre>
     *
     * @since 1.0
     */
  @Parameter
  protected List<String> scopes;

  /**
   * The dependency types to include.
   *
   * <p>All types are included by default.
   *
   * <p>
   * Use the following syntax:
   * <pre>
   * &lt;types&gt;
   *   &lt;type&gt;jar&lt;type&gt;
   *   &lt;type&gt;zip&lt;type&gt;
   * &lt;/types&gt;
   * </pre>
   */
  @Parameter
  protected List<String> types;

  /**
   * Transitive dependencies or direct dependencies only.
   */
  @Parameter(property = "transitive", defaultValue = "false")
  protected boolean transitive;

  /**
   * Flag used to suppress execution.
   */
  @Parameter(property = "checksum.skip", defaultValue = "false")
  protected boolean skip;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    if (skip)
    {
      getLog().info("Skipping checksum:check execution because property checksum.skip is set.");
    }
    else if (skipPackaging("pom"))
    {
      getLog().warn("The goal is skipped due to packaging '" + project.getPackaging() + "'");
    }
    else
    {
      try
      {
        File summaryFile = FileUtils.resolveFile(new File(project.getBasedir().getAbsolutePath()),
                                                 csvSummaryFile);
        // 1. read summeryFile
        Map<String, Map<String, String>> summaryFileContent = readSummaryFile(summaryFile);

        // 2. dependency check
        checkDependencies(summaryFileContent);
      }
      catch (ExecutionException e)
      {
        getLog().error(e.getMessage());
        throw new MojoFailureException(e.getMessage());
      }
    }
  }

  /**
   * @param packagings the packagings to skip
   * @return {@code true} if the project's packaging is in the given ones.
   */
  private boolean skipPackaging(String... packagings)
  {
    String projectPackaging = project.getPackaging();

    return Arrays.asList(packagings).contains(projectPackaging);
  }

  private void checkDependencies(Map<String, Map<String, String>> summaryFileContent)
    throws ExecutionException
  {
    for ( Artifact artifact : getArtifactsToProcess() )
    {
      Map<String, String> fileHashcodes = summaryFileContent.get(artifact.getFile().getName());
      if (fileHashcodes == null)
      {
        throw new ExecutionException("Artifact " + artifact + " is not in summary file content!");
      }

      for ( Map.Entry<String, String> fileHashcode : fileHashcodes.entrySet() )
      {
        process(fileHashcode.getKey(), artifact, fileHashcode.getValue());
      }
    }
  }

  private void process(String algorithm, Artifact artifact, String hash) throws ExecutionException
  {
    File file = artifact.getFile();
    try
    {
      // Calculate the hash for the file/algo
      FileDigester digester = DigesterFactory.getInstance().getFileDigester(algorithm);
      String calculatedHash = digester.calculate(file);
      if (!calculatedHash.equals(hash))
      {
        throw new ExecutionException("The dependency hash value '" + calculatedHash + "' of file '" + file
                                     + "' does not equal the hash value '" + hash
                                     + "' stored in the summary file!");
      }
      getLog().debug(artifact + " (" + file + ") - " + algorithm + " : " + calculatedHash + " = " + hash);
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new ExecutionException("Unsupported algorithm " + algorithm + ".");
    }
    catch (DigesterException e)
    {
      throw new ExecutionException("Unable to calculate " + algorithm + " hash for " + file.getName() + ": "
                                   + e.getMessage());
    }
  }

  private List<Artifact> getArtifactsToProcess()
  {
    List<Artifact> result = new LinkedList<Artifact>();

    Set<Artifact> allProjectModules = new HashSet<Artifact>();
    allModules(project, allProjectModules);

    @SuppressWarnings("unchecked")
    Set<Artifact> artifacts = transitive ? project.getArtifacts() : project.getDependencyArtifacts();
    for ( Artifact artifact : artifacts )
    {
      if ((!allProjectModules.contains(artifact)) || (scopes == null || scopes.contains(artifact.getScope()))
                                                     && (types == null || types.contains(artifact.getType())))
      {
        getLog().debug(String.format("check artifact %s", artifact));
        result.add(artifact);
      }
    }

    return result;
  }

  private void allModules(MavenProject mavenProject, Set<Artifact> result)
  {
    if (result.contains(mavenProject.getArtifact()))
    {
      return;
    }
    result.add(mavenProject.getArtifact());

    List<MavenProject> modules = new ArrayList<MavenProject>();
    if (mavenProject.hasParent())
    {
      modules.add(mavenProject.getParent());
    }

    @SuppressWarnings("unchecked")
    List<MavenProject> collectedProjects = mavenProject.getCollectedProjects();
    if (collectedProjects != null)
    {
      modules.addAll(collectedProjects);
    }
    for ( MavenProject module : modules )
    {
      allModules(module, result);
    }
  }

  /**
   * Read the summary file
   * 
   * @param outputFile the summary file
   * @return the summary content (<filename, <algo, checksum>>)
   * @throws ExecutionException if an error happens while running the execution.
   */
  private Map<String, Map<String, String>> readSummaryFile(File outputFile) throws ExecutionException
  {
    List<String> algorithms = new ArrayList<String>();
    Map<String, Map<String, String>> filesHashcodes = new HashMap<String, Map<String, String>>();
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new FileReader(outputFile));
      String line;
      while ((line = reader.readLine()) != null)
      {
        // Read the CVS file header
        if (isFileHeader(line))
        {
          readFileHeader(line, algorithms);
        }
        else
        {
          // Read the dependencies checksums
          readDependenciesChecksums(line, algorithms, filesHashcodes);
        }
      }
    }
    catch (IOException e)
    {
      throw new ExecutionException(e.getMessage());
    }
    finally
    {
      IOUtil.close(reader);
    }

    return filesHashcodes;
  }

  private boolean isFileHeader(String line)
  {
    return line.startsWith(CsvSummaryFileTarget.CSV_COMMENT_MARKER + "File");
  }

  private void readFileHeader(String line, List<String> algorithms)
  {
    // #File,sha1,md5,...
    String[] split = line.split(CsvSummaryFileTarget.CSV_COLUMN_SEPARATOR);
    for ( int i = 1 ; i < split.length ; i++ )
    {
      algorithms.add(split[i]);
    }
  }

  private void readDependenciesChecksums(String line,
                                         List<String> algorithms,
                                         Map<String, Map<String, String>> filesHashcodes)
  {
    String[] split = line.split(CsvSummaryFileTarget.CSV_COLUMN_SEPARATOR);
    String fileName = split[0];
    Map<String, String> fileHashcodes = new HashMap<String, String>();
    for ( int i = 1 ; i < split.length ; i++ )
    {
      fileHashcodes.put(algorithms.get(i - 1), split[i]);
    }
    filesHashcodes.put(fileName, fileHashcodes);
  }

}
