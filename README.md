# URL Shortener Application (JEE + SpringBoot + Hibernate + Postgres)
## Getting Started

**Prerequisites (Developed and Tested with):**

* **[JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)** (v1.8.0_181 or later)
* **[Maven](https://maven.apache.org/download.cgi)** (v3.5.0 or later)
* **[Docker](https://docs.docker.com/release-notes/docker-ce/)** (v17.09.0-ce or later)
* **[pgAdmin](https://www.pgadmin.org/download/)** (4 or later)
* **[Internet connectivity](https://dictionary.cambridge.org/dictionary/english/internet)**

Most often running the `mvn clean` version in `pom.xml` and running `mvn install` in this directory should be enough. For non-docker setup, `mvn spring-boot:run` could be used as well.

### Docker Deploy
Generate a JAR file for the application by running
```
mvn clean install
```
Build the image with docker-compose and start the container with the following one-line command. It's advisable not to use the cached images in some scenarios.

```
docker-compose down && docker-compose build --no-cache && docker-compose up --force-recreate --remove-orphans
```

When the server is ready, start the Frontend as well or import the Postman-Collection for playing around. Swagger UI is available at: [Swagger](http://localhost:8080/swagger-ui.html)🚀

**(OPTIONAL)** To stop and kill all containers along with removing all images
```
docker kill $(docker ps -q) && docker rm $(docker ps -a -q) && docker rmi $(docker images -q)
```

## Maintainer
[Debasis Kar](mailto:debasis.babun@gmail.com)
