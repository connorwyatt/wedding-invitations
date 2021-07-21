FROM --platform=linux/amd64 adoptopenjdk:16.0.1_9-jdk-hotspot AS builder

WORKDIR ./usr/src

COPY gradle ./gradle
COPY gradlew ./

RUN chmod u+x ./gradlew

COPY *.gradle.kts ./

RUN ./gradlew wrapper

COPY src ./src

ARG SEMVER
RUN ./gradlew build -Pversion=$SEMVER

FROM adoptopenjdk:16.0.1_9-jre-hotspot
WORKDIR ./usr/src
EXPOSE 80
ENV spring_profiles_active="production"

COPY --from=builder /usr/src/build/libs/invitations.jar ./

ENTRYPOINT ["java", "-jar", "./invitations.jar"]
