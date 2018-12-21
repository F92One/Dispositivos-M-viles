# Dispositivos-Moviles

EN DESARROLLO

El objetivo es contar con 5 funcionalidades en una aplicación.
La primera muestra un saludo al oprimir un botón; la segunda contiene dos activities, una de ellas permite obtener los datosde contacto y un botón para lanzar la segunda  activty, que mostrará el resultado de una operación. Ambas mantienen los datos al girar el dispositivo.
La tercera implementa un segundero con 3 botones, uno para inicar, uno para parar y otro para reseatear. Se acumula con AsincTask y se utiliza SharedPreferences para guardar el resultado al abrir y cerrar.
La cuarta implementa dos servicios en background, uno extiende de Service y otro de Intent Service. Utiliza Broadcast Receiver para interactuar con la UI, que inicia y muestra 4 invocaciones a cada uno.
La quinta es una implementación de Bound Service que usa la actividad anterior desde una nueva app. Retorna un número aleatorio del 0 al 100 y lo muestra en la app anterior.
