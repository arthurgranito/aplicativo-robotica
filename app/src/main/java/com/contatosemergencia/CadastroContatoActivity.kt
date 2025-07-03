package com.contatosemergencia

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CadastroContatoActivity : AppCompatActivity() {
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var etNome: TextInputEditText
    private lateinit var etTelefone: TextInputEditText
    private lateinit var etDescricao: TextInputEditText
    private lateinit var btnSalvar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    
    private lateinit var apiService: ApiService
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_contato)
        
        inicializarViews()
        inicializarApiService()
        configurarListeners()
    }
    
    private fun inicializarViews() {
        toolbar = findViewById(R.id.toolbar)
        etNome = findViewById(R.id.etNome)
        etTelefone = findViewById(R.id.etTelefone)
        etDescricao = findViewById(R.id.etDescricao)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnCancelar = findViewById(R.id.btnCancelar)
    }
    
    private fun inicializarApiService() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-arduino-damp-hill-3.fly.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        apiService = retrofit.create(ApiService::class.java)
    }
    
    private fun configurarListeners() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        
        btnCancelar.setOnClickListener {
            finish()
        }
        
        btnSalvar.setOnClickListener {
            cadastrarContato()
        }
    }
    
    private fun cadastrarContato() {
        val nome = etNome.text.toString().trim()
        val telefone = etTelefone.text.toString().trim()
        val descricao = etDescricao.text.toString().trim()
        
        // Validação dos campos
        if (TextUtils.isEmpty(nome)) {
            etNome.error = "Nome é obrigatório"
            etNome.requestFocus()
            return
        }
        
        if (TextUtils.isEmpty(telefone)) {
            etTelefone.error = "Telefone é obrigatório"
            etTelefone.requestFocus()
            return
        }
        
        // Criar objeto contato
        val contato = Contato(
            nome = nome,
            telefone = telefone,
            descricao = descricao
        )
        
        // Desabilitar botão durante o envio
        btnSalvar.isEnabled = false
        btnSalvar.text = "Salvando..."
        
        scope.launch {
            try {
                val response = apiService.cadastrarContato(contato)
                
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CadastroContatoActivity,
                        getString(R.string.sucesso_cadastro),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@CadastroContatoActivity,
                        getString(R.string.erro_cadastro),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@CadastroContatoActivity,
                    "Erro de conexão: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                // Reabilitar botão
                btnSalvar.isEnabled = true
                btnSalvar.text = getString(R.string.salvar)
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
} 