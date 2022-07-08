package com.robam.roki.net

interface  OnRequestCallback {
    fun onFailure(paramInt1: Int, paramInt2: Int, paramString1: String?, paramObject: Any?) {}
    fun onSuccess(paramInt1: Int, paramInt2: Int, paramObject: Any?) {}
}