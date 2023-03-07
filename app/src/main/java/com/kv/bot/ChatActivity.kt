package com.kv.bot

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kv.bot.databinding.ActivityChatbotBinding
import com.kv.bot.datastorage.dataStorage.USERNAME
import com.kv.bot.datastorage.dataStorage.loadData
import com.kv.bot.models.ChatModel
import com.kv.bot.models.NotificationModel
import com.kv.bot.service.ChatService
import com.kv.bot.ui.adapter.ChatMessageAdapter

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatbotBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:ChatMessageAdapter
    private lateinit var receiver: BroadcastReceiver
    private  var listOfMessage:MutableList<ChatModel> = ArrayList<ChatModel>()

    private lateinit var myServiceIntent:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        myServiceIntent = Intent(applicationContext, ChatService::class.java)

        adapter = ChatMessageAdapter(listOfMessage)
        recyclerView.adapter = adapter


        val rootView = binding.rootView

        // Scroll the RecyclerView to the bottom if detect change in layout
        // size which may indicate that the keybord is visible
        rootView.viewTreeObserver.addOnGlobalLayoutListener {

            if(adapter.itemCount > 3) {
                recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }


        // Create the BroadcastReceiver
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val message = intent.getStringExtra("message")
                val chatMessage = Gson().fromJson(message,ChatModel::class.java)
                listOfMessage.add(
                    chatMessage
                )
                adapter.notifyItemInserted(listOfMessage.count()-1)
            }
        }

        val filter = IntentFilter()
        filter.addAction(ChatService.BROADCAST_ID)
        registerReceiver(receiver, filter)


        //request notification permission

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->

            }

        when {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY

            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            shouldShowRequestPermissionRationale("Require permission inorder to get notificaion from chat bot") -> {

            }
            else -> {

                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            }
        }
    }

    fun generateMessage(view: View) {

        var bundle = Bundle()
        bundle.putString(NotificationModel.CHAT_NAME,"Bot")
        bundle.putInt(NotificationModel.CHAT_AVATAR,R.drawable.chat_bot_avatar)
        bundle.putString(USERNAME,loadData(this,USERNAME,""))

        startService(ChatService.CMD_MSG, ChatService.CMD_GENERATE_MESSAGE,bundle)
    }

    fun stopService(view: View) {
        var bundle = Bundle()
        bundle.putString(NotificationModel.CHAT_NAME,"Bot")
        bundle.putInt(NotificationModel.CHAT_AVATAR,R.drawable.chat_bot_avatar)
        startService(ChatService.CMD_MSG, ChatService.CMD_STOP_SERVICE,bundle)
    }

    private fun startService(cmdMsg: String, cmdGenerateMessage: String,data:Bundle = Bundle()) {
        data.putString(cmdMsg, cmdGenerateMessage)
        myServiceIntent.putExtras(data)
        startService(myServiceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(myServiceIntent)
        unregisterReceiver(receiver)
    }




}