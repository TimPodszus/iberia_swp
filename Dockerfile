FROM mysql:oraclelinux9

# Kopieren Sie die SQL-Datei in das Docker-Image
COPY src/main/resources/db.migration/V1_Initial.sql /docker-entrypoint-initdb.d/

# Exponieren Sie den MySQL-Port
EXPOSE 3306
