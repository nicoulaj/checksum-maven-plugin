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

  // Look for the right log message..
  if (!buildLogContent.contains("[WARNING] No file to generate checksums for."))
  {
    System.err.println("Expected message not found in build log: '[WARNING] No file to generate checksums for.'");
    return false;
  }
}
catch (Throwable t)
{
  t.printStackTrace();
  return false;
}

return true;
