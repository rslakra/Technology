# https://docs.docker.com/reference/dockerfile/

# Create a new build stage from a base image.
FROM amazoncorretto:21-alpine3.17-jdk

# Add metadata to an image.
LABEL maintainer="Rohtash Lakra <work.lakra@gmail.com>"

# Change working directory.
WORKDIR /app

# Copy files and directories.
COPY target/thymeleaf-templates.jar thymeleaf-templates.jar

# Describe which ports your application is listening on.
EXPOSE 8080

# Specify default executable.

# ENTRYPOINT - the exec form makes it possible to avoid shell string munging.
# And to invoke commands using a specific command shell, or any other executable.
# It uses a JSON array syntax, where each element in the array is a command,
# flag, or argument.
# The RUN, CMD, and ENTRYPOINT instructions all have two possible forms:
# For Example: INSTRUCTION ["executable","param1","param2"] (exec form)
ENTRYPOINT ["java", "-jar", "thymeleaf-templates.jar"]

# The shell form is more relaxed, and emphasizes ease of use, flexibility, and
# readability. The shell form automatically uses a command shell, whereas the
# exec form does not.
# For Example: RUN ["/app/java -jar target/thymeleaf-templates.jar"]
# For Example: CMD ["java", "-jar", "thymeleaf-templates.jar"]
# CMD ["java", "-jar", "thymeleaf-templates.jar"]

