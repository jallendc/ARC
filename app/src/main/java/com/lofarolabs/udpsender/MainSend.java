package com.lofarolabs.udpsender;

import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
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

    /* Adjustment for screen for x and y values for azpen tablet */
    double y_adjust = 0.4167;
    double x_adjust = 0.3333;

    /* calibration for nexus 7 */
    /**double touch_center_y = 660.0*y_adjust;
    double touch_center_x = 960.0*x_adjust;
    double touch_delta_x = 400.0*x_adjust;
    double touch_delta_y = 280.0*y_adjust;
    double joy_left_center_x = 245.0*x_adjust;
    double joy_left_center_y = 880.0*y_adjust;
    double joy_right_center_x = 1685.0*x_adjust;
    double joy_right_center_y = 880.0*y_adjust;
    double joy_right_radius = 180.0*y_adjust;
    double joy_left_radius = 180.0*y_adjust;
    double button_and_center_x = 1680.0*x_adjust;
    double button_and_center_y = 570.0*y_adjust;
    double button_and_radius = 60.0*y_adjust;
    double button_at_center_x = 1680.0*x_adjust;
    double button_at_center_y = 285.0*y_adjust;
    double button_at_radius = 60.0*y_adjust;
    double button_hash_center_x = 1520.0*x_adjust;
    double button_hash_center_y = 425.0*y_adjust;
    double button_hash_radius = 60.0*y_adjust;
    double button_percent_center_x = 1825.0*x_adjust;
    double button_percent_center_y = 425.0*y_adjust;
    double button_percent_radius = 60.0*y_adjust;
    double button_start_center_x = 365.0*x_adjust;
    double button_start_center_y = 630.0*y_adjust;
    double button_start_delta_x = 95.0*x_adjust;
    double button_start_delta_y = 45.0*y_adjust;
    double button_select_center_x = 120.0*x_adjust;
    double button_select_center_y = 630.0*y_adjust;
    double button_select_delta_x = 95.0*x_adjust;
    double button_select_delta_y = 45.0*y_adjust;**/

    /* calibration for nexus 7 */
    double touch_center_y = 660.0;
    double touch_center_x = 960.0;
    double touch_delta_x = 400.0;
    double touch_delta_y = 280.0;
    double joy_left_center_x = 245.0;
    double joy_left_center_y = 880.0;
    double joy_right_center_x = 1685.0;
    double joy_right_center_y = 880.0;
    double joy_right_radius = 180.0;
    double joy_left_radius = 180.0;
    double button_and_center_x = 1680.0;
    double button_and_center_y = 570.0;
    double button_and_radius = 60.0;
    double button_at_center_x = 1680.0;
    double button_at_center_y = 285.0;
    double button_at_radius = 60.0;
    double button_hash_center_x = 1520.0;
    double button_hash_center_y = 425.0;
    double button_hash_radius = 60.0;
    double button_percent_center_x = 1825.0;
    double button_percent_center_y = 425.0;
    double button_percent_radius = 60.0;
    double button_start_center_x = 365.0;
    double button_start_center_y = 630.0;
    double button_start_delta_x = 95.0;
    double button_start_delta_y = 45.0;
    double button_select_center_x = 120.0;
    double button_select_center_y = 630.0;
    double button_select_delta_x = 95.0;
    double button_select_delta_y = 45.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher_arc);



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

        if (id == R.id.diff_cntrl) {
            startActivity(new Intent(MainSend.this, DiffDriveCntrl.class));
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
                x > (touch_center_x-touch_delta_x) &
                x < (touch_center_x+touch_delta_x) &
                y > (touch_center_y-touch_delta_y) &
                y < (touch_center_y+touch_delta_y))
        {
            button = ("t").getBytes();
            double xx = (x-touch_center_x)/touch_delta_x;
            double yy = -(y-touch_center_y)/touch_delta_y;

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
                (Math.sqrt((x-joy_left_center_x)*(x-joy_left_center_x) + (y-joy_left_center_y)*(y-joy_left_center_y)) < joy_left_radius))
        {
            button = ("t").getBytes();
            double xx = (x-joy_left_center_x)/joy_left_radius;
            double yy = -(y-joy_left_center_y)/joy_left_radius;

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
                (Math.sqrt((x-joy_right_center_x)*(x-joy_right_center_x) + (y-joy_right_center_y)*(y-joy_right_center_y)) < joy_right_radius))
        {
            button = ("t").getBytes();
            double xx = (x-joy_right_center_x)/joy_right_radius;
            double yy = -(y-joy_right_center_y)/joy_right_radius;

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
                (Math.sqrt((x-button_at_center_x)*(x-button_at_center_x) + (y-button_at_center_y)*(y-button_at_center_y)) < button_at_radius))
        {
            button = ("t").getBytes();
            double xx = (x-button_at_center_x)/button_at_radius;
            double yy = -(y-button_at_center_y)/button_at_radius;

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
                (Math.sqrt((x-button_hash_center_x)*(x-button_hash_center_x) + (y-button_hash_center_y)*(y-button_hash_center_y)) < button_hash_radius))
        {
            button = ("t").getBytes();
            double xx = (x-button_hash_center_x)/button_hash_radius;
            double yy = -(y-button_hash_center_y)/button_hash_radius;

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
                (Math.sqrt((x-button_percent_center_x)*(x-button_percent_center_x) + (y-button_percent_center_y)*(y-button_percent_center_y)) < button_percent_radius))
        {
            button = ("t").getBytes();
            double xx = (x-button_percent_center_x)/button_percent_radius;
            double yy = -(y-button_percent_center_y)/button_percent_radius;

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
                (Math.sqrt((x-button_and_center_x)*(x-button_and_center_x) + (y-button_and_center_y)*(y-button_and_center_y)) < button_and_radius))
        {
            button = ("t").getBytes();
            double xx = (x-button_and_center_x)/button_and_radius;
            double yy = -(y-button_and_center_y)/button_and_radius;

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
                x > (button_start_center_x-button_start_delta_x) &
                x < (button_start_center_x+button_start_delta_x) &
                y > (button_start_center_y-button_start_delta_y) &
                y < (button_start_center_y+button_start_delta_y))
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
                x > (button_select_center_x-button_select_delta_x) &
                x < (button_select_center_x+button_select_delta_x) &
                y > (button_select_center_y-button_select_delta_y) &
                y < (button_select_center_y+button_select_delta_y))
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

