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
                /*if (i == 5){
                    buttonStartThread.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                }*/

                //case V: works fine
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
