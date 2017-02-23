FROM tomcat:7

ADD https://services.gradle.org/distributions/gradle-3.4-bin.zip /opt

WORKDIR /opt

RUN apt-get update && apt-get install openjdk-7-jdk -y && \
    unzip gradle-3.4-bin.zip

ENV PATH=$PATH:/opt/gradle-3.4/bin
