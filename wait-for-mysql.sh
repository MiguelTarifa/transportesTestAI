#!/bin/sh
echo "Esperando a que MySQL esté listo..."
while ! nc -z mysql 3306; do
  sleep 1
done
echo "MySQL está listo. Iniciando aplicación."
exec "$@"
