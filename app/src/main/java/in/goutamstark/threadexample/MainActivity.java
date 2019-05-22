package in.goutamstark.threadexample;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//https://www.youtube.com/watch?v=QfQE1ayCzf8&list=PLrnPJCHvNZuD52mtV8NvazNYIyIVPVZRa
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity"; //shortcut=> logt
    private Button buttonStartThread;
    private Handler mainHandler = new Handler();
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartThread = findViewById(R.id.button_start_thread);
    }



    public void startThread(View view){
        stopThread = false;

        /*//we will do a heavy operation
        for(int i=0; i<=10; i++){
            Log.d(TAG, "startThread: "+i); //shortcut => logd
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        //because of freezing the other functionality in mainThread, we are creating a Thread

        //Method I
        /*ExampleThread thread = new ExampleThread(10);
        thread.start();*/

        //Method II
        ExampleRunnable runnable = new ExampleRunnable(10);
        new Thread(runnable).start();
    }

    public void stopThread(View view){
        stopThread = true;
    }

    //1. Extending the thread class
    class ExampleThread extends Thread{
        int seconds;

        ExampleThread(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            //we will do a heavy operation instead in startThread method as it freezes our mainThread
            for(int i=0; i<=seconds; i++){
                Log.d(TAG, "startThread: "+i); //shortcut => logd
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //2. implementing the runnable interface
    // this process is preferable instead of extendign the thread
    //class. we just need a work to execute.
    class ExampleRunnable implements Runnable{
        int seconds;

        public ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            if (stopThread){
                return;
            }
            //we will do a heavy operation instead in startThread method as it freezes our mainThread
            for(int i=0; i<=seconds; i++){
                Log.d(TAG, "startThread: "+i); //shortcut => logd

                /*
                //App will crash if we are not using this inside a handler
                // because we cannot communicate UI thread without a handler.
                //handler communicates between threads.
                //our action is in exampleThread and our button buttonStartThread
                // is in UI Thread. so two different threads. so wen need handler
                //to communicate.
                if (i == 5){
                    buttonStartThread.setText("50%");
                }*/

                //case I: works fine
                /*if (i == 5){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                }*/

                //case II: crash - see the log
                /*Handler threadHandler = new Handler();
                if (i == 5){
                    threadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                }*/

                //case III: works fine
                //now handler is associated with the looper of the ui thread with passing Looper.getMainLooper()
                //instead of case III you can do it by case IV
                /*Handler threadHandler = new Handler(Looper.getMainLooper());
                if (i == 5){
                    threadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                }*/

                //case IV: works fine
                //
                /*if (i == 5){
                    buttonStartThread.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                }*/

                //case V: works fine
                //runOnUiThread is a activity method
                if (i == 5){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

/*
There are two way of starting the background thread:
1. Extending the Thread class and override the run method
or
2. Implementing the runnable interface and passing this runnable
 object to a new thread.

 Handler and Looper: those make running these thread and
 communicates between different threads more convenient.


Asynctask, HandlerThread, ThreadPoolExecutor are the heigher
extraction classes. will discuss later.


HOW HANDLER WORKS?-Later

SOMETHING ABOUT UI THREAD:
After app started the ui thread just not terminated even if it does not have any work to do. instead it wait for new input like click a button
and do something or perform any other works. The mechanism which keeps alive the ui thread is called Message Queue. Intead just one piece of
works it has whole lot of work to execute. one after another. Then there something called looper which loops through the message queue. and dispatches
messages sequencially. and this is literally a infinite for loop. and unless we create a purpose we dont leave this loop. There something called
handler which is responsible for    getting the messages of works into the message queue and it is used to get work from background thread to main
thread.

 */