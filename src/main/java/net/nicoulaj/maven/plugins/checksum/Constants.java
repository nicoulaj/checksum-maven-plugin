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
package net.nicoulaj.maven.plugins.checksum;

/**
 * Constants used by checksum-maven-plugin.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 1.0
 */
public class Constants
{
    /**
     * The CRC/checksum digest algorithms supported by checksum-maven-plugin.
     */
    public static final String[] SUPPORTED_ALGORITHMS = {
        "Cksum",
        "CRC32",
        "BLAKE2B-160",
        "BLAKE2B-256",
        "BLAKE2B-384",
        "BLAKE2B-512",
        "GOST3411",
        "GOST3411-2012-256",
        "GOST3411-2012-512",
        "KECCAK-224",
        "KECCAK-256",
        "KECCAK-288",
        "KECCAK-384",
        "KECCAK-512",
        "MD2",
        "MD4",
        "MD5",
        "RIPEMD128",
        "RIPEMD160",
        "RIPEMD256",
        "RIPEMD320",
        "SHA",
        "SHA-1",
        "SHA-224",
        "SHA-256",
        "SHA3-224",
        "SHA3-256",
        "SHA3-384",
        "SHA3-512",
        "SHA-384",
        "SHA-512",
        "SHA-512/224",
        "SHA-512/256",
        "SKEIN-1024-1024",
        "SKEIN-1024-384",
        "SKEIN-1024-512",
        "SKEIN-256-128",
        "SKEIN-256-160",
        "SKEIN-256-224",
        "SKEIN-256-256",
        "SKEIN-512-128",
        "SKEIN-512-160",
        "SKEIN-512-224",
        "SKEIN-512-256",
        "SKEIN-512-384",
        "SKEIN-512-512",
        "SM3",
        "TIGER",
        "WHIRLPOOL"
    };

    /**
     * The algorithms used by default for a mojo execution.
     */
    public static final String[] DEFAULT_EXECUTION_ALGORITHMS = { "MD5", "SHA-1" };

    /**
     * The file encoding used by default.
     */
    public static final String DEFAULT_ENCODING = "UTF-8";
}
