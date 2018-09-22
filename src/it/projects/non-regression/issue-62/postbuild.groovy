/*
 * Copyright 2010-2018 Julien Nicoulaud <julien.nicoulaud@gmail.com>
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
import net.nicoulaj.maven.plugins.checksum.test.integration.PostBuildScriptHelper

try
{
  // Instantiate a helper.
  PostBuildScriptHelper helper = new PostBuildScriptHelper( basedir, localRepositoryPath, context )

  // Fail if there are warnings
  helper.assertBuildLogDoesNotContain('[WARNING]')
  helper.assertBuildLogDoesNotContain('[ERROR]')

  // Fail if no traces of checksum-maven-plugin invocation.
  helper.assertBuildLogContains( "checksum-maven-plugin" );

  // Check files have been created and are not empty.
  helper.assertFileIsNotEmpty( "target/issue-62-1.0-SNAPSHOT.jar.sha256" )
  helper.assertFileIsNotEmpty( "target/issue-62-1.0-SNAPSHOT.jar.sha512" )
  helper.assertFileIsNotEmpty( "target/issue-62-1.0-SNAPSHOT-src.tar.bz2.sha256" )
  helper.assertFileIsNotEmpty( "target/issue-62-1.0-SNAPSHOT-src.tar.bz2.sha512" )
  helper.assertFileIsNotEmpty( "target/issue-62-1.0-SNAPSHOT-src.tar.gz.sha256" )
  helper.assertFileIsNotEmpty( "target/issue-62-1.0-SNAPSHOT-src.tar.gz.sha512" )
  helper.assertFileIsNotEmpty( "target/issue-62-1.0-SNAPSHOT-src.zip.sha256" )
  helper.assertFileIsNotEmpty( "target/issue-62-1.0-SNAPSHOT-src.zip.sha512" )

  // Check files are installed to the right location
  helper.assertFileIsNotEmptyInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/issue-62/1.0-SNAPSHOT/issue-62-1.0-SNAPSHOT.jar.sha256" )
  helper.assertFileIsNotEmptyInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/issue-62/1.0-SNAPSHOT/issue-62-1.0-SNAPSHOT.jar.sha512" )
  helper.assertFileIsNotEmptyInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/issue-62/1.0-SNAPSHOT/issue-62-1.0-SNAPSHOT-src.tar.bz2.sha256" )
  helper.assertFileIsNotEmptyInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/issue-62/1.0-SNAPSHOT/issue-62-1.0-SNAPSHOT-src.tar.bz2.sha512" )
  helper.assertFileIsNotEmptyInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/issue-62/1.0-SNAPSHOT/issue-62-1.0-SNAPSHOT-src.tar.gz.sha256" )
  helper.assertFileIsNotEmptyInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/issue-62/1.0-SNAPSHOT/issue-62-1.0-SNAPSHOT-src.tar.gz.sha512" )
  helper.assertFileIsNotEmptyInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/issue-62/1.0-SNAPSHOT/issue-62-1.0-SNAPSHOT-src.zip.sha256" )
  helper.assertFileIsNotEmptyInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/issue-62/1.0-SNAPSHOT/issue-62-1.0-SNAPSHOT-src.zip.sha512" )

}
catch ( Exception e )
{
  System.err.println( e.getMessage() )
  return false;
}
