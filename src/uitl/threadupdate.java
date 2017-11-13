package uitl;

import dao.localdao;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import configure.config;
import dao.userdao;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class threadupdate {

    private static Logger logger = Logger.getLogger(threadupdate.class);

    public static ThreadA update = new ThreadA();
    public static ThreadB check = new ThreadB();
    public static ThreadC newupdate = new ThreadC();

    public void start() throws InterruptedException {
        userdao.setState(0);
        userdao.setCustate(0);
        if (threadupdate.update.isAlive()) {
            ThreadA.sleep(100);
        } else {
            getdata.getconn();
            update.start();
            check.start();
        }
        logger.info("数据同步后台已启动。");
    }

    @SuppressWarnings("deprecation")
    public void stop(JTextArea jt) throws InterruptedException {
        logger.info("线程正在中断。。。");
        if (threadupdate.check.isAlive()) {
            check.stop();
        }
        if (threadupdate.update.isAlive()) {
            userdao.setState(1);
        }
        if (threadupdate.newupdate.isAlive()) {
            userdao.setCustate(1);
        }
        NewJFrame njf = new NewJFrame();
        njf.setI(0);
        jt.append("已经中断线程" + "\n");
    }

    @SuppressWarnings("deprecation")
    public void update(JTextArea jt, JProgressBar jpb) throws InterruptedException {

        newupdate.setJpb(jpb);
        newupdate.setJt(jt);
        check.stop();
        userdao.setCustate(0);
        newupdate.start();

    }

}

class ThreadA extends Thread {

    private static Logger logger = Logger.getLogger(ThreadA.class);

    @Override
    public void run() {
        logger.info("后台同步线程已启动。");
        localdao.getConnection();
        HashMap<String, Integer> tn = localdao.searchtable();
        if (tn.get("localuser") == 1) {
            logger.info("本地数据库已存在");
        } else {
            logger.info("本地数据库不存在,正在新建数据库。。。");
            localdao.createtable();
            logger.info("新建成功。");
        }
        while (true && !isInterrupted()) {
            if (threadupdate.check.isAlive()) {
                try {
                    logger.info("开始同步数据。");
                    int s = 0;
                    int f = 0;
                    List<String> result = null;
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
                    s = 0;
                    f = 0;
                    result.clear();
                    ThreadA.sleep(config.getInterval());
                } catch (InterruptedException e) {

                    logger.error("错误故障为：" + e);

                    return;

                } catch (Exception e) {
                    logger.error("错误故障为：" + e);
                }
            } else {
                logger.error("监控线程已停止。");
                ThreadB check = new ThreadB();
                check.start();
            }
            try {
                Thread.sleep(180000);
            } catch (InterruptedException ex) {
                logger.error("错误故障为：" + ex);
            }
            logger.info("已检查更新");

        }
    }

}

class ThreadB extends Thread {

    private static Logger logger = Logger.getLogger(ThreadB.class);

    @Override
    public void run() {
        logger.info("监控线程已启动。");

        try {
            while (true && !isInterrupted()) {
                if (threadupdate.update.isAlive()) {
                    logger.info("后台同步线程正在运行中");
                } else {
                    logger.error("后台同步线程已停止。");
                    ThreadA update = new ThreadA();
                    update.start();
                }
                Thread.sleep(180000);

            }
        } catch (InterruptedException e) {
            logger.error("错误故障为：" + e);

        }
    }
}

class ThreadC extends Thread {

    private static Logger logger = Logger.getLogger(ThreadC.class);
    private JTextArea jt;
    private JProgressBar jpb;

    public void setJt(JTextArea jt) {
        this.jt = jt;
    }

    public void setJpb(JProgressBar jpb) {
        this.jpb = jpb;
    }

    public ThreadC() {

    }

    @Override
    public void run() {

        try {
            logger.info("中断同步线程。。。");
            jt.append("中断同步线程。。。" + "\n");
            logger.info("开始同步数据。");
            int s = 0;
            int f = 0;
            userdao.setState(1);
            getdata.getconn();
            List<String> result = userdao.cuupdate(jt, jpb);
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).equals("1")) {
                    s++;
                } else {
                    f++;
                }
            }
            logger.info("更新完成。共计成功：" + s + ",失败：" + f);
            jt.append("更新完成。共计成功：" + s + ",失败：" + f + "\n");
            s = 0;
            f = 0;
            result.clear();
            userdao.setState(0);
            NewJFrame njf = new NewJFrame();
            njf.setI(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(threadupdate.newupdate.getState());
            logger.error(e);
            jt.append("出现错误：" + e.toString());
        }

    }
}
