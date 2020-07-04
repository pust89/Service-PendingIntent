package com.pustovit.pendintservice

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Ловим сообщения о старте задач
        if (resultCode == STATUS_START) {
            when (requestCode) {
                TASK1_CODE -> tvTask1.text = "Task1 start"
                TASK2_CODE -> tvTask2.text = "Task2 start"
                TASK3_CODE -> tvTask3.text = "Task3 start"
            }
        }

        if (resultCode == STATUS_FINISH) {
            val result: Int? = data?.getIntExtra(PARAM_RESULT, 0)

            when (requestCode) {
                TASK1_CODE -> tvTask1.text = "Task1 finish, result=$result"
                TASK2_CODE -> tvTask2.text = "Task2 finish, result=$result"
                TASK3_CODE -> tvTask3.text = "Task3 finish, result=$result"
            }
        }


    }


    fun onClickStart(v: View?) {

        var pendingIntent: PendingIntent =
            createPendingResult(TASK1_CODE, Intent(), 0)
        var intent: Intent = Intent(this, MyService::class.java).apply {
            putExtra(PARAM_TIME, 7)
            putExtra(PARAM_PINTENT, pendingIntent)
        }
        startService(intent)

        pendingIntent = createPendingResult(TASK2_CODE, Intent(), 0)
        intent = Intent(this, MyService::class.java).apply {
            putExtra(PARAM_TIME, 4)
            putExtra(PARAM_PINTENT, pendingIntent)
        }
        startService(intent)

        pendingIntent = createPendingResult(TASK3_CODE, Intent(), 0)
        intent = Intent(this, MyService::class.java).apply {
            putExtra(PARAM_TIME, 6)
            putExtra(PARAM_PINTENT, pendingIntent)
        }
        startService(intent)

    }
}