package com.contatosemergencia

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @GET("contatos")
    suspend fun getContatos(): Response<List<Contato>>
    
    @POST("contatos")
    suspend fun cadastrarContato(@Body contato: Contato): Response<Contato>
} 