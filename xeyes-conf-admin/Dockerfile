FROM openjdk:8-jre-slim
MAINTAINER wyfish

ENV PARAMS=""

ADD target/xeyes-conf-admin-*.jar /app.jar

ENTRYPOINT ["sh","-c","java -jar /app.jar $PARAMS"]