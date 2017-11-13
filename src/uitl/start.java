/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uitl;

import dao.userdao;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author -_-
 */
@SuppressWarnings("deprecation")
public class start {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(start.class);

	public void upstart1() throws Exception {
		long daySpan = 24 * 60 * 60 * 1000;

		// 规定的每天时间15:33:30运行
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd '01:00:00'");
		// 首次运行时间
		Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));

		// 如果今天的已经过了 首次运行时间就改为明天
		if (System.currentTimeMillis() > startTime.getTime())
			startTime = new Date(startTime.getTime() + daySpan);

		Timer t = new Timer();

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// 要执行的代码
				try {
					int s = 0;
					int f = 0;
					List<String> result=null;
					try {
						result = userdao.queryupdate();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (int i = 0; i < result.size(); i++) {
						if (result.get(i).length() < 2) {
							s++;
						} else {
							f++;
							logger.info(result.get(i));
						}
					}
					logger.info("更新完成。共计成功：" + s + ",失败：" + f);
					if (f > 10) {
						s = 0;
						f = 0;
						result.clear();
						Calendar calendar = Calendar.getInstance();
						upstart(calendar.get(Calendar.HOUR_OF_DAY) + 1);
					} else {
						s = 0;
						f = 0;
						result.clear();
						upstart(1);
					}

				} catch (Exception ex) {
					Logger.getLogger(start.class.getName()).log(Level.SEVERE, null, ex);
				}
				System.out.println("时间到  " + new Date().toLocaleString());
			}

		};

		// 以每24小时执行一次
		t.scheduleAtFixedRate(task, startTime, daySpan);
	}

	public void upstart(int time) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					int s = 0;
					int f = 0;
					List<String> result=null;
					try {
						result = userdao.queryupdate();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (int i = 0; i < result.size(); i++) {
						if (result.get(i).length() < 2) {
							s++;
						} else {
							f++;
							logger.info(result.get(i));
						}
					}
					logger.info("更新完成。共计成功：" + s + ",失败：" + f);
					if (f > 10) {
						s = 0;
						f = 0;
						result.clear();
						Calendar calendar = Calendar.getInstance();
						upstart(calendar.get(Calendar.HOUR_OF_DAY) + 1);
					} else {
						s = 0;
						f = 0;
						result.clear();
						upstart(1);
					}

				} catch (Exception ex) {
					Logger.getLogger(start.class.getName()).log(Level.SEVERE, null, ex);
				} 
				System.out.println("时间到  " + new Date().toLocaleString());
			}
		}, timereader.getNextFullTime(time), 3600000);
	}
}
