# Weather Patterns — Architectural Comparison in Android

This repository demonstrates and compares four fundamental Android architectural patterns (**MVC**, **MVP**, **MVVM**, and **MVI**) within a single cohesive Kotlin & Jetpack Compose application.

---

## 📁 Project Package Structure

```text
uz.ttpu.weatherpatterns
├── MainActivity.kt        # Launcher activity to open any of the 4 pattern screens
├── data/                  # Shared Data Layer
│   ├── api/               # WeatherApi & FakeWeatherApi
│   ├── model/             # Weather data class
│   └── repository/        # WeatherRepository
├── di/                    # Dependency Injection (WeatherModule & ViewModel Factories)
├── ui/                    # Shared Compose UI (WeatherScreen)
├── mvc/                   # Model-View-Controller
│   ├── model/             # WeatherModel
│   ├── view/              # WeatherView & MvcActivity
│   └── controller/        # WeatherController
├── mvp/                   # Model-View-Presenter
│   ├── WeatherContract    # View interface contract
│   ├── presenter/         # WeatherPresenter
│   └── view/              # MvpActivity
├── mvvm/                  # Model-View-ViewModel
│   ├── viewmodel/         # WeatherUiState & WeatherViewModel
│   └── view/              # MvvmActivity
└── mvi/                   # Model-View-Intent
    ├── model/             # WeatherState & WeatherIntent
    ├── viewmodel/         # WeatherMviViewModel
    └── view/              # MviActivity
```

---

## 📊 Architectural Pattern Comparison (Question 14)

| Pattern | Logic Holder | Imports `android.` | Imports `androidx.` | View Holds Reference To | Survives Rotation |
| :--- | :--- | :---: | :---: | :--- | :--- |
| **MVC** | `WeatherController` / `WeatherModel` | **No** | **No** | `WeatherController` & `WeatherModel` | **Nothing** |
| **MVP** | `WeatherPresenter` | **No** | **No** | `WeatherPresenter` | **Nothing** |
| **MVVM** | `WeatherViewModel` | **No** | **Yes** (`lifecycle`) | `WeatherViewModel` | **`WeatherViewModel`** |
| **MVI** | `WeatherMviViewModel` | **No** | **Yes** (`lifecycle`) | `WeatherMviViewModel` | **`WeatherMviViewModel`** |

### Testing Requirements Summary
- **MVC**: Controller & Model unit tests run locally on JVM with no extra test libraries (`runBlocking`).
- **MVP**: Presenter unit tests run locally on JVM with no extra test libraries (by passing `CoroutineScope(Dispatchers.Unconfined)` into constructor).
- **MVVM**: ViewModel unit tests require `kotlinx-coroutines-test` (`Dispatchers.setMain(UnconfinedTestDispatcher())`).
- **MVI**: ViewModel unit tests require `kotlinx-coroutines-test` (`Dispatchers.setMain(UnconfinedTestDispatcher())`).

---

## ❓ Conceptual Q&A Summary

### Question 6 — MVC Concept
- **Why does `render()` take no parameters in classic MVC?**
  In classic MVC, the View holds a direct reference to the Model and pulls state directly from it whenever asked to re-render.
- **What changes if Controller passes data into View instead?**
  Passing data directly would make the View passive and turn the Controller into a mediator, transforming the architecture from MVC to MVP.

### Question 9 — MVP Rotation & Memory Leaks
- **Before Fix**: Rotating destroyed Activity #1, but Presenter #1 kept running its coroutine and invoked `showWeather` on destroyed Activity #1.
- **Fix**: Added `destroy()` to Presenter (`scope.cancel()`) and invoked it from `MvpActivity.onDestroy()`.
- **After Rotation**: Status line resets to `"Pick a city"` because the old Presenter and coroutine were cancelled, and the new Activity creates a fresh Presenter.

### Question 11 — MVVM Rotation State Retention
- **When tapping a city and rotating**: `WeatherViewModel` survives rotation via `ViewModelStore`. The in-flight request continues in `viewModelScope` and smoothly updates `uiState` on the recreated `MvvmActivity`. The request is **not lost**.

---

## 🧪 Running Unit Tests

Execute all 9 unit tests from the command line:

```bash
./gradlew testDebugUnitTest
```
