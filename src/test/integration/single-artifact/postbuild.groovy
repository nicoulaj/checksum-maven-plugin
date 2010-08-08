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

import org.codehaus.plexus.util.FileUtils

try
{
  // Open the build.log file.
  String buildLogContent = FileUtils.fileRead(new File(basedir, "build.log"), "UTF-8");

  // Fail if no traces maven-checksum invocation.
  if (!buildLogContent.contains("maven-checksum-plugin"))
  {
    System.err.println("maven-checksum-plugin has not been invoked.");
    return false;
  }

  // Check files have been created and are not empty.
  String[] filesToCheck = ["target/single-artifact-1.0-SNAPSHOT.jar.crc32",
          "target/single-artifact-1.0-SNAPSHOT.jar.md5",
          "target/single-artifact-1.0-SNAPSHOT.jar.sha1"];
  for (String fileName in filesToCheck)
  {
    File file = new File(basedir, fileName);
    if (file.exists())
    {
      if (FileUtils.fileRead(file).length() == 0)
      {
        System.err.println("The file " + fileName + " is empty.");
        return false;
      }
    }
    else
    {
      System.err.println("The file " + fileName + " is missing.");
      return false;
    }
  }
}
catch (Throwable t)
{
  t.printStackTrace();
  return false;
}

return true;
