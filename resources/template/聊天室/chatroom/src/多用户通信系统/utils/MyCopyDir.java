package 多用户通信系统.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//虽然能跑 但是写的是真垃圾
//笨办法 全程分两种情况 如果进入递归是第一次 即是file是源文件夹 和file不是源文件夹
//的创建file处理全部分开
public class MyCopyDir
{
    static String startdir;
    static String out;
    static File fsourse;
    public static void copyDir(File file,File outfile) throws IOException
    {
        //终止条件
        if (file.list() == null)
            return;
        //当前文件的值操作
        //除了源文件是文件之外 其他情况的文件都直接由文件夹下的文件遍历解决了
        // 不会往下传 然后进入这条语句
        if (file.isFile())//第一层 如果源文件直接是文件 直接不用递归了 直接结束就返回
        {
            System.out.println(file.getAbsolutePath() + "是文件");

        }
        else
        {
            File[] fs = file.listFiles();
            if(file.getAbsolutePath().equals(startdir))
            {
                //创建镜像源文件夹
                File f=new File(out);
                f.mkdir();
                fsourse=f;

            }
            for (File ff : fs)
            {


                if (ff.isFile())
                {
                    System.out.println(ff.getAbsolutePath() + "是文件");
                    //创建镜像文件
                    File f;
                    if(file.getAbsolutePath().equals(startdir))
                    {
                        f=new File(fsourse.getAbsolutePath()+'\\'+ff.getName());
                    }
                    else
                    {
                        f = new File(outfile.getAbsolutePath()+'\\'+ff.getName());
                    }

                    //复制文件内容到镜像文件
                    FileOutputStream fileOutputStream=new FileOutputStream(f);
                    FileInputStream fileInputStream=new FileInputStream(ff);
                    byte[]b=new byte[10000];int len;
                    while((len=fileInputStream.read(b))!=-1)
                    {
                        fileOutputStream.write(b);
                        fileOutputStream.flush();
                    }
                    fileOutputStream.close();
                    fileInputStream.close();


                }
                else
                {

                    //创建镜像文件夹 并传入递归
                    File f;
                    System.out.println(ff.getAbsolutePath() + "是文件夹");
                    if(file.getAbsolutePath().equals(startdir))
                    {
                         f = new File(fsourse.getAbsolutePath() +'\\'+ ff.getName());
                    }
                    else  f=new File(outfile.getAbsolutePath() +'\\'+ ff.getName());
                    f.mkdir();

                   copyDir(ff, f);
                }
            }
        }
    }


    public static void copyDirectiory(String sourceDir, String targetDir)  throws IOException
    {

       String p1=sourceDir;
       String p2="";
       MyCopyDir.startdir=p1;
       MyCopyDir.out=targetDir;
       File file1=new File(p1);
       File file2=new File("");
       file2.mkdir();
       copyDir(file1,file2);
    }

    public static void main(String[] args) throws IOException
    {
        copyDirectiory("D:\\Lab09","D:\\newLab");
    }

}
