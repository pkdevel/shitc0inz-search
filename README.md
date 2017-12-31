# build
mvn clean install

# build docker image (optional)
docker build --build-arg JAR_FILE=target/shitc0inz-0.0.1-SNAPSHOT.jar -t pkdevel/shitc0inz:0.0.1 .

# build with docker compose
docker-compose build

# run with docker compose
docker-compose up
