/**
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2020 checksum-maven-plugin contributors
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

// Instantiate a helper.
PostBuildScriptHelper helper = new PostBuildScriptHelper( basedir, localRepositoryPath, context )

// Fail if there are errors
helper.assertBuildLogDoesNotContain('[ERROR]')

// Fail if no traces of checksum-maven-plugin invocation.
helper.assertBuildLogContains( "checksum-maven-plugin" );

// Check files have been created and are not empty.
helper.assertFileDoesNotExist( "target/single-artifact.artifacts.attach-checksums-with-classifier-filtering-1.0-SNAPSHOT.jar.sha512" )
helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.attach-checksums-with-classifier-filtering-1.0-SNAPSHOT-sources.jar.sha512" )
helper.assertFileDoesNotExist( "target/single-artifact.artifacts.attach-checksums-with-classifier-filtering-1.0-SNAPSHOT-javadoc.jar.sha512" )
helper.assertFileDoesNotExistInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/single-artifact.artifacts.attach-checksums-with-classifier-filtering/1.0-SNAPSHOT/single-artifact.artifacts.attach-checksums-with-classifier-filtering-1.0-SNAPSHOT.jar.sha512" )
helper.assertFileIsNotEmptyInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/single-artifact.artifacts.attach-checksums-with-classifier-filtering/1.0-SNAPSHOT/single-artifact.artifacts.attach-checksums-with-classifier-filtering-1.0-SNAPSHOT-sources.jar.sha512" )
helper.assertFileDoesNotExistInLocalRepo( "net/nicoulaj/maven/plugins/checksum/test/projects/single-artifact.artifacts.attach-checksums-with-classifier-filtering/1.0-SNAPSHOT/single-artifact.artifacts.attach-checksums-with-classifier-filtering-1.0-SNAPSHOT-javadoc.jar.sha512" )
