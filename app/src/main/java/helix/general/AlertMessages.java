package helix.general;

/**
 *  Ridio v1.0.1
 * 	Purpose	   : Custom Alert Producer
 *  Created by : Abish
 *  Created Dt : 15-02-2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 */
import helix.ridioandroidstudio.HotelMaster;
import helix.ridioandroidstudio.NewCustomer;
import helix.ridioandroidstudio.RateMasterClass;
import helix.ridioandroidstudio.ReturnVehicle;
import helix.ridioandroidstudio.VehicleMasterClass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


public class AlertMessages {

    private Context context;

    public AlertMessages(Context contex){
        this.context = contex;
    }

    /**Alert with single button*/
    public void SingleButtonAlert(String title,String message,String but_txt){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(but_txt,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**Alert with Double button Rate Master*/
    public void alertDoubleButton(String title,String message,String but1_txt,String but2_txt){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(but1_txt,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
            }
        });
        alertDialogBuilder.setNeutralButton(but2_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((RateMasterClass) context).reloadActivityAgain("oncreate");;
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**Alert for wrong from Date*/
    public void alertMessageWrongFromDate() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("New Customer");
        alertDialogBuilder.setMessage("Wrong Date Entry.!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Reset",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                ((NewCustomer) context).callDateTimePicker(1);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**Alert for wrong to Date*/
    public void alertMessageWrongToDate() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("New Customer");
        alertDialogBuilder.setMessage("Wrong Date Entry.!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Reset",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                ((NewCustomer) context).callDateTimePicker(2);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /** Date Alerts for vehicle master*/
    public void alertWrongDate(String tit, String msg, String btn,final int calender_val) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(tit);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton(btn,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                    ((VehicleMasterClass)context).showDialog(calender_val);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /** Vehicle Master reload, Hotel Mastr to Dashboard, New Booking, Rate Master Reload*/
    public void alertClassClasses(String cls, String message, final String purpose) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(cls);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                if(purpose.equals("ReloadVehicleMaster"))
                    ((VehicleMasterClass)context).reloadActivityAgain("oncreate");
                else if(purpose.equals("callDashboardFromHotel"))
                    ((HotelMaster)context).callDashboard();
                else if(purpose.equals("reloadBookings"))
                    ((NewCustomer) context).reloadActity();
                else if(purpose.equals("returnVehicle"))
                    ((ReturnVehicle) context).onBackPressed();
                else if(purpose.equals("ratemaster"))
                    ((RateMasterClass) context).reloadActivityAgain("oncreate");
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void alertThreeButtonWarning(String tit,String msg,final String b1,final String b2,final String b3) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(tit);
        alertDialogBuilder.setMessage(msg);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(b1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((VehicleMasterClass)context).triplAlertResult(1);
            }
        });
        alertDialogBuilder.setNeutralButton(b2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialogBuilder.setNegativeButton(b3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((VehicleMasterClass)context).triplAlertResult(3);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
