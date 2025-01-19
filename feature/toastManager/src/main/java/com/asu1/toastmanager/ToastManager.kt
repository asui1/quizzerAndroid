import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

enum class ToastType{
    SUCCESS,
    ERROR,
    INFO
}

object ToastManager {
    private val _toastMessage = MutableLiveData<Pair<Int, ToastType>?>()
    val toastMessage: LiveData<Pair<Int, ToastType>?> = _toastMessage

    fun showToast(message: Int, type: ToastType = ToastType.INFO) {
        _toastMessage.postValue(Pair(message, type))
    }

    fun toastShown() {
        _toastMessage.postValue(null)
    }
}