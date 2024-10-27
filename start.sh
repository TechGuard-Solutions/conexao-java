#!/bin/bash
# Iniciar o serviço cron
service cron start

echo "0 16 * * * java -jar target/Integracao-1.0-SNAPSHOT-jar-with-dependencies.jar" > /etc/cron.d/mycron
crontab /etc/cron.d/mycron

# Iniciar a aplicação Java
java -jar target/Integracao-1.0-SNAPSHOT-jar-with-dependencies.jar