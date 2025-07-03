package com.contatosemergencia

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerViewContatos: androidx.recyclerview.widget.RecyclerView
    private lateinit var tvSemContatos: MaterialTextView
    private lateinit var tvStatusBluetooth: MaterialTextView
    private lateinit var btnConectarBluetooth: MaterialButton
    private lateinit var fabAdicionarContato: FloatingActionButton
    
    private lateinit var contatosAdapter: ContatosAdapter
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var smsManager: SmsManager
    private lateinit var apiService: ApiService
    
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    companion object {
        private const val BLUETOOTH_REQUEST_CODE = 1
        private const val PERMISSIONS_REQUEST_CODE = 2
        private const val HC05_MAC_ADDRESS = "98:D3:31:FD:65:0F" // Substitua pelo MAC do seu HC-05
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        inicializarViews()
        inicializarManagers()
        configurarRecyclerView()
        configurarListeners()
        solicitarPermissoes()
        carregarContatos()
    }
    
    private fun inicializarViews() {
        recyclerViewContatos = findViewById(R.id.recyclerViewContatos)
        tvSemContatos = findViewById(R.id.tvSemContatos)
        tvStatusBluetooth = findViewById(R.id.tvStatusBluetooth)
        btnConectarBluetooth = findViewById(R.id.btnConectarBluetooth)
        fabAdicionarContato = findViewById(R.id.fabAdicionarContato)
    }
    
    private fun inicializarManagers() {
        // Inicializar API Service
        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-arduino-damp-hill-3.fly.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        apiService = retrofit.create(ApiService::class.java)
        
        // Inicializar managers
        bluetoothManager = BluetoothManager(this)
        smsManager = SmsManager(this)
        
        // Configurar callbacks do Bluetooth
        bluetoothManager.onQuedaDetected = {
            scope.launch {
                enviarSmsEmergencia()
            }
        }
        
        bluetoothManager.onConnectionStatusChanged = { conectado ->
            runOnUiThread {
                atualizarStatusBluetooth(conectado)
            }
        }
    }
    
    private fun configurarRecyclerView() {
        contatosAdapter = ContatosAdapter(onExcluirClick = { contato ->
            confirmarExclusao(contato)
        })
        recyclerViewContatos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = contatosAdapter
        }
    }
    
    private fun configurarListeners() {
        fabAdicionarContato.setOnClickListener {
            val intent = Intent(this, CadastroContatoActivity::class.java)
            startActivity(intent)
        }
        
        btnConectarBluetooth.setOnClickListener {
            if (bluetoothManager.estaConectado()) {
                desconectarBluetooth()
            } else {
                conectarBluetooth()
            }
        }
    }
    
    private fun solicitarPermissoes() {
        val permissoes = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        
        val permissoesNecessarias = permissoes.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
        
        if (permissoesNecessarias.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissoesNecessarias, PERMISSIONS_REQUEST_CODE)
        }
    }
    
    private fun carregarContatos() {
        scope.launch {
            try {
                val response = apiService.getContatos()
                if (response.isSuccessful) {
                    val contatos = response.body() ?: emptyList()
                    contatosAdapter.updateContatos(contatos)
                    atualizarVisibilidadeLista(contatos.isNotEmpty())
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao carregar contatos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun conectarBluetooth() {
        if (!bluetoothManager.isBluetoothEnabled()) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, BLUETOOTH_REQUEST_CODE)
            return
        }
        
        scope.launch {
            val sucesso = bluetoothManager.conectarHC05(HC05_MAC_ADDRESS)
            if (!sucesso) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Erro ao conectar Bluetooth", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun desconectarBluetooth() {
        bluetoothManager.desconectar()
    }
    
    private fun atualizarStatusBluetooth(conectado: Boolean) {
        if (conectado) {
            tvStatusBluetooth.text = "Status Bluetooth: Conectado"
            btnConectarBluetooth.text = getString(R.string.desconectar_bluetooth)
        } else {
            tvStatusBluetooth.text = getString(R.string.status_bluetooth)
            btnConectarBluetooth.text = getString(R.string.conectar_bluetooth)
        }
    }
    
    private fun atualizarVisibilidadeLista(temContatos: Boolean) {
        if (temContatos) {
            recyclerViewContatos.visibility = View.VISIBLE
            tvSemContatos.visibility = View.GONE
        } else {
            recyclerViewContatos.visibility = View.GONE
            tvSemContatos.visibility = View.VISIBLE
        }
    }
    
    private suspend fun enviarSmsEmergencia() {
        val contatos = contatosAdapter.getContatos()
        if (contatos.isNotEmpty()) {
            val sucesso = smsManager.enviarSmsParaTodos(contatos)
            runOnUiThread {
                if (sucesso) {
                    Toast.makeText(this@MainActivity, "SMS de emergência enviado!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao enviar SMS", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun confirmarExclusao(contato: Contato) {
        AlertDialog.Builder(this)
            .setTitle("Excluir contato")
            .setMessage("Tem certeza que deseja excluir o contato ${contato.nome}?")
            .setPositiveButton("Excluir") { _, _ ->
                excluirContato(contato)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun excluirContato(contato: Contato) {
        scope.launch {
            try {
                val response = apiService.excluirContato(contato.id ?: "")
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Contato excluído com sucesso!", Toast.LENGTH_SHORT).show()
                        carregarContatos()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Erro ao excluir contato", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        carregarContatos()
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == BLUETOOTH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                conectarBluetooth()
            }
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            val todasPermissoesConcedidas = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            
            if (!todasPermissoesConcedidas) {
                Toast.makeText(this, getString(R.string.permissao_necessaria), Toast.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        bluetoothManager.onDestroy()
        scope.cancel()
    }
} 