FROM openjdk:8-jdk-alpine AS builder
MAINTAINER Thomas Pham <thomas.pham@ithings.ch>

WORKDIR /build
RUN apk --update --no-cache add \
    maven

COPY . /build
RUN mvn clean package

FROM openjdk:8-jre-alpine AS image

RUN apk --update --no-cache add \
    ca-certificates curl

ENV DOCKERIZE_VERSION v0.6.1
RUN curl -jksSLO https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

WORKDIR /usr/share/service
ENTRYPOINT ["./entrypoint.sh"]

ADD entrypoint.sh .
# Add Maven dependencies (not shaded into the artifact; Docker-cached)
COPY --from=builder /build/target/libs  libs


COPY --from=builder /build/target/selin*.jar app.jar
