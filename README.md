# JavipaurFit
Proyecto 2016


Para contruir este proyecto hemos utilizado 3 fases de construccion:
1-Andorid-Google Fit
2-Parse
3-Página web con Plotly.js

Configuracion de Google fit.

  Hemos partido desde la aplicacion de muestra de Google Fit History Api.

Pre-requisitos

  Nivel API Android> 9
  Android Construir Herramientas v23
  Android Repositorio Soporte
  Registrar Proyecto Google con un cliente de Android por obtener instrucciones iniciadas           http://developers.google.com/fit/android/get-started

Empezando

  Este ejemplo utiliza el sistema de construcción Gradle. Para construir este proyecto, utilice el comando "build gradlew" o use "Proyecto de importación" en Android Studio.
  
  NOTA: Debe registrar un cliente de Android por debajo de un Proyecto de Google para que la API de Google Fit que estén disponibles para su aplicación. El proceso asegura que su aplicación tiene la información adecuada pantalla consentimiento para que los usuarios aceptan, entre otras cosas necesarias para acceder a las API de Google. Lee las instrucciones para más detalles: http://developers.google.com/fit/android/get-started

Apoyo

  El problema más común el uso de estas muestras es una excepción SIGN_IN_FAILED. Los usuarios pueden experimentar esto después de seleccionar una cuenta de Google para conectarse a la API de FIT. Si ve el siguiente en la salida Logcat a continuación, asegúrese de registrar su aplicación para Android por debajo de un Proyecto de Google como se indica en las instrucciones para el uso de esta muestra en: http://developers.google.com/fit/android/get-started
  
  10-26 14: 40: 37,082 1858-2370 /? E / MDM: [138] b.run: No se pudo conectar con el cliente API de Google: ConnectionResult {statuscode = API_UNAVAILABLE, resolución = null, null} message =

Utilice los siguientes canales de apoyo:

  Google+ Comunidad: https://plus.google.com/communities/103314459667402704958
  Desbordamiento de pila: http://stackoverflow.com/questions/tagged/android
  Si has encontrado un error en esta muestra, por favor presentar un problema: https://github.com/googlesamples/android-fitness/issues
