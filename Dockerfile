# syntax=docker/dockerfile:1

FROM testamento/maven-jdk21
COPY . .
RUN mvn verify