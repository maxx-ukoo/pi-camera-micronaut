FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/pi-camera-micronaut*.jar pi-camera-micronaut.jar
CMD java ${JAVA_OPTS} -jar pi-camera-micronaut.jar