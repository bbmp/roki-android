package com.robam.roki.request.bean

class TestBean(var rc:String,var msg:String,var cookbook:Cookbook) {



    class Cookbook(var id:String,var name:String,var cookbookType:String,var introduction:String,var needTime:String,var imgSmall:String,var imgMedium:String){
        override fun toString(): String {
            return "Cookbook(id='$id', name='$name', cookbookType='$cookbookType', introduction='$introduction', needTime='$needTime', imgSmall='$imgSmall', imgMedium='$imgMedium')"
        }
    }

    override fun toString(): String {
        return "TestBean(rc='$rc', msg='$msg', cookbook=$cookbook)"
    }
}