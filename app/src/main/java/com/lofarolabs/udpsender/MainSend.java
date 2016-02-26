package com.lofarolabs.udpsender;

import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;

import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.os.SystemClock;
import android.os.StrictMode;
import android.widget.EditText;
import android.view.MotionEvent;
import android.graphics.PointF;
import android.util.SparseArray;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;
import android.content.Context;
import java.util.TimerTask;
import java.util.Timer;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.widget.ImageView;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import android.text.SpannableString;
import android.widget.Toast;

public class MainSend extends ActionBarActivity {
    private static final String host = null;
    private int port;
    String str=null;
    byte[] send_data = new byte[1024];
    byte[] receiveData = new byte[1024];
    Button bt_open_port,bt_send_port,bt3,bt4;
    TextView txt0,txt1, txt_touch_x, txt_touch_y;
    EditText txt_ip, txt_port;
    DatagramSocket client_socket = null;
    int mPort = 2362;
    private SparseArray<PointF> mActivePointers;
    boolean pressedUp = false;
    private SurfaceHolder holder;
    Timer t = new Timer();
    Paint paint = new Paint();
    ImageView drawingImageView;

    /* calibration for use with other devices */
    double touch_center_yP, touch_center_xP;
    double touch_delta_yP, touch_delta_xP;
    double joy_left_center_yP, joy_left_center_xP, joy_left_radiusP;
    double joy_right_center_yP, joy_right_center_xP, joy_right_radiusP;
    double button_and_center_yP, button_and_center_xP, button_and_radiusP;
    double button_at_center_yP, button_at_center_xP, button_at_radiusP;
    double button_hash_center_yP, button_hash_center_xP, button_hash_radiusP;
    double button_percent_center_yP, button_percent_center_xP, button_percent_radiusP;
    double button_start_center_yP, button_start_center_xP;
    double button_start_delta_yP, button_start_delta_xP;
    double button_select_center_yP, button_select_center_xP;
    double button_select_delta_yP, button_select_delta_xP;

    /* calibration for nexus 7 */
    double touch_center_y = 660.0; double touch_center_x = 960.0;
    double touch_delta_x = 400.0; double touch_delta_y = 280.0;
    double joy_left_center_x = 245.0; double joy_left_center_y = 880.0;
    double joy_right_center_x = 1685.0; double joy_right_center_y = 880.0;
    double joy_right_radius = 180.0; double joy_left_radius = 180.0;
    double button_and_center_x = 1680.0; double button_and_center_y = 570.0;
    double button_and_radius = 60.0;
    double button_at_center_x = 1680.0; double button_at_center_y = 285.0;
    double button_at_radius = 60.0;
    double button_hash_center_x = 1520.0; double button_hash_center_y = 425.0;
    double button_hash_radius = 60.0;
    double button_percent_center_x = 1825.0; double button_percent_center_y = 425.0;
    double button_percent_radius = 60.0;
    double button_start_center_x = 365.0; double button_start_center_y = 630.0;
    double button_start_delta_x = 95.0; double button_start_delta_y = 45.0;
    double button_select_center_x = 120.0; double button_select_center_y = 630.0;
    double button_select_delta_x = 95.0; double button_select_delta_y = 45.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher_arc);

        /* Gets the Screen width and height of current device and converts Nexus 7 measurements
         * for buttons above to the current device */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        double device_width = displayMetrics.widthPixels;  double conversion_y = device_width/1920;
        double device_height = displayMetrics.heightPixels; double conversion_x = device_height/1200;

        touch_center_yP = touch_center_y*conversion_y;
        touch_center_xP = touch_center_x*conversion_x;
        touch_delta_yP = touch_delta_y*conversion_y;
        touch_delta_xP = touch_delta_x*conversion_x;
        joy_left_center_yP = joy_left_center_y*conversion_y;
        joy_left_center_xP = joy_left_center_x*conversion_x;
        joy_left_radiusP = joy_left_radius*conversion_y;
        joy_right_center_yP = joy_right_center_y*conversion_y;
        joy_right_center_xP = joy_right_center_x*conversion_x;
        joy_right_radiusP = joy_right_radius*conversion_y;
        button_and_center_yP = button_and_center_y*conversion_y;
        button_and_center_xP = button_and_center_x*conversion_x;
        button_and_radiusP = button_and_radius*conversion_y;
        button_at_center_yP = button_at_center_y*conversion_y;
        button_at_center_xP = button_at_center_x*conversion_x;
        button_at_radiusP = button_at_radius*conversion_y;
        button_hash_center_yP = button_hash_center_y*conversion_y;
        button_hash_center_xP = button_hash_center_x*conversion_x;
        button_hash_radiusP = button_hash_radius*conversion_y;
        button_percent_center_yP = button_percent_center_y*conversion_y;
        button_percent_center_xP = button_percent_center_x*conversion_x;
        button_percent_radiusP = button_percent_radius*conversion_y;
        button_start_center_yP = button_start_center_y*conversion_y;
        button_start_center_xP = button_start_center_x*conversion_x;
        button_start_delta_yP = button_start_delta_y*conversion_y;
        button_start_delta_xP = button_start_delta_x*conversion_x;
        button_select_center_yP = button_select_center_y*conversion_y;
        button_select_center_xP = button_select_center_x*conversion_x;
        button_select_delta_yP = button_select_delta_y*conversion_y;
        button_select_delta_xP = button_select_delta_x*conversion_x;

        mActivePointers = new SparseArray<PointF>();

        //BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap)
        //image.setBackground(Drawable background)



        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }





        setContentView(R.layout.activity_main_send);
        txt1   = (TextView)findViewById(R.id.textView_top);
        bt_open_port = (Button) findViewById(R.id.button_open_port);
        bt_open_port.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //bt_open_port.setText("Dan1");
                // Perform action on click
                //textIn.setText("test");
                //txt2.setText("text2");
                //task.execute(null);
                str="temp";
                try {
                    client_open();

                    //txt1.setText(modifiedSentence);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    txt1.setText(e.toString());
                    e.printStackTrace();
                }
            }

        });




        bt_send_port = (Button) findViewById(R.id.button_send_port);
        bt_send_port.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Perform action on click
                //textIn.setText("test");
                //txt2.setText("text2");
                //task.execute(null);
                str="temp";
                try {
                    client_send();


                    //txt1.setText(modifiedSentence);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    txt1.setText(e.toString());
                    e.printStackTrace();
                }
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Location information */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        //vibrator.vibrate(500); // 0.5 seconds

        int x = (int)event.getX();
        int y = (int)event.getY();
        txt_touch_x   = (TextView)findViewById(R.id.textView_touch_x);
        txt_touch_y   = (TextView)findViewById(R.id.textView_touch_y);
        txt_touch_x.setText(Integer.toString(x));
        txt_touch_y.setText(Integer.toString(y));
        vibrator.vibrate(10);
        //txt1.setText(Integer.toString(event.getPointerCount()));

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
/*
                if(pressedUp == false){
                    pressedUp = true;
                    try {
                        txt1.setText("dan1");
                        client_send_buff("xx");
                        txt1.setText("dan");
                        //txt1.setText(modifiedSentence);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        txt1.setText(e.toString());
                        e.printStackTrace();
                    }
                }
                */
                break;
            case MotionEvent.ACTION_UP:

                pressedUp = false;

        }





        txt1.setText("dana");
        for (int size = event.getPointerCount(), i = 0; i < size; i++) {
            txt1.setText("danb");
            PointF point = mActivePointers.get(event.getPointerId(i));
            txt1.setText("danc");
            if (1==1) {
                txt1.setText("dand");
                float xx = event.getX(i);
                float yy = event.getY(i);

                boolean openFlag = false;
                if (!(client_socket == null)) {
                    openFlag = (client_socket.getLocalPort() == Integer.parseInt(txt_port.getText().toString())) & !client_socket.isClosed();
                }

                if(openFlag) {
                    String buff = parseTouch(xx, yy);
                    if(buff != null) {
                        try {
                            client_send_buff(buff);
                            //txt1.setText(modifiedSentence);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            txt1.setText(e.toString());
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

        /*
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
        }
        */
        return false;
    }
/*
    public double[] getCenterSquare(float x, float y) {
        double[] xy = {0.0, 0.0};
        xy[0] =
        return xy;
    }
*/
    //public void client() throws IOException {
    public void client_open() throws IOException {
        //SystemClock.sleep(1000);
        txt_port   = (EditText)findViewById(R.id.editText_port);
        mPort = Integer.parseInt(txt_port.getText().toString());
        client_socket = new DatagramSocket(mPort);
        bt_open_port.setText("Port Open");
    }
    public void client_send() throws IOException {
        //SystemClock.sleep(1000);
        txt_ip   = (EditText)findViewById(R.id.editText_ip);
        InetAddress IPAddress =  InetAddress.getByName(txt_ip.getText().toString());
        str="dan1";
        send_data = str.getBytes();
        DatagramPacket send_packet = new DatagramPacket(send_data,str.length(), IPAddress, mPort);
        client_socket.send(send_packet);
    }

    public void client_send_buff(String buff) throws IOException {
        //SystemClock.sleep(1000);
        txt_ip   = (EditText)findViewById(R.id.editText_ip);
        InetAddress IPAddress =  InetAddress.getByName(txt_ip.getText().toString());
        str=buff;
        send_data = str.getBytes();
        DatagramPacket send_packet = new DatagramPacket(send_data,str.length(), IPAddress, mPort);
        client_socket.send(send_packet);
        try {Thread.sleep(10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void send_buff(String buff) {
        //SystemClock.sleep(1000);
        try {
            client_send_buff(buff);
            //txt1.setText(modifiedSentence);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            txt1.setText(e.toString());
            e.printStackTrace();
        }

    }

    public static byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    public String parseTouch(float x, float y){
        byte[] button = {0};
        double[] xy = {0.0, 0.0};
        String theOut = null;


        /* main touch */
        if(
                x > (touch_center_xP-touch_delta_xP) &
                x < (touch_center_xP+touch_delta_xP) &
                y > (touch_center_yP-touch_delta_yP) &
                y < (touch_center_yP+touch_delta_yP))
        {
            button = ("t").getBytes();
            double xx = (x-touch_center_xP)/touch_delta_xP;
            double yy = -(y-touch_center_yP)/touch_delta_yP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("touch").getBytes() );
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%.3f", xx)).getBytes());
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%.3f", yy)).getBytes());
                //outputStream.write( toByteArray(xx) );
                //outputStream.write(toByteArray(yy));
                txt1.setText(outputStream.toString());
                //outputStream.toByteArray();
                theOut = outputStream.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                txt1.setText(e.toString());
                e.printStackTrace();
            }

        }
        /* left joystick */
        else if(
                (Math.sqrt((x-joy_left_center_xP)*(x-joy_left_center_xP) + (y-joy_left_center_yP)*(y-joy_left_center_yP)) < joy_left_radiusP))
        {
            button = ("t").getBytes();
            double xx = (x-joy_left_center_xP)/joy_left_radiusP;
            double yy = -(y-joy_left_center_yP)/joy_left_radiusP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("joy left").getBytes() );
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%.3f", xx)).getBytes());
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%.3f", yy)).getBytes());
                //outputStream.write( toByteArray(xx) );
                //outputStream.write(toByteArray(yy));
                txt1.setText(outputStream.toString());
                //outputStream.toByteArray();
                theOut = outputStream.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                txt1.setText(e.toString());
                e.printStackTrace();
            }

        }
        /* right joystick */
        else if(
                (Math.sqrt((x-joy_right_center_xP)*(x-joy_right_center_xP) + (y-joy_right_center_yP)*(y-joy_right_center_yP)) < joy_right_radiusP))
        {
            button = ("t").getBytes();
            double xx = (x-joy_right_center_xP)/joy_right_radiusP;
            double yy = -(y-joy_right_center_yP)/joy_right_radiusP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("joy right").getBytes() );
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%.3f", xx)).getBytes());
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%.3f", yy)).getBytes());
                //outputStream.write( toByteArray(xx) );
                //outputStream.write(toByteArray(yy));
                txt1.setText(outputStream.toString());
                //outputStream.toByteArray();
                theOut = outputStream.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                txt1.setText(e.toString());
                e.printStackTrace();
            }

        }

        /* @button */
        else if(
                (Math.sqrt((x-button_at_center_xP)*(x-button_at_center_xP) + (y-button_at_center_yP)*(y-button_at_center_yP)) < button_at_radiusP))
        {
            button = ("t").getBytes();
            double xx = (x-button_at_center_xP)/button_at_radiusP;
            double yy = -(y-button_at_center_yP)/button_at_radiusP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("button @").getBytes() );
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%d", 1)).getBytes());
                //outputStream.write( toByteArray(xx) );
                //outputStream.write(toByteArray(yy));
                txt1.setText(outputStream.toString());
                //outputStream.toByteArray();
                theOut = outputStream.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                txt1.setText(e.toString());
                e.printStackTrace();
            }

        }


        /* #button */
        else if(
                (Math.sqrt((x-button_hash_center_xP)*(x-button_hash_center_xP) + (y-button_hash_center_yP)*(y-button_hash_center_yP)) < button_hash_radiusP))
        {
            button = ("t").getBytes();
            double xx = (x-button_hash_center_xP)/button_hash_radiusP;
            double yy = -(y-button_hash_center_yP)/button_hash_radiusP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("button #").getBytes() );
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%d", 1)).getBytes());
                //outputStream.write( toByteArray(xx) );
                //outputStream.write(toByteArray(yy));
                txt1.setText(outputStream.toString());
                //outputStream.toByteArray();
                theOut = outputStream.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                txt1.setText(e.toString());
                e.printStackTrace();
            }

        }


        /* %button */
        else if(
                (Math.sqrt((x-button_percent_center_xP)*(x-button_percent_center_xP) + (y-button_percent_center_yP)*(y-button_percent_center_yP)) < button_percent_radiusP))
        {
            button = ("t").getBytes();
            double xx = (x-button_percent_center_xP)/button_percent_radiusP;
            double yy = -(y-button_percent_center_yP)/button_percent_radiusP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("button %").getBytes() );
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%d", 1)).getBytes());
                //outputStream.write( toByteArray(xx) );
                //outputStream.write(toByteArray(yy));
                txt1.setText(outputStream.toString());
                //outputStream.toByteArray();
                theOut = outputStream.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                txt1.setText(e.toString());
                e.printStackTrace();
            }

        }

        /* &button */
        else if(
                (Math.sqrt((x-button_and_center_xP)*(x-button_and_center_xP) + (y-button_and_center_yP)*(y-button_and_center_yP)) < button_and_radiusP))
        {
            button = ("t").getBytes();
            double xx = (x-button_and_center_xP)/button_and_radiusP;
            double yy = -(y-button_and_center_yP)/button_and_radiusP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("button &").getBytes() );
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%d", 1)).getBytes());
                //outputStream.write( toByteArray(xx) );
                //outputStream.write(toByteArray(yy));
                txt1.setText(outputStream.toString());
                //outputStream.toByteArray();
                theOut = outputStream.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                txt1.setText(e.toString());
                e.printStackTrace();
            }

        }

        /* start button */
        else if(
                x > (button_start_center_xP-button_start_delta_xP) &
                x < (button_start_center_xP+button_start_delta_xP) &
                y > (button_start_center_yP-button_start_delta_yP) &
                y < (button_start_center_yP+button_start_delta_yP))
        {
            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("button start").getBytes() );
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%d", 1)).getBytes());
                //outputStream.write( toByteArray(xx) );
                //outputStream.write(toByteArray(yy));
                txt1.setText(outputStream.toString());
                //outputStream.toByteArray();
                theOut = outputStream.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                txt1.setText(e.toString());
                e.printStackTrace();
            }

        }

        /* select button */
        else if(
                x > (button_select_center_xP-button_select_delta_xP) &
                x < (button_select_center_xP+button_select_delta_xP) &
                y > (button_select_center_yP-button_select_delta_yP) &
                y < (button_select_center_yP+button_select_delta_yP))
        {
            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("button select").getBytes() );
                outputStream.write( (" ").getBytes() );
                outputStream.write( (String.format("%d", 1)).getBytes());
                //outputStream.write( toByteArray(xx) );
                //outputStream.write(toByteArray(yy));
                txt1.setText(outputStream.toString());
                //outputStream.toByteArray();
                theOut = outputStream.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                txt1.setText(e.toString());
                e.printStackTrace();
            }

        }



        return theOut;
    }
}

