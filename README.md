## Infinum task

### Code style

Google code style is being used for writing Java.
Appropriate configuration can be found on following links:

[Eclipse](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)

[IntelliJ IDEA](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)

Additionally, maximum line width is set to 150 characters.

Furthermore, Lombok is being used for development, so appropriate plugin is required.

### Using application
Manual for using application

#### Requirements 

Installed docker and docker-compose.

#### Starting

Execute `docker-compose up -d` in docker directory, in order to run database.
After that step, application is ready for usage and can be started in any desired manner.

#### Packaging

Run `gradlew build` from project root. JAR file will be located in `build/libs/` directory.

#### Running tests
Run `gradlew clean test` from project root.