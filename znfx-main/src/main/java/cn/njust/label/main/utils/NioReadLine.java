package cn.njust.label.main.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * @program:
 * @description:
 * @
 **/
public class NioReadLine {

    public static void main(String[] args) throws Exception {
        int bufSize = 5000;
        File file = new File("E:\\workspace\\znfx\\资料文件\\AIS_2019_01_01\\AIS_2019_01_04.csv");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fcin = fis.getChannel();
        ByteBuffer rBuffer = ByteBuffer.allocate(bufSize);
        String enterStr = "\n";
        try {
            // 如果出现了换行符，将temp中的内容与换行符之前的内容拼接
            StringBuilder strBuf = new StringBuilder("");
            int lineNum = 0;
            ArrayList<String>  list = null;
            while (fcin.read(rBuffer) != -1) {
                list = new ArrayList<>();
                int rSize = rBuffer.position();
                rBuffer.clear();

                String tempString = new String(rBuffer.array(), 0, rSize);

                if(fis.available() ==0){//最后一行，加入"\n分割符"
                    tempString+="\n";
                }

                int fromIndex = 0;
                int endIndex = 0;

                while ((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {

                    String line = tempString.substring(fromIndex, endIndex);
                    line = strBuf + line;
                    list.add(line);
                    System.out.println(line);
                    strBuf.delete(0, strBuf.length());
                    fromIndex = endIndex + 1;
                }
                if (rSize > tempString.length()) {
                    strBuf.append(tempString.substring(fromIndex));
                }
//                else {
//                    strBuf.append(tempString.substring(fromIndex));
//                }
                System.out.println((list.size()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
