package com.faysal.jobkhujibd2.service;


import androidx.activity.EdgeToEdge;


import com.faysal.jobkhujibd2.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/users")
    Call<User> saveUser(@Body User user);

    @GET("api/users")
    Call<List<User>> getAllUsers();

    @GET("user/{id}")
    Call<User> getUserById(@Path("id") int id);

    @PUT("user/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);

    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Path("id") int id);
}
