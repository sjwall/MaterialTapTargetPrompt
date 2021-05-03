---
layout: home
---

# Creating a release

The instructions below detail the release process:

0. Increment versionCode in `build.gradle` by 1
0. Increment versionName in `build.gradle` as per [Semantic Versioning](http://semver.org/)
0. In `local.properties` set the following properties
    - `sona.usr` - Sonatype username
    - `sona.key` - Sonatype password
    - `gpr.usr` - Github username
    - `gpr.key` - Github access token
0. Create `secring.gpg` using: `gpg --export-secret-keys -o secring.gpg`
0. Set the following properties in `~/.gradle/gradle.properties`
    - `signing.keyId` - The id for the key to use, can be found with: `gpg --list-keys --keyid-format short`
    - `signing.password` - The password
    - `signing.secretKeyRingFile` - The absolute path to `secring.gpg`
0. Run the gradle task `clean assemble` which will build the library, generate JavaDocs and run the unit tests
0. Run the gradle task `publish`
0. Delete the existing JavaDocs in `docs/javadoc`
0. Copy the newly generated JavaDocs from `library/build/docs` to `docs/javadoc`
0. Update `CHANGELOG.md` detailing any changes made in the new release
0. Update README.md installation instructions to reference the new library version. The version numbering in the docs is retrieved from the github releases
0. Commit and push
0. Log in to Sonatype and release the update
