/**
 * checksum-maven-plugin - http://checksum-maven-plugin.nicoulaj.net
 * Copyright © 2010-2016 checksum-maven-plugin contributors
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

  // Fail if no traces of checksum-maven-plugin invocation.
  helper.assertBuildLogContains( "checksum-maven-plugin" );

  // Check files have been created and are not empty.
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.cksum" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.crc32" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.blake2b160" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.blake2b256" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.blake2b384" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.blake2b512" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.gost3411" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.gost34112012256" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.gost34112012512" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.keccak224" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.keccak256" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.keccak288" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.keccak384" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.keccak512" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.md2" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.md4" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.md5" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.ripemd128" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.ripemd160" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.ripemd256" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.ripemd320" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha1" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha224" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha256" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha3224" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha3256" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha3384" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha3512" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha384" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha512" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha512224" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sha512256" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein10241024" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein1024384" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein1024512" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein256128" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein256160" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein256224" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein256256" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein512128" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein512160" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein512224" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein512256" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein512384" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.skein512512" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.sm3" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.tiger" )
  helper.assertFileIsNotEmpty( "target/single-artifact.artifacts.custom-algorithms-1.0-SNAPSHOT.jar.whirlpool" )

}
catch ( Exception e )
{
  System.err.println( e.getMessage() )
  return false;
}
