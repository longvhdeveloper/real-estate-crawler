FROM openjdk:11-jre-slim-buster
LABEL maintainer="vohoanglong07@gmail.com"
ADD target/admin-0.0.1-SNAPSHOT.jar admin.jar
ENTRYPOINT ["java","-jar","admin.jar"]