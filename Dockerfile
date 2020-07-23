FROM maven:3.6.3-openjdk-14

ENV MAVEN_OPTS -XX:+TieredCompilation -XX:TieredStopAtLevel=1

WORKDIR /source
COPY pom.xml .
RUN mvn --threads 1C --errors --batch-mode dependency:resolve-plugins dependency:go-offline
COPY src ./src

ENTRYPOINT [ "mvn", "--threads", "1C", "--errors", "--batch-mode" ]
