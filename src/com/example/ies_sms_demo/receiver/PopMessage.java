package com.example.ies_sms_demo.receiver;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PopMessage implements Serializable {
 
      /**
	 * 
	 */
	
	public  static String CH_ALARM = "Channel Alarm Message";
	public  static String IP_ALARM = "Input Switch Alarm Message";
	public  static String SYS_STATE = "System State Message";
	private static final long serialVersionUID = 1L;
	public String sender;
	public String body;
	public long timestamp;
	public String msgType;
      // getters and setters go here
      // ...
 
     /**
       * Utility method
       * Display a shorten, more user-friendly readable date from the original timestamp
       */
     public String getShortDate(long timestamp){
        Date date = new Date(timestamp);
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd hh:mmaa");
        sdf.setCalendar(cal);cal.setTime(date);
        return sdf.format(date);
     }
}