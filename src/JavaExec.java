import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class JavaExec 
{
	public static String appname;
	public static void main(String[] args) 
	{
       System.out.println("/n Usage:java JavaExec command or  java JavaExec");
	   
		JavaExec javaExec = new  JavaExec();
		ArrayList<String> channellist = new ArrayList<String>();
		//readTxtFile(channellist,"D:\\360files\\1_Crack\\2_Work\\achannellist.txt");
		readTxtFile(channellist,"D:\\360files\\1_Crack\\3_apktool2.2\\achannellist.txt");
		
		for(int index=0;index<channellist.size();index++)
		{
			String targetFilename = appname+"-"+getdate()+"-"+channellist.get(index)+".apk";
			javaExec.oneChannel(channellist,index,targetFilename);
		}
	}
	
	public void oneChannel(ArrayList<String> channellist,int index,String targetFilename)  
    {  
		replaceChannelName(channellist,index);
        /*executePackage("6_0_1_new_YOU.bat");
        executePackage("6_0_2_sign_YOU.bat <D:\\360files\\1_Crack\\2_Work\\apassword.txt");
        executePackage("6_0_3_copy_YOU.bat");
        String fromfile="D:\\360files\\1_Crack\\2_Work\\YOU_signed.apk";
		String toFile="D:\\360files\\1_Crack\\2_Work\\"+targetFilename;*/
		executePackage("6_0_1_new_YOU.bat");
        executePackage("6_0_2_sign_YOU.bat <D:\\360files\\1_Crack\\3_apktool2.2\\apassword.txt");
        executePackage("6_0_3_copy_YOU.bat");
        String fromfile="D:\\360files\\1_Crack\\3_apktool2.2\\YOU_signed.apk";
		String toFile="D:\\360files\\1_Crack\\3_apktool2.2\\"+targetFilename;
		renameAppFile(fromfile, toFile);
   }

	private void replaceChannelName(ArrayList<String> list,int index)
	{
		String manifest="D:\\360files\\1_Crack\\3_apktool2.2\\YOU\\AndroidManifest.xml";
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

	private String getmiddle(String contentString, String first, String last)
	{
		int index = contentString.indexOf(first);
		String string = contentString.substring(index+first.length());
		return string.split(last)[0];
	}

	private void executePackage(String command)
	{
		try  
        {  
	        String home="D:\\360files\\1_Crack\\3_apktool2.2\\";
	        String cmd = home+command;
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
	
	public static void readTxtFile(ArrayList<String> channellist, String filePath)
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
                    	JavaExec.appname=str[1];
                    }
                    else
                    	channellist.add(lineTxt);
                }
                read.close();
            }
            else
            {
            	System.out.println("找不到指定的文件");
            }
        } 
        catch (Exception e) 
        {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
	
	public static String getdate()
	{
		Date date = new Date();
		String dataString=date.toLocaleString().split(" ")[0].replace(":", "");
		return dataString;
	}
	
	public void renameAppFile(String fromfile, String toFile) 
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
	
	public static String read(String path) 
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

    public static boolean write(String cont, String dist) 
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
}