package net.nicoulaj.maven.plugins.checksum.artifacts;

import java.io.File;

import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

public class ArtifactAttacher implements ArtifactListener {
    private final MavenProject project;
    private final MavenProjectHelper projectHelper;

    public ArtifactAttacher(MavenProject project, MavenProjectHelper projectHelper) {
        this.project = project;
        this.projectHelper = projectHelper;
    }

    @Override
    public void artifactCreated(File artifact, String type) {
        if (type.startsWith(".")) {
            // Project helper expects a type without leading dot (e.g. turn ".md5" into "md5").
            type = type.substring(1);
        }
        projectHelper.attachArtifact(project, type, null, artifact);
    }
}
