# Pastelería Mil Sabores - App Android (MVVM + Compose)

Este módulo Android replica partes del proyecto web usando Jetpack Compose, arquitectura MVVM y navegación con barra inferior.

## Qué incluye
- Pantallas:
  - Productos: listado con búsqueda, precios en CLP y miniaturas desde assets.
  - Noticias: listado con imagen, título y extracto.
- MVVM:
  - domain/model: `Product`, `NewsItem`.
  - data/repository: `AssetsProductRepository`, `AssetsNewsRepository` (leen JSON en `app/src/main/assets`).
  - ui/**: `ProductsViewModel`, `NewsViewModel` + pantallas `ProductsScreen`, `NewsScreen`.
- Navegación: `navigation/AppNavHost.kt` con barra inferior (Productos/Noticias).
- Imágenes con Coil: carga desde assets (`file:///android_asset/...`).

## Estructura de assets
```
app/src/main/assets/
  products.json        # catálogo de productos
  news.json            # entradas de noticias
  products/*.jpg       # imágenes de productos
  news/*.{jpg,png}     # imágenes de noticias
```
- Las rutas de imagen dentro de los JSON son relativas a `assets/` (por ejemplo `products/TC001.jpg`).

## Ejecutar
Requisitos: Android SDK configurado.

Desde Windows (cmd):
```
cd app/../Prueba2
gradlew.bat assembleDebug
```
Opcional (instalar con ADB):
```
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Extender
- Productos:
  - Agrega más items en `assets/products.json` y copia las imágenes a `assets/products/`.
  - Si deseas filtros por categoría/precio, añade estado en `ProductsViewModel` y controles en `ProductsScreen`.
- Noticias:
  - Agrega entradas en `assets/news.json` y sus imágenes en `assets/news/`.
- Más secciones (cart/checkout/faqs/locations/reviews):
  - Exporta los datos desde `web/pasteleriamilsabores-react/src/data/*.js` a JSON en `assets/` y crea su MVVM + pantallas.

## Notas
- El tema Material 3 se encuentra en `ui/theme`.
- Las dependencias (Compose, Navigation, Coil, Lifecycle) están gestionadas con `gradle/libs.versions.toml`.
- Si cambias dependencias, ejecuta una sincronización/compilación para actualizar el IDE.

