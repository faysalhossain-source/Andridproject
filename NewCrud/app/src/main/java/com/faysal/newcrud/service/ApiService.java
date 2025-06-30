package com.faysal.newcrud.service;







import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @GET("employees")
    Call<List<Employee>> getEmployees();

    @POST("employees")
    Call<Employee> addEmployee(@Body Employee employee);

    @PUT("employees/{id}")
    Call<Employee> updateEmployee(@Path("id") int id, @Body Employee employee);

    @DELETE("employees/{id}")
    Call<Void> deleteEmployee(@Path("id") int id);
}
