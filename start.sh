#!/bin/bash
# Iniciar o serviço cron
service cron start

# Iniciar a aplicação Java
java -jar target/Integracao-1.0-SNAPSHOT-jar-with-dependencies.jar
