#!/bin/bash

set -e
set -u

function create_user_and_database() {
  local database=$1
  local password=$2
  echo "  Creating user and database '$database'"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL
    CREATE USER $database WITH PASSWORD '$password';
    CREATE DATABASE $database;
    GRANT ALL PRIVILEGES ON DATABASE $database TO $database;

    \connect $database;
    GRANT ALL ON SCHEMA public TO $database;
    ALTER SCHEMA public OWNER TO $database;
EOSQL
}

if [ -n "${POSTGRES_MULTIPLE_DATABASES:-}" ]; then
  echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
  for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
    create_user_and_database $db "$db"
  done
  echo "Multiple databases created"
fi
