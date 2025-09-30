#!/bin/bash
set -e
set -u

function create_user_and_database() {
  local database=$1
  local password=$2
  echo "  Creating user and database '$database'"

  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL
    DO
    \$do\$
    BEGIN
       IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '$database') THEN
          CREATE USER "$database" WITH PASSWORD '$password';
       END IF;
    END
    \$do\$;

    CREATE DATABASE "$database" OWNER "$database";

    \connect "$database";
    GRANT ALL ON SCHEMA public TO "$database";
    ALTER SCHEMA public OWNER TO "$database";
EOSQL
}

if [ -n "${POSTGRES_MULTIPLE_DATABASES:-}" ]; then
  echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
  for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
    create_user_and_database "$db" "$db"
  done
  echo "Multiple databases created"
fi
