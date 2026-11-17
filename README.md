# Gestor Financiero Digital (GFD)

Aplicación móvil nativa para Android enfocada en la administración de presupuestos personales, control de gastos por categorías y exportación de reportes financieros en tiempo real, bajo una interfaz estilo Fintech.

---

## Ficha del Proyecto

* **Nombre del Proyecto:** Gestor Financiero Digital (GFD)
* **Asignatura:** Programación Android (IEI-093) — Unidad II
* **Institución:** Instituto Profesional Santo Tomás
* **Arquitectura:** MVVM (Model - View - ViewModel)
* **Persistencia:** Room Database (SQLite local)

---

## Stack Tecnológico

* **Lenguaje:** Java (JDK 17)
* **SDK / UI:** Android SDK, Material Design Components (`CardView`, `OutlinedBox`, `MaterialButton`), `ConstraintLayout`, `RecyclerView`
* **Arquitectura y Reactividad:** `ViewModel`, `LiveData`
* **Base de Datos Local:** AndroidX Room (`@Entity`, `@Dao`, `@Database` con Patrón Singleton y `ON DELETE CASCADE`)
* **Autenticación y Hardware:** AndroidX `BiometricPrompt` (Sensor de huella dactilar) + Google Sign-In (`play-services-auth`) + `SharedPreferences`
* **Exportación:** Intents implícitos nativos (`Intent.ACTION_SEND`)

---

## Medidas de Seguridad Implementadas

1. **Autenticación Híbrida (Google + Biometría):** Vinculación inicial mediante cuenta de Google persistida localmente y bloqueo obligatorio mediante sensor biométrico (`BiometricPrompt`) en cada apertura de la bóveda.
2. **Prevención de Capturas de Pantalla (`FLAG_SECURE`):** Bloqueo de capturas y grabación de pantalla en las actividades que exhiben saldos, sueldos base y presupuestos (`MenuPrincipalActivity` y `MainActivity`).
3. **Aislamiento de Componentes (`android:exported="false"`):** Restricción explícita en `AndroidManifest.xml` para impedir que aplicaciones externas invoquen pantallas internas saltándose el flujo de autenticación.
4. **Prevención de Inyección SQL:** Uso exclusivo de consultas parametrizadas mediante anotaciones `@Query` en `CategoriaDao` (Room Database), evitando concatenación directa de cadenas SQL.

---

## Estructura del Proyecto

```text
app/src/main/java/com/example/gfd/
├── model/          # Entidades Room (Presupuesto, Categoria), CategoriaDao y AppDatabase (Singleton)
├── viewmodel/      # CategoriaViewModel con LiveData para actualización reactiva de saldos
├── adapter/        # CategoriaAdapter para renderizado en RecyclerView
└── view/           # LoginActivity, MenuPrincipalActivity y MainActivity
diagramas/
├── diagrama_mer_bd.png             # Modelo Entidad-Relación físico (SQLite / Room)
├── diagrama_arquitectura_mvvm.png  # Diagrama de Sistemas por capas (MVVM)
└── diagrama_flujo_actividades.png  # Diagrama de flujo UI e Intents