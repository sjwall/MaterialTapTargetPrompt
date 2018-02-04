# Creating a release

The instructions below detail the release process:

0. Increment versionCode in `build.gradle` by 1
0. Increment versionName in `build.gradle` as per [Semantic Versioning](http://semver.org/)
0. In `local.properties` set `bintray.user` to the Bintray username and set `bintray.apikey` to the Bintray api key.
0. Run the gradle task `bintrayUpload` which will build the library, generate JavaDocs and run the unit tests.
    If the unit tests pass it will upload the library to Bintray ready for release to jcenter:
    ```bash
    ./gradlew bintrayUpload
    ```
0. Delete the existing JavaDocs in `docs/javadocs`
0. Copy the newly generated JavaDocs from `library/build/docs` to `docs/javadocs`
0. Update `CHANGELOG.md` detailing any changes made in the new release
0. Update README.md installation instructions to reference the new library version
0. Commit and push
0. Log in to Bintray and release the update
