FROM gradle:7.6.0-jdk17-alpine AS build_step

RUN mkdir /project
COPY . /project
WORKDIR /project
RUN gradle build
CMD ["java", "-jar", "build/libs/tenpo-api-0.0.1-SNAPSHOT.jar"]

#FROM openjdk:17-jdk-slim-buster
#RUN mkdir /application
#COPY --from=build_step /project/target/tenpo-api-0.0.1-SNAPSHOT.jar /application
#WORKDIR /application
#CMD ["java", "-jar", "tenpo-api-0.0.1-SNAPSHOT.jar"]


#WORKDIR /app

#COPY app/build/lib/* build/lib/

#COPY app/build/libs/app.jar build/

#WORKDIR /app/build
#ENTRYPOINT java -jar app.jar