FROM openjdk:11-jdk-slim as build
WORKDIR /src

COPY ["Services/myproject/mvnw", "Services/myproject/"]
COPY ["Services/myproject/.mvn", "Services/myproject/.mvn"]
COPY ["Services/myproject/pom.xml", "Services/myproject/"]
COPY ["Services/myproject/src", "Services/myproject/src"]

WORKDIR /src/Services/myproject
RUN chmod +x ./mvnw
RUN ./mvnw install -DskipTests
RUN mkdir -p target

FROM openjdk:16-jdk-slim
VOLUME /tmp
ARG DEPENDENCY=/src/Services/myproject/target
COPY --from=build ${DEPENDENCY}/myproject-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]