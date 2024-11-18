#!/bin/bash
mvn clean package
echo '10 18 * * * ubuntu java -jar /usr/src/app/target/Integracao-1.0-SNAPSHOT-jar-with-dependencies.jar' > /etc/cron.d/mycron
chmod 755 /etc/cron.d/mycron
cat /etc/cron.d/mycron
crontab -l
chmod 755 target/Integracao-1.0-SNAPSHOT-jar-with-dependencies.jar
java -jar target/Integracao-1.0-SNAPSHOT-jar-with-dependencies.jar