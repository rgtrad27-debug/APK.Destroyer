package com.rgtrad.minhasfinancas

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private lateinit var web: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(15, 42, 46)
        window.navigationBarColor = android.graphics.Color.rgb(15, 42, 46)
        web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.allowFileAccess = true
        web.settings.allowContentAccess = false
        web.webViewClient = WebViewClient()
        web.webChromeClient = WebChromeClient()
        web.addJavascriptInterface(FinanceBridge(), "AndroidFinance")
        setContentView(web)
        if (savedInstanceState == null) web.loadUrl("file:///android_asset/index.html")
        else web.restoreState(savedInstanceState)
    }

    inner class FinanceBridge {
        @JavascriptInterface
        fun authenticateBiometric() {
            runOnUiThread {
                val manager = BiometricManager.from(this@MainActivity)
                val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
                if (manager.canAuthenticate(authenticators) != BiometricManager.BIOMETRIC_SUCCESS) {
                    web.evaluateJavascript("window.onNativeBiometricResult(false)", null)
                    return@runOnUiThread
                }
                val prompt = BiometricPrompt(this@MainActivity, ContextCompat.getMainExecutor(this@MainActivity),
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            web.evaluateJavascript("window.onNativeBiometricResult(true)", null)
                        }
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            web.evaluateJavascript("window.onNativeBiometricResult(false)", null)
                        }
                        override fun onAuthenticationFailed() {
                            // O sistema permite novas tentativas; não fecha a tela.
                        }
                    })
                val info = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Minhas Finanças")
                    .setSubtitle("Confirme sua identidade para acessar seus dados")
                    .setAllowedAuthenticators(authenticators)
                    .build()
                prompt.authenticate(info)
            }
        }
    }

    override fun onBackPressed() {
        if (::web.isInitialized && web.canGoBack()) web.goBack() else super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::web.isInitialized) web.saveState(outState)
        super.onSaveInstanceState(outState)
    }
}
