package com.robam.roki.net;


import java.lang.reflect.Type;
import java.util.Map;

public  interface Connector
{
    int doPost(int requestCode,  String url, Object paramClass, Class<?> paramType,
                               OnRequestCallback paramOnRequestCallback, Class<?> responseInfoClass);


    int doGet(int requestCode,  String url, Map<String,Object> request, Class<?> paramType,
               OnRequestCallback OnRequestCallback, Class<?> responseInfoClass);


    int doFilePost(int requestCode, String url, Map<String,Object> request,Class<?> paramType,
                   OnRequestCallback mOnRequestCallback, Class<?> responseInfoClass);

    int doDel(int requestCode,  String url, Map<String,Object> request, Class<?> paramType,
              OnRequestCallback OnRequestCallback, Class<?> responseInfoClass);
}

