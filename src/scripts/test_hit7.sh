# Configuración
FILAS=200
ESPERA=10  # Ajustado para el ciclo de actualización de ventanas (20s)

echo "🔄 Construyendo y levantando los contenedores..."
docker-compose up --build -d
sleep $ESPERA

echo "✅ Contenedores levantados. Comenzando pruebas..."

# Mostrar logs iniciales
echo "📜 Logs iniciales:"
echo "-----------------------------------------"
echo "| log registro_contactos:               |"
docker logs --tail $FILAS registro_contactos
echo "-----------------------------------------"
echo "| log nodo1:                            |"
docker logs --tail $FILAS nodo1
echo "-----------------------------------------"
echo "| log nodo2:                            |"
docker logs --tail $FILAS nodo2
echo "-----------------------------------------"
echo "| log nodo3:                            |"
docker logs --tail $FILAS nodo3
echo "-----------------------------------------"

sleep $ESPERA

echo "⏸ Deteniendo 'nodo1' y 'nodo2'..."
docker stop nodo1 || { echo "Error: No se pudo detener 'nodo1'"; exit 1; }
docker stop nodo2 || { echo "Error: No se pudo detener 'nodo2'"; exit 1; }

sleep $ESPERA
echo "▶ Iniciando 'nodo1' y 'nodo2'..."
docker start nodo1 || { echo "Error: No se pudo iniciar 'nodo1'"; exit 1; }
docker start nodo2 || { echo "Error: No se pudo iniciar 'nodo2'"; exit 1; }
sleep $ESPERA

# Mostrar logs después de reiniciar nodos
echo "📜 Logs después de reiniciar nodo1 y nodo2:"
echo "-----------------------------------------"
echo "| log registro_contactos:               |"
docker logs --tail 6 registro_contactos
echo "-----------------------------------------"
echo "| log nodo1:                            |"
docker logs --tail $FILAS nodo1
echo "-----------------------------------------"
echo "| log nodo2:                            |"
docker logs --tail $FILAS nodo2
echo "-----------------------------------------"
sleep 21
# Verificar nuevamente el archivo de inscripciones
echo "📂 Verificando contenido del archivo de inscripciones..."
cat ../../logs-registros/inscripciones.json || echo "⚠ No se encontró el archivo de inscripciones."
echo " "
echo "✅ Pruebas completadas."
sleep $ESPERA
docker-compose down
