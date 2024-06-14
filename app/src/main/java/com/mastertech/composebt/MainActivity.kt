package com.mastertech.composebt

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mastertech.composebt.ui.theme.ComposeBTTheme

class GlobalExampleClass : Application() {
    private var globalName: String? = null
    private var globalEmail: String? = null

    fun getName() : String? {
        return globalName
    }
    fun setName(aName: String?) {
        globalName = aName
    }

    fun getEmail(): String? {
        return globalEmail
    }
   fun setEmail (aEmail: String?) {
       globalEmail = aEmail
   }
}

class MainActivity : ComponentActivity() {
    private val PERMISSION_CODE = 1

    val bluetoothManager = getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
    private var btAdapter: BluetoothAdapter? = null

//
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK)
            { Log.i("Bluetooth", ":request permission result ok")
        } else
        { Log.i("Bluetooth", ":request permission result canceled / denied")
        }
    }

    private fun requestBluetoothPermission() {
        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activityResultLauncher.launch(enableBluetoothIntent)
    }

    @SuppressLint("MissingPermission")
    var pairedDevices: Set<BluetoothDevice> = btAdapter!!.bondedDevices

    var discoveredDevices: Set<BluetoothDevice> = emptySet()

//    private val receiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            when (intent?.action) {
//                BluetoothDevice.ACTION_FOUND -> {
//                   val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//                    if (device != null) {
//                        val updated = discoveredDevices.plus(device)
//                        discoveredDevices = updated
//                    }
//                    Log.i("Bluetooth", "onReceive: Device found")
//                }
//                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
//                    Log.i("Bluetooth", "onReceive: Started Discovery")
//                }
//                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
//                    Log.i("Bluetooth", "onReceive: Finished Discovery")
//                }
//            }
//        }
//
//    }

//    @SuppressLint("MissingPermission")
//    @RequiresApi(Build.VERSION_CODES.M)
//    fun scan() : Set<BluetoothDevice> {
//        if (bluetoothAdapter.isDiscovering) {
//            bluetoothAdapter.cancelDiscovery()
//            bluetoothAdapter.startDiscovery()
//        } else {
//            bluetoothAdapter.startDiscovery()
//        }
//        Handler(Looper.getMainLooper()).postDelayed({
//            bluetoothAdapter.cancelDiscovery()
//        }, 10000L)
//        return discoveredDevices
//    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val globalExampleVariable = applicationContext as GlobalExampleClass

        globalExampleVariable.setName("getApplicationContext example")
        globalExampleVariable.setEmail("sebasarriaga@gmail.com")

        btAdapter = bluetoothManager.adapter
        if (btAdapter == null) {
            Log.i("Bluetooth", "Device doesn't support Bluetooth")
        }
//        val foundFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
 //        val startFilter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
//        val endFilter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//        registerReceiver(receiver, foundFilter)
//        registerReceiver(receiver, startFilter)
//        registerReceiver(receiver, endFilter)
//
//       if (!bluetoothAdapter.isEnabled) {
//            requestBluetoothPermission()
//        }
//
//        if (SDK_INT >= Build.VERSION_CODES.O) {
//            if (ContextCompat.checkSelfPermission(
//                    baseContext, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED) {
//                if (SDK_INT >= Build.VERSION_CODES.Q) {
//                    ActivityCompat.requestPermissions(
//                        this, arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),
//                        PERMISSION_CODE
//                    )
//                }
//            }
//        }

        setContent {
            var devices: Set<BluetoothDevice> by remember { mutableStateOf(emptySet()) }
            ComposeBTTheme {
                // A surface container using the 'background' color from the theme
                Surface(

                    color = MaterialTheme.colorScheme.background
                ) { Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Jetpack Bluetooth Connected List",
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Left
                                )
                            }
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        OutlinedButton(onClick = {/*devices = scan() */}) {


                            Text(
                                text = "Scan",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Paired Devices List",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.height(10.dp))
//                        pairedDevices.forEach { device ->
//                            Card(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(horizontal = 5.dp, vertical = 3.dp),
//                                elevation = CardDefaults.cardElevation(
//                                    defaultElevation = 10.dp),
//
//                                ) {
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(5.dp),
//                                    verticalArrangement = Arrangement.spacedBy(10.dp)
//                                ) {
//                                    Text(text = device.name, style = MaterialTheme.typography.labelSmall)
//                                    Text(text = device.address, style = MaterialTheme.typography.labelSmall)
//                                }
//                            }
//                        }
                    }
                }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
//        if (bluetoothAdapter.isDiscovering)
//            bluetoothAdapter.cancelDiscovery()
//
//        unregisterReceiver(receiver)
    }
}


