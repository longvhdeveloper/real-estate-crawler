FROM openjdk:11-jre-slim-buster
LABEL maintainer="vohoanglong07@gmail.com"
ADD target/property-0.0.1-SNAPSHOT.jar property.jar
ENTRYPOINT ["java","-jar","property.jar"]