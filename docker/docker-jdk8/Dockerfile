FROM debian:stable

# Install Java 8
ADD jdk-8u301-linux-x64.tar.gz /opt

RUN ls /opt

# Link default java to actual java version
RUN ln -s $(find /opt -maxdepth 1 -name jdk*) /opt/java && \
    ln -s /opt/java/bin/java /usr/bin/java && \
    ln -s /opt/java/bin/javac /usr/bin/javac 

# Set JAVA ENV vars
ENV JAVA_HOME /opt/java
ENV PATH /opt/java/bin:$PATH
