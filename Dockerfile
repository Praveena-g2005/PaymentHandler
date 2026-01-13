FROM maven:3.8.8-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM tomcat:9.0-jre11
ENV CATALINA_HOME=/usr/local/tomcat
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Dfile.encoding=UTF-8"
RUN rm -rf $CATALINA_HOME/webapps/*
COPY --from=build /app/target/paymenthandler.war $CATALINA_HOME/webapps/paymenthandler.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
