# 🔗 **Sistema Distribuido con Comunicación TCP**

## 📌 **Descripción General**
Este repositorio contiene la implementación de un **sistema distribuido** basado en la comunicación TCP, donde los nodos interactúan utilizando un **modelo cliente-servidor**. A lo largo de distintos **HITs**, se han ido incorporando nuevas funcionalidades para mejorar la **resiliencia**, **escalabilidad** y **organización de la comunicación** entre los nodos.

El desarrollo sigue una estructura incremental, donde cada **release** incorpora nuevas características y mejoras, detalladas en los registros de cambios.

---

## 🚀 **Progresión de Funcionalidades**
El desarrollo del sistema se ha realizado a través de distintos **HITs**, cada uno agregando mejoras y funcionalidades. Podés encontrar la implementación de cada **HIT** en las diferentes **releases** del repositorio.

## 📜 **Notas de Release**
Cada **HIT** cuenta con su propia **nota de release**, donde se detallan los cambios implementados, instrucciones de ejecución y consideraciones adicionales.
Para más información, consulta cada **release** en los siguientes enlaces:

### 🔹 [**HIT #1 – Comunicación Cliente-Servidor**](https://github.com/SebaJuarez/ComunicacionTCP/releases/tag/hit1)
- Implementación de un **servidor TCP (B)** que espera conexiones.
- Creación de un **cliente TCP (A)** que se conecta y envía un saludo.

### 🔹 [**HIT #2 y #3 – Manejo de Reconexiones**](https://github.com/SebaJuarez/ComunicacionTCP/releases/tag/hit2-3)
- **Cliente (A)** intenta reconectarse automáticamente si el servidor **(B)** cierra la conexión.
- **Servidor (B)** permanece activo y acepta nuevas conexiones si el cliente **(A)** se desconecta.

### 🔹 [**HIT #4 – Unificación Cliente-Servidor**](https://github.com/SebaJuarez/ComunicacionTCP/releases/tag/hit4)
- Se reemplazan **A y B** por un único programa **C**, que funciona **simultáneamente** como cliente y servidor.

### 🔹 [**HIT #5 – Serialización de Mensajes**](https://github.com/SebaJuarez/ComunicacionTCP/releases/tag/hit5)
- Implementación de mensajes en **formato JSON**, utilizando una librería de serialización.

### 🔹 [**HIT #6 – Registro de Contactos (Nodo D)**](https://github.com/SebaJuarez/ComunicacionTCP/releases/tag/hit6)
- Introducción de un nuevo nodo **D**, que mantiene un registro de nodos **C** activos.  
- **C** ya no necesita conocer las IPs de sus pares de antemano.  
- **D** responde a cada **C** con la lista de nodos activos para que establezcan comunicación.

### 🔹 [**HIT #7 – Sistema de Inscripciones en Ventanas de Tiempo**](https://github.com/SebaJuarez/ComunicacionTCP/releases/tag/hit7)
- Implementación de **ventanas de inscripciones** de **1 minuto**, coordinadas por **D**.  
- Los nodos **C** deben registrarse en **D** para la próxima ventana de tiempo.  
- **D** mueve automáticamente las inscripciones cada 60 segundos.  
- Se persisten los registros en **JSON** para auditoría y seguimiento.
