FROM gradle:7.6.0-jdk17-alpine

RUN mkdir /project
COPY . /project
WORKDIR /project
RUN gradle build -x test
CMD ["java", "-jar", "build/libs/tenpo-api-0.0.1-SNAPSHOT.jar"]
