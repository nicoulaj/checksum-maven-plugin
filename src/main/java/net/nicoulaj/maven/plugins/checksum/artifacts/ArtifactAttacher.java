/*
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
package net.nicoulaj.maven.plugins.checksum.artifacts;

import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

import java.io.File;

public class ArtifactAttacher implements ArtifactListener {
    private final MavenProject project;
    private final MavenProjectHelper projectHelper;

    public ArtifactAttacher(MavenProject project, MavenProjectHelper projectHelper) {
        this.project = project;
        this.projectHelper = projectHelper;
    }

    @Override
    public void artifactCreated(File artifact, String checksumType, String artifactExtension, String artifactClassifier) {
        if (checksumType.startsWith(".")) {
            // Project helper expects a type without leading dot (e.g. turn ".md5" into "md5").
            checksumType = checksumType.substring(1);
        }

        String checksumArtifactType = artifactExtension + "." + checksumType;
        projectHelper.attachArtifact(project, checksumArtifactType, artifactClassifier, artifact);
    }
}
