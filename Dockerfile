FROM gradle:5.3.0-jdk8-alpine as builder
COPY . .
USER root
RUN gradle bootJar

FROM openjdk:8-jre-alpine
COPY --from=builder /home/gradle/build/libs/jenhub.jar .
CMD ["java", "-jar", "jenhub.jar"]
