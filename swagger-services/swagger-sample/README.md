# swagger-sample

## Swagger UI

To verify that Springfox is working, we can visit this URL in our browser:
```shell
http://localhost:8080/v2/api-docs
```

Now we can test it in our browser by visiting:
```shell
http://localhost:8080/swagger-ui/
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
ADD ./target/employee-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
```

## And build docker image
```
docker build -t dockerlakra/employee-service .
```

## Run your docker image and the microservice is up
```
docker run -p 8080:8080 -d --name employee-service dockerlakra/employee-service
```

## Check Logs
```shell
docker container logs -f employee-service
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
        <imageName>${docker.image.prefix}/employee-service</imageName>
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
docker run -p 8080:8080 -d --name employee-service dockerlakra/employee-service
```

## If we want to run your microservice container in docker by linking mysql container in docker instead of directly pointing to url we can do that by below command
```shell
docker run --link mysql-docker --name employee-service -d dockerlakra/employee-service
```

OR

```shell
docker container run --network=mysql-network --name employee-service -p 8080:8080 -d dockerlakra/employee-service
```

OR

```shell
docker container run --network=mysql-network --link mysql-docker --name employee-service -p 8080:8080 -d dockerlakra/employee-service
```

## Remove Docker Services
```shell
docker rm employee-service
```

## References
- [Docker MySQL](https://www.javainuse.com/devOps/docker/docker-mysql)

# Author
- Rohtash Lakra
