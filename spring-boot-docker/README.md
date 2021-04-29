## The Entry Point

If the command line gets a bit long you can extract it out into a shell script and ```sh COPY``` 
it into the image before you run it. Example:

```sh
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY run.sh
COPY target/*.jar app.jar
ENTRYPOINT ["run.sh"]
````

Remember to use the ```sh exec java ...``` to launch the java process so it can handle the ``sh KILL`` signals:

```sh
run.sh

#!/bin/sh
exec java -jar /app.jar
```

${} substitution requires a shell; the exec form doesn't use a shell to
launch the process, so the options will not be applied. You can get round
that by moving the entry point to a script (like ```sh run.sh``` example above), or by explicity creating a shell in the entry point.

```sh
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTIONS} -jar /app.jar"]
```

Using an ```sh ENTRYPOINT``` with an explicit shell like the above means you can pass environment 
variables into the java command, but so far you cannot also provide command line arguments to the 
Spring Boot application. This trick doesn't work to run the app on port 9000:

```sh docker run -p 9000:9000 myorg/myapp --server.port=9000```

The reason id didn't work is because the docker command line (the ```sh --server.port=9000``` part) is passed to the entry point (```sh sh```), not to the java process which it launches. To fix that you need to add the command line from the ```sh CMD``` to the ```sh ENTRYPOINT```:

```sh
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTIONS} -jar /app.jar ${0} ${@}"]
```
