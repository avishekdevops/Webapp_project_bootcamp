FROM tomcat:8.0.51-jre8-alpine

EXPOSE 9095

COPY /target/javawebappbootcamp.war /usr/local/tomcat/webapps/javawebapp.war

CMD ["catalina.sh","run"]

MAINTAINER "Sourav"
