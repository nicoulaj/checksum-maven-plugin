/*
 * Copyright 2010-2012 Julien Nicoulaud <julien.nicoulaud@gmail.com>
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

package net.nicoulaj.maven.plugins.checksum.execution.target;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.FileUtils;

import net.nicoulaj.maven.plugins.checksum.digest.DigesterFactory;

/**
 * An {@link ExecutionTarget} that attaches the generated checksum to the project.
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class AttachChecksumTarget implements ExecutionTarget
{
   private final File outputDirectory;
   private final MavenProject project;
   private final MavenProjectHelper projectHelper;

   public AttachChecksumTarget(File outputDirectory, MavenProject project, MavenProjectHelper projectHelper)
   {
      this.outputDirectory = outputDirectory;
      this.project = project;
      this.projectHelper = projectHelper;
   }

   @Override
   public void init() throws ExecutionTargetInitializationException
   {
   }

   @Override
   public void write(String digest, File file, String algorithm) throws ExecutionTargetWriteException
   {

      try
      {
         File outputFileDirectory = (outputDirectory != null) ? outputDirectory : file.getParentFile();
         String fileExtension = DigesterFactory.getInstance().getFileDigester(algorithm).getFileExtension();
         String outputFileName = file.getName() + fileExtension;
         File digestFile = new File(outputFileDirectory, outputFileName);
         String artifactType = FileUtils.getExtension(file.getName()) + fileExtension;
         projectHelper.attachArtifact(project, artifactType, digestFile);
      }
      catch (NoSuchAlgorithmException e)
      {
         throw new ExecutionTargetWriteException(e.getMessage());
      }
   }

   @Override
   public void close() throws ExecutionTargetCloseException
   {
   }

}
