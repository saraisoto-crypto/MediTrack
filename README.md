# 💊 MediTrack

Aplicación Android de seguimiento y recordatorio de medicamentos desarrollada con Kotlin y Jetpack Compose.

## 📱 Pantallas

| Login | Registro | Lista | Detalle | Formulario | Frases |
|-------|----------|-------|---------|------------|--------|
| Inicio de sesión con Firebase Auth | Crear cuenta con validación | Hero morado + filtros + lista | Banner con gradiente por categoría | Secciones visuales | Pantalla oscura editorial |

## 🚀 Funcionalidades

### Parte 1 — Entrega Semana 14
- ✅ CRUD completo de medicamentos (nombre, dosis, frecuencia, hora, categoría, notas)
- ✅ Persistencia local con Room Database 2.7.1
- ✅ Pantalla de frases motivacionales con API propia en español
- ✅ Navegación con Jetpack Navigation Compose y paso de argumentos
- ✅ Arquitectura MVVM con StateFlow y UiState por pantalla
- ✅ Filtro de lista por categoría (Analgésico, Antibiótico, Vitamina, etc.)

### Parte 2 — Entrega Semana 15
- ✅ Autenticación con Firebase Auth (registro, login, sesión persistente, cierre de sesión)
- ✅ Sincronización en la nube con Cloud Firestore (datos separados por UID de usuario)
- ✅ API propia de frases en Node.js + Express desplegada en Render
- ✅ Notificaciones locales automáticas con AlarmManager (suenan a la hora exacta del medicamento sin tocar nada)
- ✅ Notificaciones push vía servidor con Firebase Cloud Messaging (FCM)
- ✅ Componentes reutilizables en `ui/components/`

## 🔔 Cómo funcionan las notificaciones

### Notificación local automática
Al registrar un medicamento con hora `20:00`, `AlarmScheduler` programa una alarma con `AlarmManager.setExactAndAllowWhileIdle()`. Cuando llega esa hora, Android despierta `AlarmReceiver` (aunque la app esté cerrada) y muestra la notificación automáticamente. No requiere internet.

### Notificación push vía servidor
`MediTrackFirebaseService` extiende `FirebaseMessagingService`. Los mensajes se envían desde Firebase Console → Cloud Messaging y llegan al dispositivo mediante los servidores de Google, tanto si la app está abierta como cerrada.

## 🛠️ Stack tecnológico

| Capa | Tecnología | Versión |
|------|-----------|---------|
| Lenguaje | Kotlin | 2.2.10 |
| UI | Jetpack Compose + Material 3 | BOM 2024 |
| Arquitectura | MVVM + Repository Pattern | — |
| Base de datos local | Room | 2.7.1 + KSP |
| Red | Retrofit + Gson | 2.11 |
| Autenticación | Firebase Auth | BOM 33.7.0 |
| Base de datos nube | Cloud Firestore | BOM 33.7.0 |
| Notificaciones locales | AlarmManager + BroadcastReceiver | — |
| Notificaciones push | Firebase Cloud Messaging | BOM 33.7.0 |
| API de frases | Node.js + Express en Render | 1.0.0 |
| Build | AGP 9.1.1 + Gradle 9.3.1 | — |

## 🏗️ Estructura del proyecto

```
app/src/main/java/com/sarai/meditrack/
├── MainActivity.kt
├── MediTrackApp.kt
├── data/
│   ├── local/
│   │   ├── Medication.kt          ← Entidad Room
│   │   ├── MedicationDao.kt       ← DAO con CRUD
│   │   └── MedicationDatabase.kt  ← Base de datos
│   ├── remote/
│   │   ├── QuoteApiService.kt     ← Retrofit interface
│   │   ├── QuoteResponse.kt       ← Modelo de respuesta
│   │   └── RetrofitInstance.kt    ← Cliente HTTP
│   └── repository/
│       └── MedicationRepository.kt ← Room + Firestore + Alarmas
├── viewmodel/
│   ├── AuthViewModel.kt           ← Firebase Auth
│   ├── MedicationListViewModel.kt ← Lista + filtro por categoría
│   ├── MedicationFormViewModel.kt ← Crear / editar
│   └── QuoteViewModel.kt          ← Frases motivacionales
├── notifications/
│   ├── NotificationHelper.kt      ← Canal y envío de notificaciones
│   ├── AlarmScheduler.kt          ← Programa alarmas con AlarmManager
│   ├── AlarmReceiver.kt           ← BroadcastReceiver de alarmas
│   └── MediTrackFirebaseService.kt ← Servicio FCM push
└── ui/
    ├── screens/
    │   ├── LoginScreen.kt
    │   ├── RegisterScreen.kt
    │   ├── MedicationListScreen.kt
    │   ├── MedicationDetailScreen.kt
    │   ├── MedicationFormScreen.kt
    │   └── QuoteScreen.kt
    ├── components/
    │   ├── MedicationCard.kt
    │   └── CommonComponents.kt
    ├── navigation/
    │   └── AppNavigation.kt
    └── theme/
        ├── Color.kt
        ├── Type.kt
        └── Theme.kt
```

## 🌐 API propia de frases

```
GET https://meditrack-quotes-api.onrender.com/api/phrase
```

```json
{
  "phrase": "Cuidar tu salud hoy es la mejor inversión para tu mañana.",
  "author": "Anónimo"
}
```

Repositorio: [meditrack-quotes-api](https://github.com/saraisoto-crypto/meditrack-quotes-api)

## 🔥 Estructura Firestore

```
users/
  {uid}/
    medications/
      {medicationId}/
        name, dose, frequency, time, category, notes
```

Cada usuario solo ve sus propios medicamentos gracias al UID de Firebase Auth.

## ⚙️ Configuración para ejecutar

1. Clona el repositorio
2. Agrega tu propio `google-services.json` en `app/`
3. Activa en Firebase Console:
    - Authentication → Correo/Contraseña
    - Firestore Database → Modo prueba
4. Compila y ejecuta en Android Studio

## 🎯 Demo de sustentación

1. Abrir la app → acepta permiso de notificaciones
2. Registrar con un correo → ir al Login → ingresar credenciales
3. Crear medicamento con hora 2 minutos adelante → esperar → llega notificación sola
4. Firebase Console → Cloud Messaging → enviar notificación push → llega al dispositivo
5. Mostrar Firestore → los medicamentos aparecen bajo `users/{uid}/medications`
6. Cerrar sesión → entrar con otro usuario → lista vacía (datos separados por UID)

## 👩‍💻 Autora

**Saraí Sthefanie Soto López**
Programación en Móviles — Tecsup — Junio 2026
GitHub: [saraisoto-crypto](https://github.com/saraisoto-crypto)