# Construir y levantar los contenedores
docker-compose up --build -d
FILAS=50
ESPERA=5

# Mostrar mensaje indicando que las pruebas van a comenzar
echo "Iniciando pruebas..."
# Esperar 5 segundos antes de detener el cliente
sleep $ESPERA

# Detener el contenedor del nodo1
echo "⏸ Deteniendo el contenedor 'nodo1'..."
docker stop nodo1 || { echo "Error: No se pudo detener el contenedor 'nodo1'"; exit 1; }
# Esperar 5 segundos
sleep $ESPERA

# Iniciar nuevamente el contenedor del nodo1
echo "▶ Iniciando el contenedor 'nodo1'..."
docker start nodo1 || { echo "Error: No se pudo iniciar el contenedor 'nodo1'"; exit 1; }
sleep $ESPERA
echo "-----------------------------------------"
echo "| log nodo1:                         |"
docker logs --tail $FILAS nodo1
echo "-----------------------------------------"
echo "| log nodo2:                          |"
docker logs --tail $FILAS nodo2
echo "-----------------------------------------"

echo "Iniciando pruebas con el otro nodo..."
# Esperar 5 segundos antes de detener el cliente
sleep $ESPERA

# Detener el contenedor del nodo2
echo "⏸ Deteniendo el contenedor 'nodo2'..."
docker stop nodo2 || { echo "Error: No se pudo detener el contenedor 'nodo2'"; exit 1; }
# Esperar 5 segundos
sleep $ESPERA

# Iniciar nuevamente el contenedor del nodo2
echo "▶ Iniciando el contenedor 'nodo2'..."
docker start nodo2 || { echo "Error: No se pudo iniciar el contenedor 'nodo2'"; exit 1; }
sleep $ESPERA
echo "-----------------------------------------"
echo "| log nodo1:                         |"
docker logs --tail $FILAS nodo1
echo "-----------------------------------------"
echo "| log nodo2:                          |"
docker logs --tail $FILAS nodo2
echo "-----------------------------------------"
# Mostrar mensaje indicando que las pruebas han terminado
echo "Pruebas completadas."