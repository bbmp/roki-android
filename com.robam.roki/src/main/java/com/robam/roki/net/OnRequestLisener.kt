
package com.robam.roki.net
import java.util.*

interface OnRequestListener {
    fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?)
    fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?)
    fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?)
}
