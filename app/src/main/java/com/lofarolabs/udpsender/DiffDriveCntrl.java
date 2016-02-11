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

public class DiffDriveCntrl extends ActionBarActivity {
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
    double slide_left_center_yP, slide_left_center_xP;
    double slide_right_center_yP, slide_right_center_xP;
    double slide_delta_yP, slide_delta_xP;
    double button_star_center_yP, button_star_center_xP;
    double button_and_center_yP, button_and_center_xP;
    double button_dollar_center_yP, button_dollar_center_xP;
    double button_plus_center_yP, button_plus_center_xP;
    double button_at_center_yP, button_at_center_xP;
    double button_octo_center_yP, button_octo_center_xP;

    /* calibration for nexus 7 */
    double touch_center_x = 649.0; double touch_center_y = 399.0;
    double touch_delta_x = 240.0; double touch_delta_y = 160.0;
    double slide_left_center_x = 272.0; double slide_left_center_y = 432.0;
    double slide_right_center_x = 1007.0; double slide_right_center_y = 432.0;
    double slide_delta_x = 70.0; double slide_delta_y = 200.0;
    double button_star_center_x = 74.0; double button_star_center_y = 274.0;
    double button_and_center_x = 74.0; double button_and_center_y = 432.0;
    double button_dollar_center_x = 74.0; double button_dollar_center_y = 554.0;
    double button_plus_center_x = 1214.0; double button_plus_center_y = 274.0;
    double button_at_center_x = 1214.0; double button_at_center_y = 432.0;
    double button_octo_center_x = 1214.0; double button_octo_center_y = 554.0;
    double button_radius = 40.0;


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
        double device_width = displayMetrics.widthPixels;  double conversion_y = device_width/1280;
        double device_height = displayMetrics.heightPixels; double conversion_x = device_height/720;

        touch_center_yP = touch_center_y * conversion_y;
        touch_center_xP = touch_center_x * conversion_x;
        touch_delta_yP = touch_delta_y * conversion_y;
        touch_delta_xP = touch_delta_x * conversion_x;
        slide_left_center_yP = slide_left_center_y * conversion_y;
        slide_left_center_xP = slide_left_center_x * conversion_x;
        slide_delta_xP = slide_delta_x * conversion_x;
        slide_right_center_yP = slide_right_center_y*conversion_y;
        slide_right_center_xP = slide_right_center_x*conversion_x;
        slide_delta_yP = slide_delta_y * conversion_y;
        button_star_center_xP = button_star_center_x * conversion_x;
        button_star_center_yP = button_star_center_y * conversion_y;
        button_and_center_yP = button_and_center_y * conversion_y;
        button_and_center_xP = button_and_center_x * conversion_x;
        button_dollar_center_xP = button_dollar_center_x * conversion_x;
        button_dollar_center_yP = button_dollar_center_y * conversion_y;
        button_plus_center_xP = button_plus_center_x * conversion_x;
        button_plus_center_yP = button_plus_center_y * conversion_y;
        button_at_center_yP = button_at_center_y * conversion_y;
        button_at_center_xP = button_at_center_x * conversion_x;
        button_octo_center_xP = button_octo_center_x * conversion_x;
        button_octo_center_yP = button_octo_center_y * conversion_y;


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





        setContentView(R.layout.activity_diff_drive_cntrl);
        txt1   = (TextView)findViewById(R.id.message_center);
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
        //
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
        getMenuInflater().inflate(R.menu.menu_diff_drive_cntrl, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),
                        "On This Page",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.main_cntrl:
                Toast.makeText(getApplicationContext(),
                        "Switching Screens",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainSend.class));

                break;
        }

        return true;
    }

    /* Location information */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        //vibrator.vibrate(500); // 0.5 seconds

        int x = (int)event.getX();
        int y = (int)event.getY();
        txt_touch_x   = (TextView)findViewById(R.id.touch_x_view);
        txt_touch_y   = (TextView)findViewById(R.id.touch_y_view);
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





        txt1.setText("jasa");
        for (int size = event.getPointerCount(), i = 0; i < size; i++) {
            txt1.setText("jasb");
            PointF point = mActivePointers.get(event.getPointerId(i));
            txt1.setText("jasc");
            if (1==1) {
                txt1.setText("jasd");
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
        txt_port   = (EditText)findViewById(R.id.port_num);
        mPort = Integer.parseInt(txt_port.getText().toString());
        client_socket = new DatagramSocket(mPort);
        bt_open_port.setText("Port Open");
    }
    public void client_send() throws IOException {
        //SystemClock.sleep(1000);
        txt_ip   = (EditText)findViewById(R.id.address_input);
        InetAddress IPAddress =  InetAddress.getByName(txt_ip.getText().toString());
        str="jas1";
        send_data = str.getBytes();
        DatagramPacket send_packet = new DatagramPacket(send_data,str.length(), IPAddress, mPort);
        client_socket.send(send_packet);
    }

    public void client_send_buff(String buff) throws IOException {
        //SystemClock.sleep(1000);
        txt_ip   = (EditText)findViewById(R.id.address_input);
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
        /* left slider */
        else if(
                x > (slide_left_center_xP-slide_delta_xP) &
                x < (slide_left_center_xP+slide_delta_xP) &
                y > (slide_left_center_yP-slide_delta_yP) &
                y < (slide_left_center_yP+slide_delta_yP))
        {
            button = ("t").getBytes();
            double xx = (x-slide_left_center_xP)/slide_delta_xP;
            double yy = -(y-slide_left_center_yP)/slide_delta_yP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("left slider").getBytes() );
                //outputStream.write( (" ").getBytes() );
                //outputStream.write( (String.format("%.3f", xx)).getBytes());
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
        /* right slider */
        else if(
                x > (slide_right_center_xP-slide_delta_xP) &
                x < (slide_right_center_xP+slide_delta_xP) &
                y > (slide_right_center_yP-slide_delta_yP) &
                y < (slide_right_center_yP+slide_delta_yP))
        {
            button = ("t").getBytes();
            double xx = (x-slide_right_center_xP)/slide_delta_xP;
            double yy = -(y-slide_left_center_yP)/slide_delta_yP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("right slider").getBytes() );
                //outputStream.write( (" ").getBytes() );
                //outputStream.write( (String.format("%.3f", xx)).getBytes());
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

        /* *button */
        else if(
                (Math.sqrt((x-button_star_center_xP)*(x-button_star_center_xP) + (y-button_star_center_yP)*(y-button_star_center_yP)) < button_radius))
        {
            button = ("t").getBytes();
            //double xx = (x-button_star_center_xP)/button_radiusP;
            //double yy = -(y-button_star_center_yP)/button_radiusP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("button *").getBytes() );
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
                (Math.sqrt((x-button_and_center_xP)*(x-button_and_center_xP) + (y-button_and_center_yP)*(y-button_and_center_yP)) < button_radius))
        {
            button = ("t").getBytes();
            //double xx = (x-button_and_center_xP)/button_radiusP;
            //double yy = -(y-button_and_center_yP)/button_radiusP;

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

        /* $button */
        else if(
                (Math.sqrt((x-button_dollar_center_xP)*(x-button_dollar_center_xP) + (y-button_dollar_center_yP)*(y-button_dollar_center_yP)) < button_radius))
        {
            button = ("t").getBytes();
            //double xx = (x-button_star_center_xP)/button_radiusP;
            //double yy = -(y-button_star_center_yP)/button_radiusP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("button $").getBytes() );
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

        /* +button */
        else if(
                (Math.sqrt((x-button_plus_center_xP)*(x-button_plus_center_xP) + (y-button_plus_center_yP)*(y-button_plus_center_yP)) < button_radius))
        {
            button = ("t").getBytes();
            //double xx = (x-button_plus_center_xP)/button_radiusP;
            //double yy = -(y-button_plus_center_yP)/button_radiusP;

            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write( ("button +").getBytes() );
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

        /* @button */
        else if(
                (Math.sqrt((x-button_at_center_xP)*(x-button_at_center_xP) + (y-button_at_center_yP)*(y-button_at_center_yP)) < button_radius))
        {
            button = ("t").getBytes();
            //double xx = (x-button_at_center_xP)/button_radiusP;
            //double yy = -(y-button_at_center_yP)/button_radiusP;

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
                (Math.sqrt((x-button_octo_center_xP)*(x-button_octo_center_xP) + (y-button_octo_center_yP)*(y-button_octo_center_yP)) < button_radius))
        {
            button = ("t").getBytes();
            //double xx = (x-button_octo_center_xP)/button_radiusP;
            //double yy = -(y-button_octo_center_yP)/button_radiusP;

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

        return theOut;
    }
}

