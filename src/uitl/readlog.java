/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uitl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;

/**
 *
 * @author nantu
 */
public class readlog {
    public static List<String> log=new ArrayList<String>();
     private static Logger logger = Logger.getLogger(readlog.class);
    public static List<String> readTxtFile(String filePath,JTextArea jt ){

        try {
                
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file));//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        jt.append(lineTxt+"\n");

                    }
                    read.close();
        }else{
                    logger.error("找不到指定的文件");
            
        }
        } catch (Exception e) {
             logger.error("读取日志出错："+e);
        }
     return  log;
    }
}
