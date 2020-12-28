package org.unifiedpush.connector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

interface MessagingReceiverHandler {
    fun onNewEndpoint(context: Context?, endpoint: String)
    fun onUnregistered(context: Context?)
    fun onUnregisteredAck(context: Context?)
    fun onMessage(context: Context?, message: String)
}

open class MessagingReceiver(private val handler: MessagingReceiverHandler) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent!!.action) {
            ACTION_NEW_ENDPOINT -> {
                if (getToken(context!!) != intent.getStringExtra(EXTRA_TOKEN)) {
                    return
                }
                val endpoint = intent.getStringExtra(EXTRA_ENDPOINT)!!
                this@MessagingReceiver.handler.onNewEndpoint(context, endpoint)
            }
            ACTION_UNREGISTERED -> {
                intent.getStringExtra(EXTRA_TOKEN).let {
                    if (it.isNullOrEmpty())
                        this@MessagingReceiver.handler.onUnregisteredAck(context)
                    else {
                        if (getToken(context!!) != it) {
                            return
                        }
                        this@MessagingReceiver.handler.onUnregistered(context)
                    }
                    removeToken(context!!)
                    removeDistributor(context!!)
                }
            }
            ACTION_MESSAGE -> {
                if (getToken(context!!) != intent.getStringExtra(EXTRA_TOKEN)) {
                    return
                }
                val message = intent.getStringExtra(EXTRA_MESSAGE)!!
                val id = intent.getStringExtra(EXTRA_MESSAGE_ID) ?: ""
                this@MessagingReceiver.handler.onMessage(context, message)
                acknowledgeMessage(context, id)
            }
        }
    }
}

private fun acknowledgeMessage(context: Context, id: String) {
    val token = getToken(context)!!
    val broadcastIntent = Intent()
    broadcastIntent.`package` = getDistributor(context)
    broadcastIntent.action = ACTION_MESSAGE_ACK
    broadcastIntent.putExtra(EXTRA_TOKEN, token)
    broadcastIntent.putExtra(EXTRA_MESSAGE_ID, id)
    context.sendBroadcast(broadcastIntent)
}