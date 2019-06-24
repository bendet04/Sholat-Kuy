package id.bhinneka.sholatkuy.experimental

import android.os.Handler
import android.os.Looper
import kotlin.coroutines.*

private class AndroidContinuation<T>(val cont: Continuation<T>) : Continuation<T> by cont {

    fun resume(value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            cont.resume(value)
        } else {
            Handler(Looper.getMainLooper()).post {
                cont.resume(value)
            }
        }
    }

     fun resumeWithException(exception: Throwable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            cont.resumeWithException(exception)
        } else {
            Handler(Looper.getMainLooper()).post {
                cont.resumeWithException(exception)
            }
        }
    }
}

object Android: AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
            AndroidContinuation(continuation)
}
