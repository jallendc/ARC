package com.lofarolabs.udpsender;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DiffDriveCntrl extends ActionBarActivity {


    /* configuration for 1080 x 1920 Resolution  */
    double touch_center_y = 632 ;
    double touch_center_x = 959;
    double touch_delta_x = 368;
    double touch_delta_y = 256;
    double slideL_center_x = 441;
    double slideL_center_y = 632;
    double slide_delta_x = 87;
    double slide_delta_y = 375;
    double slideR_center_x = 1477;
    double slideR_center_y = 632;
    /**double button_oneL_y = ;
    double button_oneL_x = ;
    double button_twoL_y = ;
    double button_twoL_x = ;
    double button_threeL_y = ;
    double button_threeL_x = ;
    double button_oneR_y = ;
    double button_oneR_x = ;
    double Button_twoR_x_y = ;
    double button_twoR_x = ;
    double button_threeR_y = ;
    double button_threeR_x = ;**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_drive_cntrl);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.main_cntrl) {
            startActivity(new Intent(DiffDriveCntrl.this, MainSend.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
