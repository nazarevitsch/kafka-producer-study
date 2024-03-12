FROM amazoncorretto:21-alpine-jdk
COPY ./service/build/libs/service-0.0.1-SNAPSHOT.jar .
CMD ["java","-jar","service-0.0.1-SNAPSHOT.jar"]