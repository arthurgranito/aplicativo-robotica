package com.contatosemergencia

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.util.*

class BluetoothManager(private val context: Context) {
    
    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var isConnected = false
    private var isListening = false
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    // UUID padrão para HC-05
    private val HC05_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    
    var onQuedaDetected: (() -> Unit)? = null
    var onConnectionStatusChanged: ((Boolean) -> Unit)? = null
    
    fun conectarHC05(enderecoMac: String): Boolean {
        return try {
            val device = bluetoothAdapter?.getRemoteDevice(enderecoMac)
            bluetoothSocket = device?.createRfcommSocketToServiceRecord(HC05_UUID)
            bluetoothSocket?.connect()
            
            isConnected = bluetoothSocket?.isConnected == true
            onConnectionStatusChanged?.invoke(isConnected)
            
            if (isConnected) {
                inputStream = bluetoothSocket?.inputStream
                iniciarEscuta()
            }
            
            isConnected
        } catch (e: IOException) {
            Log.e("BluetoothManager", "Erro ao conectar: ${e.message}")
            isConnected = false
            onConnectionStatusChanged?.invoke(false)
            false
        }
    }
    
    private fun iniciarEscuta() {
        if (isListening) return
        
        isListening = true
        scope.launch {
            val buffer = ByteArray(1024)
            
            while (isConnected && isListening) {
                try {
                    val bytes = inputStream?.read(buffer)
                    if (bytes != null && bytes > 0) {
                        val mensagem = String(buffer, 0, bytes).trim()
                        Log.d("BluetoothManager", "Mensagem recebida: $mensagem")
                        
                        if (mensagem.contains("queda", ignoreCase = true)) {
                            Log.d("BluetoothManager", "Queda detectada!")
                            onQuedaDetected?.invoke()
                        }
                    }
                } catch (e: IOException) {
                    Log.e("BluetoothManager", "Erro na leitura: ${e.message}")
                    break
                }
            }
        }
    }
    
    fun desconectar() {
        isListening = false
        isConnected = false
        
        try {
            inputStream?.close()
            bluetoothSocket?.close()
        } catch (e: IOException) {
            Log.e("BluetoothManager", "Erro ao desconectar: ${e.message}")
        }
        
        onConnectionStatusChanged?.invoke(false)
    }
    
    fun estaConectado(): Boolean = isConnected
    
    fun getDispositivosPareados(): Set<BluetoothDevice>? {
        return bluetoothAdapter?.bondedDevices
    }
    
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }
    
    fun onDestroy() {
        desconectar()
        scope.cancel()
    }
} 