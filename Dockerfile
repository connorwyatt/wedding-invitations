FROM --platform=linux/amd64 adoptopenjdk:16.0.1_9-jdk-hotspot AS builder

WORKDIR /usr/src

COPY gradle ./gradle
COPY gradlew ./

RUN chmod u+x ./gradlew

COPY *.gradle.kts ./

RUN ./gradlew wrapper

COPY src ./src

ARG SEMVER
ARG POSTGRES_HOST=localhost
ARG POSTGRES_PORT=5433
ARG POSTGRES_DB=wedding-invitations
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_DB}
RUN ./gradlew build -Pversion=$SEMVER

FROM adoptopenjdk:16.0.1_9-jre-hotspot
WORKDIR /usr/src
EXPOSE 80
ENV SPRING_PROFILES_ACTIVE="production"

COPY --from=builder /usr/src/build/libs/invitations.jar ./

ENTRYPOINT ["java", "-jar", "./invitations.jar"]
