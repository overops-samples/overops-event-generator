FROM centos:7

LABEL maintainer="tjveil@gmail.com"

ARG PROJECT_DIR=/overops-event-generator

# install java and update OS
RUN yum install -y java-1.8.0-openjdk-devel.x86_64 \
    && yum -y update \
    && yum clean all \
    && mkdir $PROJECT_DIR

ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk
ENV PATH $JAVA_HOME/bin:$PATH

# copy source code to container
ADD .mvn $PROJECT_DIR/.mvn
ADD mvnw $PROJECT_DIR
ADD mvnw.cmd $PROJECT_DIR
ADD src $PROJECT_DIR/src
ADD pom.xml $PROJECT_DIR

# fixing a wierd issue i had running from windows
RUN sed -i 's/\r$//' $PROJECT_DIR/mvnw && chmod a+x $PROJECT_DIR/mvnw

WORKDIR $PROJECT_DIR

# package generator
RUN ./mvnw clean package -DskipTests \
    && ./mvnw dependency:copy-dependencies -Dsources.dir=/sources

# port for embedded Jetty
EXPOSE 8080

# mount sources as volume
VOLUME /sources

ADD libsentry_agent_linux-x86_64.so $PROJECT_DIR

ENTRYPOINT java -agentpath:libsentry_agent_linux -Dtakipi.sources.path=/sources -jar target/*.jar
