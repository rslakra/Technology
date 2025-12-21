# swagger-sample

## Swagger UI

This project uses **springdoc-openapi** (compatible with Spring Boot 3) for API documentation.

To verify that springdoc-openapi is working, we can visit this URL in our browser to view the OpenAPI JSON:
```shell
http://localhost:8080/v3/api-docs
```

Now we can test it in our browser by visiting the Swagger UI:
```shell
http://localhost:8080/swagger-ui.html
```

Or alternatively:
```shell
http://localhost:8080/swagger-ui/index.html
```


## Create Network
```shell
docker network create mysql-network
```

```shell
docker network ls
```

## Pull and run mysql image
```
docker container run --network mysql-network --name mysql-docker -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=EmployeeService -d mysql:latest

#docker container run --network mysql-network --name mysql-docker -p 3306:3306 -e MYSQL_ROOT_HOST=localhost -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=EmployeeService -d mysql:latest
```

```shell
docker stop mysql-docker
docker rm mysql-docker
```

## Can use below command to get into mysql bash and run queries
```
docker exec -it mysql-docker mysql -uroot -proot
use EmployeeService;
show databases;
show tables;
exit;
```

## Create a spring boot project at start.spring.io and write a little rest api to fetch employees

## Compile Application
```shell
mvn clean package -DskipTests
```

## Test your application on localhost:8080

## Create Dockerfile under the project directory and put below lines in it
```
FROM openjdk:21
VOLUME /tmp
ADD ./target/swagger-sample.jar app.jar
EXPOSE 8080
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
```

## And build docker image
```
docker build -t dockerlakra/swagger-sample .
```

## Run your docker image and the microservice is up
```
docker run -p 8080:8080 -d --name swagger-sample dockerlakra/swagger-sample
```

## Check Logs
```shell
docker container logs -f swagger-sample
```

## Or instead of creating Dockerfile we can also use docker-maven-plugin the pom

## Add docker-maven-plugin to your pom
```
<properties>
   <docker.image.prefix>dockerlakra</docker.image.prefix>
</properties>
<build>
    <plugins>
    <plugin>
      <groupId>com.spotify</groupId>
      <artifactId>docker-maven-plugin</artifactId>
      <version>0.4.13</version>
      <configuration>
        <imageName>${docker.image.prefix}/swagger-sample</imageName>
        <baseImage>openjdk:21</baseImage>
        <entryPoint>["java", "-jar", "/${project.build.finalName}.jar"]</entryPoint>
        <resources>
          <resource>
            <targetPath>/</targetPath>
            <directory>${project.build.directory}</directory>
            <include>${project.build.finalName}.jar</include>
          </resource>
        </resources>
      </configuration>
    </plugin>
    </plugins>
</build>
```

## And build your application using which will also build the docker image
```
./mvnw clean package docker:build
```

## Run your docker image and the microservice is up
```
docker run -p 8080:8080 -d --name swagger-sample dockerlakra/swagger-sample
```

## If we want to run your microservice container in docker by linking mysql container in docker instead of directly pointing to url we can do that by below command
```shell
docker run --link mysql-docker --name swagger-sample -d dockerlakra/swagger-sample
```

OR

```shell
docker container run --network=mysql-network --name swagger-sample -p 8080:8080 -d dockerlakra/swagger-sample
```

OR

```shell
docker container run --network=mysql-network --link mysql-docker --name swagger-sample -p 8080:8080 -d dockerlakra/swagger-sample
```

## Remove Docker Services
```shell
docker rm swagger-sample
```

## Build Application

### Using Maven
```shell
./buildMaven.sh
```

Or manually:
```shell
mvn clean install -DskipTests=true
```

### Using Gradle
```shell
./buildGradle.sh
```

Or manually:
```shell
./gradlew clean build -x test
```

## Run Application

### Using Maven
```shell
./runMaven.sh
```

### Using Gradle
```shell
./runGradle.sh
```

## References
- [Docker MySQL](https://www.javainuse.com/devOps/docker/docker-mysql)
- [springdoc-openapi Documentation](https://springdoc.org/)

# Author
- Rohtash Lakra
