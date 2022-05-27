package cn.njust.label.main.utils;


public class ProgressBarUtil {

    public static void Print_ProgressBar(int start, int cur, int end, String str){
        if(start == cur){
            int progress = (int)((double)(cur-start+1)/(end-start+1)*100);
            System.out.print(str + progress + "%");
        }
        else{
            int progress_cur = (int)((double)(cur-start+1)/(end-start+1)*100);
            int progress_pre = (int)((double)(cur-start)/(end-start+1)*100);
            if(progress_pre >= 10){
                System.out.print("\b\b\b");
                System.out.print(progress_cur + "%");
            }
            else{
                System.out.print("\b\b");
                System.out.print(progress_cur + "%");
            }
        }
        if(end == cur){
            System.out.println();
        }
    }
}
