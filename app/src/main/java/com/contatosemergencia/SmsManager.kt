package com.contatosemergencia

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SmsManager(private val context: Context) {
    
    companion object {
        private const val SMS_PERMISSION_REQUEST = 100
        private const val MENSAGEM_QUEDA = "Atenção! Queda detectada"
    }
    
    fun temPermissaoSms(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun solicitarPermissaoSms(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.SEND_SMS),
            SMS_PERMISSION_REQUEST
        )
    }
    
    fun enviarSmsParaTodos(contatos: List<Contato>): Boolean {
        if (!temPermissaoSms()) {
            return false
        }
        
        return try {
            val smsManager = SmsManager.getDefault()
            
            contatos.forEach { contato ->
                try {
                    // Envia SMS para cada contato
                    smsManager.sendTextMessage(
                        contato.telefone,
                        null,
                        MENSAGEM_QUEDA,
                        null,
                        null
                    )
                } catch (e: Exception) {
                    // Log do erro mas continua enviando para outros contatos
                    android.util.Log.e("SmsManager", "Erro ao enviar SMS para ${contato.nome}: ${e.message}")
                }
            }
            
            true
        } catch (e: Exception) {
            android.util.Log.e("SmsManager", "Erro geral ao enviar SMS: ${e.message}")
            false
        }
    }
    
    fun enviarSmsParaContato(contato: Contato): Boolean {
        if (!temPermissaoSms()) {
            return false
        }
        
        return try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(
                contato.telefone,
                null,
                MENSAGEM_QUEDA,
                null,
                null
            )
            true
        } catch (e: Exception) {
            android.util.Log.e("SmsManager", "Erro ao enviar SMS: ${e.message}")
            false
        }
    }
} 