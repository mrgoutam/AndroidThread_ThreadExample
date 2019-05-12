package in.goutamstark.threadexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

//https://www.youtube.com/watch?v=QfQE1ayCzf8&list=PLrnPJCHvNZuD52mtV8NvazNYIyIVPVZRa
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity"; //shortcut=> logt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void startThread(View view){

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
}
