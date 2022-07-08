package com.robam.roki.request.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class FlavouringBean(var msg:String="",var rc:String="0",var payload:ArrayList<FlavouringArray>) {





}


class FlavouringArray() :Serializable {
    var id:Int ?= null
    var code = 0
    var name: String? = null
    var materialSubCatalog: MaterialSubCatalog? = null
    var position = 0
    var image: String? = null
    var itemId: String? = null
    var orderImage: String? = null
    var orderProvider: String? = null
    var orderUrl: String? = null
    var originPrice: String? = null
    var price: String? = null
    var units: ArrayList<Units>? = ArrayList()
    var desc: String? = null



}

class Units() :Serializable {
    var id = 0
    var name: String? = null
    var type = 0
    override fun toString(): String {
        return "Units(id=$id, name=$name, type=$type)"
    }


}
class MaterialSubCatalog() :Serializable {
    var id: Long = 0
    var name: String? = null
    var materialCatalog: MaterialCatalog? = null
    var description: String? = null
    var position = 0



}

class MaterialCatalog:Serializable {
    var id = 0
    var name: String? = null
    var description: String? = null
    var position = 0
    override fun toString(): String {
        return "MaterialCatalog(id=$id, name=$name, description=$description, position=$position)"
    }


}