package uz.ttpu.weatherpatterns.mvp.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository
import uz.ttpu.weatherpatterns.mvp.WeatherContract
class WeatherPresenter(
    private val view: WeatherContract.View,
    private val repository: WeatherRepository,
    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main),
) {
    fun onCitySelected(city: String) {
        view.showLoading()
        scope.launch {
            repository.getWeather(city)
                .onSuccess { weather -> view.showWeather(weather) }
                .onFailure { error -> view.showError(error.message ?: "Unknown error") }
        }
    }
    fun destroy() {
        scope.cancel()
    }
}
