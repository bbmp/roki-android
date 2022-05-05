package com.robam.roki.ui.helper3.http;

import com.robam.roki.ui.bean3.DeleteMultiItemBean;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface IRokiApiService {

    String deleteMultiItem = "/rest/cks/api/multi/delete/step/{id}";


    @DELETE(deleteMultiItem)
    Call<DeleteMultiItemBean> deleteMultiItemBean(@Path("id") Integer id, @Query("no") Integer no);
}
