FROM openjdk:8-jdk-alpine

RUN mkdir /app
ADD entrypoint.sh entrypoint.sh
COPY target/restkeyvaluevault-*.jar app.jar
RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]