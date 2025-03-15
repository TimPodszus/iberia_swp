FROM mysql:oraclelinux9

COPY server/src/main/resources/db.migration/V1_Initial.sql /docker-entrypoint-initdb.d/

EXPOSE 3306




