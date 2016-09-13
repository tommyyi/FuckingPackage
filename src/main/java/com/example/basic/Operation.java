package com.example.basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/5.
 */
public abstract class Operation
{
    private static final String CONFIGFILENAME = "achannellist.txt";
    private final ArrayList<String> mChannellist = new ArrayList<String>();
    private String mAppName;
    protected String mHome;

    public Operation(String home)
    {
        mHome = home;
        initiateAppNameAndChannelList(mHome+ CONFIGFILENAME);
    }

    private void initiateAppNameAndChannelList(String filePath)
    {
        try
        {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists())
            {
                //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null)
                {
                    System.out.println(lineTxt);
                    if(lineTxt.contains("appname"))
                    {
                        String str[] = lineTxt.split(":");
                        mAppName =str[1];
                    }
                    else
                        mChannellist.add(lineTxt);
                }
                read.close();
            }
            else
            {
                System.out.println(""/*"找不到指定的文件"*/);
            }
        }
        catch (Exception e)
        {
            System.out.println(""/*"读取文件内容出错"*/);
            e.printStackTrace();
        }
    }

    private String getdate()
    {
        Date date = new Date();
        String dataString=date.toLocaleString().split(" ")[0].replace(":", "");
        return dataString;
    }

    protected String read(String path)
    {
        StringBuffer res = new StringBuffer();
        String line = null;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
            while ((line = reader.readLine()) != null)
            {
                res.append(line + "\n");
            }
            reader.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return res.toString();
    }

    protected boolean write(String cont, String dist)
    {
        try
        {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File(dist)),"utf-8");
            writer.write(cont);
            writer.flush();
            writer.close();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private void oneChannelPkg(ArrayList<String> channellist, int index, String targetFilename)
    {
        replaceChannelNameInManifest(channellist,index);
        /*executePackage("6_0_1_new_YOU.bat");
        executePackage("6_0_2_sign_YOU.bat <D:\\360files\\1_Crack\\2_Work\\apassword.txt");
        executePackage("6_0_3_copy_YOU.bat");
        String fromfile="D:\\360files\\1_Crack\\2_Work\\YOU_signed.apk";
		String toFile="D:\\360files\\1_Crack\\2_Work\\"+targetFilename;*/
        executePackage("6_0_1_new_YOU.bat");
        executePackage("6_0_2_sign_YOU.bat <" + mHome + "apassword.txt");
        executePackage("6_0_3_copy_YOU.bat");
        String fromfile= mHome + "YOU_signed.apk";
        String toFile= mHome +targetFilename;
        renameAppFile(fromfile, toFile);
    }

    public void replaceChannelNameInManifest(ArrayList<String> list, int index)
    {
        String manifest= mHome + "YOU\\AndroidManifest.xml";
        String contentString = read(manifest);

        String first;
        String middle;
        String last = "\"";

        first = "<meta-data android:name=\"UMENG_CHANNEL\" android:value=\"";
        middle = getmiddle(contentString,first,last);
        contentString=contentString.replace(first+middle+last, first+list.get(index)+last);

        first = "<meta-data android:name=\"com.snowfish.channel\" android:value=\"";
        middle = getmiddle(contentString,first,last);
        contentString=contentString.replace(first+middle+last, first+list.get(index)+last);

        first = "<meta-data android:name=\"mjfm\" android:value=\"";
        middle = getmiddle(contentString,first,last);
        contentString=contentString.replace(first+middle+last, first+list.get(index)+last);

        first = "<meta-data android:name=\"channelId\" android:value=\"";
        middle = getmiddle(contentString,first,last);
        contentString=contentString.replace(first+middle+last, first+list.get(index)+last);

        write(contentString, manifest);
    }

    protected String getmiddle(String contentString, String first, String last)
    {
        int index = contentString.indexOf(first);
        String string = contentString.substring(index+first.length());
        return string.split(last)[0];
    }

    /**
     * using process.getOutputStream().close() to avoid that requiring user to press key "Enter" to go on
     * @param command
     */
    private void executePackage(String command)
    {
        try
        {
            String cmd = mHome +command;
            final Process process = Runtime.getRuntime().exec(cmd);

            new Thread(new StreamDrainer(process.getInputStream())).start();
            new Thread(new StreamDrainer(process.getErrorStream())).start();

            process.getOutputStream().close();
            int exitValue = process.waitFor();
            System.out.println("Return Value:" + exitValue);
            process.destroy();
        }
        catch(Exception e)
        {
            System.out.println("Exception:"+e.getMessage() );
        }
    }

    private void renameAppFile(String fromfile, String toFile)
    {
        File toBeRenamed = new File(fromfile);
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory())
        {
            System.out.println("File does not exist: " + fromfile);
            return;
        }

        File newFile = new File(toFile);
        if (newFile.exists())
        {
            newFile.delete();
            System.out.println("File deleted: " + toFile);
            newFile = new File(toFile);
        }

        //修改文件名
        if (toBeRenamed.renameTo(newFile))
        {
            System.out.println("File has been renamed.");
        }
        else
        {
            System.out.println("Error rename file");
        }
    }

    public void work()
    {
        for(int index=0;index<mChannellist.size();index++)
        {
            String targetFilename = mAppName+"-"+getdate()+"-"+mChannellist.get(index)+".apk";
            oneChannelPkg(mChannellist,index,targetFilename);
        }
    }
}
