FROM java:8
MAINTAINER wyfish

ENV PARAMS=""

ADD target/xeyes-conf-admin-*.jar /app.jar

ENTRYPOINT ["sh","-c","java -jar /app.jar $PARAMS"]