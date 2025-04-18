# Docker Microservice Application

## Pull and run mysql image
```
docker run -p 3306:3306 --name mysql-docker -e MYSQL_ROOT_HOST=209.131.48.141 -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=EmployeeService -d mysql/mysql-server:latest
```

## Can use below command to get into mysql bash and run queries
```
docker exec -it mysql-docker mysql -uroot -p
use EmployeeService;
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
ADD target/docker-service-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
```

## And build docker image
```
docker build -t dockerlakra/docker-service .
```

## Run your docker image and the microservice is up
```
docker run -p 8080:8080 -d --name docker-service dockerlakra/docker-service
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
        <imageName>${docker.image.prefix}/docker-service</imageName>
        <baseImage>java:21</baseImage>
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
docker run -p 8080:8080 -d --name docker-service dockerlakra/docker-service
```

## If we want to run your microservice container in docker by linking mysql container in docker instead of directly pointing to url we can do tha by below command
```
docker run --name docker-service --link mysql-docker -d dockerlakra/docker-service
```

OR

```shell
docker run --name docker-service --network=docker-network --link mysql-docker -d dockerlakra/docker-service
```

## References
- [Docker MySQL](https://www.javainuse.com/devOps/docker/docker-mysql)
