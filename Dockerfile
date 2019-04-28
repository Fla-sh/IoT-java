FROM java:8
WORKDIR /home/piotr/Documents/java/IoT-java
RUN pwd
ADD target/IoT-java-1.0-SNAPSHOT-jar-with-dependencies.jar .
RUN java -jar IoT-java-1.0-SNAPSHOT-jar-with-dependencies.jar
