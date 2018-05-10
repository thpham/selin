#!/bin/sh

run () {
  exec dockerize -wait tcp://${POSTGRESQL_SERVER:-'postgres'}:${POSTGRESQL_PORT:-5432} -timeout 100s \
    java -Dspring.profiles.active=docker -jar app.jar
}


case "$1" in
  *)
    echo "Starting the service"
    run
    ;;
esac
