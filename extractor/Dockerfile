FROM openjdk:11-jre-slim-buster
LABEL maintainer="vohoanglong07@gmail.com"
ADD target/extractor-0.0.1-SNAPSHOT.jar extractor-url.jar
ENTRYPOINT ["java","-jar","extractor-url.jar"]