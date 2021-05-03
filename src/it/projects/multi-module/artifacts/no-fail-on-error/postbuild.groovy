/**
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2021 checksum-maven-plugin contributors
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
import net.nicoulaj.maven.plugins.checksum.test.integration.PostBuildScriptHelper

try
{
  // Instantiate a helper.
  PostBuildScriptHelper helper = new PostBuildScriptHelper( basedir, localRepositoryPath, context )

  // Fail if there are warnings
  helper.assertBuildLogDoesNotContain('[WARNING]')

  // Fail if no traces of checksum-maven-plugin invocation.
  helper.assertBuildLogContains( "checksum-maven-plugin" );

  // Check files have been created and are not empty.
  helper.assertFileIsNotEmpty( "module1/target/multi-module.artifacts.no-fail-on-error.module1-1.0-SNAPSHOT.jar.md5" )
  helper.assertFileIsNotEmpty( "module1/target/multi-module.artifacts.no-fail-on-error.module1-1.0-SNAPSHOT.jar.sha1" )
  helper.assertFileIsNotEmpty( "module2/target/multi-module.artifacts.no-fail-on-error.module2-1.0-SNAPSHOT.jar.md5" )
  helper.assertFileIsNotEmpty( "module2/target/multi-module.artifacts.no-fail-on-error.module2-1.0-SNAPSHOT.jar.sha1" )

}
catch ( Exception e )
{
  System.err.println( e.getMessage() )
  return false;
}
