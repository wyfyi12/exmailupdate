/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uitl;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

/**
 *
 * @author -_-
 */
public class timereader {
     @SuppressWarnings("deprecation")
	public static Date getNextFullTime(int time){
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        if(calendar.get(Calendar.HOUR_OF_DAY) == time&&calendar.get(Calendar.MINUTE) == 0 ){
            date =  calendar.getTime();
        }else{
            date =  new Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY) + 1, 0 , 0);
        }
        return date;
    }
     @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                JOptionPane.showMessageDialog(null, "时间到  " + new Date().toLocaleString());
                System.out.println("时间到  " + new Date().toLocaleString());
            }
        }, timereader.getNextFullTime(16) , 3600000);
    }
}
