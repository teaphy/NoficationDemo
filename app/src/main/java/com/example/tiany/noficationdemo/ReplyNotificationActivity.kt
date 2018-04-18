package com.example.tiany.noficationdemo

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.RemoteInput
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_reply_notification.*

class ReplyNotificationActivity : AppCompatActivity() {

    private val REQUEST_CODE_REPLY = 0x01

    companion object {
        var notificationId = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply_notification)

//        // 注册广播
//        val myReceiver = MyBroadcastReceiver()
//        val filterIntent = IntentFilter()
//        filterIntent.addAction(REPLY_ACTION)
//
//        registerReceiver(myReceiver, filterIntent)

        setListener()
    }

    private fun setListener() {
        btnReplyNoti.setOnClickListener({
            val replyLabel = "Reply"
            // 创建一个RemoteInput实例
            val remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
                    .setLabel(replyLabel)
                    .build()

            val replyReceiverIntent = Intent(this, MyBroadcastReceiver::class.java)
            // 为响应动作添加一个PendingIntent
            val replyPendingIntent = PendingIntent.getBroadcast(this, 0, replyReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            // 调用addRemoteInput()方法将RemoteInput实例绑定到action
            val action = NotificationCompat.Action.Builder(R.drawable.ic_fg, "reply", replyPendingIntent)
                    .addRemoteInput(remoteInput)
                    .build()
            // 将操作应用于通知并发出通知。
            val messageNotification = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_fg)
                    .setContentTitle("Test Reply")
                    .setContentText("This a demo for the notification with reply")
                    .addAction(action)
                    .build()

            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(notificationId, messageNotification)
        })
    }

    class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e("tea", "来源于通知广播")

            if (null != intent) {
                val remoteInput = getMessageText(intent)
                if (null == remoteInput) {
                    Log.e("tea", "用户未输入")
                } else {

                    val notificationManager = NotificationManagerCompat.from(context!!)
                    notificationManager.cancel(ReplyNotificationActivity.notificationId)
                    Log.e("tea", "用户输入：$remoteInput")
                    Toast.makeText(context, "回复成功！", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getMessageText(intent: Intent): CharSequence? {
            val remoteInput: Bundle? = RemoteInput.getResultsFromIntent(intent)

            if (null != remoteInput) {
                return remoteInput.getCharSequence(KEY_TEXT_REPLY)
            }

            return null
        }
    }

}
