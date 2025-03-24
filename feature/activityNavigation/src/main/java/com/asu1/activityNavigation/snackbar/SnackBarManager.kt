import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

enum class ToastType(val prefix: String){
    SUCCESS(prefix = "S"),
    ERROR(prefix = "E"),
    INFO(prefix = "I")
}

object SnackBarManager {
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate) // Creates a scope for UI-related work

    private val _snackBarMessage = MutableSharedFlow<Pair<Int, ToastType>?>(replay = 0) // No replay
    val snackBarMessage = _snackBarMessage.asSharedFlow()
//    private val _toastMessage = MutableLiveData<Pair<Int, ToastType>?>()
//    val toastMessage: LiveData<Pair<Int, ToastType>?> = _toastMessage

    fun showSnackBar(message: Int, type: ToastType = ToastType.INFO) {
        coroutineScope.launch {
            _snackBarMessage.emit(Pair(message, type))
        }
    }

    fun snackBarShown() {
        coroutineScope.launch {
            _snackBarMessage.emit(null)
        }
    }
}