## URL Shortener App Demo

## Steps to Setup

**1. Clone the repository**

```bash
git clone 
```

**2. Run the App inside Docker**
Generate a JAR file for the application by running
```bash
mvn clean install -DskipTests
```

Type the following command from the root directory of the project to run it -

```bash
docker-compose down && docker-compose build --no-cache && docker-compose up --force-recreate --remove-orphans
```