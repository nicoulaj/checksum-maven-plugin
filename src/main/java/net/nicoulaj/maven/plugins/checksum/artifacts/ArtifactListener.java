package net.nicoulaj.maven.plugins.checksum.artifacts;

import java.io.File;

public interface ArtifactListener {
    void artifactCreated(File artifact, String type);
}
