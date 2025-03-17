# Construir y levantar los contenedores
docker-compose up --build -d
FILAS=50
ESPERA=5

# Mostrar mensaje indicando que las pruebas van a comenzar
echo "Iniciando pruebas..."
# Esperar 5 segundos antes de detener el cliente
sleep $ESPERA

# Detener el contenedor del servidor
echo "⏸ Deteniendo el contenedor 'cliente'..."
docker stop cliente || { echo "Error: No se pudo detener el contenedor 'cliente'"; exit 1; }
# Esperar 5 segundos
sleep $ESPERA

# Iniciar nuevamente el contenedor del servidor
echo "▶ Iniciando el contenedor 'cliente'..."
docker start cliente || { echo "Error: No se pudo iniciar el contenedor 'cliente'"; exit 1; }
sleep $ESPERA
echo "-----------------------------------------"
echo "| log servidor:                         |"
docker logs --tail $FILAS servidor
echo "-----------------------------------------"
echo "| log cliente:                          |"
docker logs --tail $FILAS cliente
echo "-----------------------------------------"
# Mostrar mensaje indicando que las pruebas han terminado
echo "Pruebas completadas."