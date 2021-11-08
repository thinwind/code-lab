FROM debian:stable

# Install Java 8
ADD jre-8u301-linux-x64.tar.gz /opt

# Link default java to actual java version
RUN ln -s $(find /opt -name jre*) /opt/java && \
    ln -s /opt/java/bin/java /usr/bin/java && \

# Clean installation form unused librairies
    rm -rf  /opt/java/lib/plugin.jar \
            /opt/java/lib/ext/jfxrt.jar \
            /opt/java/bin/javaws \
            /opt/java/lib/javaws.jar \
            /opt/java/lib/desktop \
            /opt/java/plugin \
            /opt/java/lib/deploy* \
            /opt/java/lib/*javafx* \
            /opt/java/lib/*jfx* \
            /opt/java/lib/amd64/libdecora_sse.so \
            /opt/java/lib/amd64/libprism_*.so \
            /opt/java/lib/amd64/libfxplugins.so \
            /opt/java/lib/amd64/libglass.so \
            /opt/java/lib/amd64/libgstreamer-lite.so \
            /opt/java/lib/amd64/libjavafx*.so \
            /opt/java/lib/amd64/libjfx*.so 

# Set JAVA ENV vars
ENV JAVA_HOME /opt/java
ENV PATH /opt/java/bin:$PATH
