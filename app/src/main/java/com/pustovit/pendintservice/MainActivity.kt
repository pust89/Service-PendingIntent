package com.pustovit.pendintservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialBroadcastReceiver()

    }

    fun initialBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {


            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    val task = intent.getIntExtra(PARAM_TASK, 0)
                    val status = intent.getIntExtra(PARAM_STATUS, 0)
                    Log.i(TAG, "onReceive:  task =  $task , status =  $status")

                    // Ловим сообщения о старте задач
                    if (status == STATUS_START) {
                        when (task) {
                            TASK1_CODE -> tvTask1.text = "Task1 start"
                            TASK2_CODE -> tvTask2.text = "Task2 start"
                            TASK3_CODE -> tvTask3.text = "Task3 start"
                        }
                    }

                    if (status == STATUS_FINISH) {
                        val result: Int? = intent.getIntExtra(PARAM_RESULT, 0)
                        when (task) {
                            TASK1_CODE -> tvTask1.text = "Task1 finish, result=$result"
                            TASK2_CODE -> tvTask2.text = "Task2 finish, result=$result"
                            TASK3_CODE -> tvTask3.text = "Task3 finish, result=$result"
                        }
                    }

                }
            }


        }

        val intentFilter: IntentFilter = IntentFilter(BROADCAST_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Ловим сообщения о старте задач
//        if (resultCode == STATUS_START) {
//            when (requestCode) {
//                TASK1_CODE -> tvTask1.text = "Task1 start"
//                TASK2_CODE -> tvTask2.text = "Task2 start"
//                TASK3_CODE -> tvTask3.text = "Task3 start"
//            }
//        }
//
//        if (resultCode == STATUS_FINISH) {
//            val result: Int? = data?.getIntExtra(PARAM_RESULT, 0)
//
//            when (requestCode) {
//                TASK1_CODE -> tvTask1.text = "Task1 finish, result=$result"
//                TASK2_CODE -> tvTask2.text = "Task2 finish, result=$result"
//                TASK3_CODE -> tvTask3.text = "Task3 finish, result=$result"
//            }
//        }
//    }


    fun onClickStart(v: View?) {

        var intent: Intent = Intent(this, MyService::class.java).apply {
            putExtra(PARAM_TIME, 7)
                .putExtra(PARAM_TASK, TASK1_CODE);
        }
        startService(intent)


        intent = Intent(this@MainActivity, MyService::class.java).apply {
            putExtra(PARAM_TIME, 4)
                .putExtra(PARAM_TASK, TASK2_CODE);
        }
        startService(intent)


        intent = Intent(this, MyService::class.java).apply {
            putExtra(PARAM_TIME, 6)
                .putExtra(PARAM_TASK, TASK3_CODE);
        }
        startService(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }
    }
}