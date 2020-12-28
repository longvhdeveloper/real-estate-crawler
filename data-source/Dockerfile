FROM openjdk:11-jre-slim-buster
LABEL maintainer="vohoanglong07@gmail.com"
ADD target/data-source-0.0.1-SNAPSHOT.jar data-source.jar
ENTRYPOINT ["java","-jar","data-source.jar"]