package com.example;

import com.example.basic.Operation;

import java.util.ArrayList;

/**
 *
 * Created by Administrator on 2016/7/5.
 */
public class ApkTool22_huobaoyingyuan extends Operation
{
    private static final String HOME = "D:\\360files\\1_Crack\\3_apktool2.2\\";

    public ApkTool22_huobaoyingyuan()
    {
        super(HOME);
    }

    /**
     * prohibit manifest replacement
     * @param list unused
     * @param index unused
     */
    @Override
    public void replaceChannelNameInManifest(ArrayList<String> list, int index)
    {
        String manifest= mHome + "YOU\\AndroidManifest.xml";
        String contentString = read(manifest);

        String first;
        String middle;
        String last = "\"";

        first = "<meta-data android:name=\"channelId\" android:value=\"";
        middle = getmiddle(contentString,first,last);
        contentString=contentString.replace(first+middle+last, first+list.get(index)+last);

        write(contentString, manifest);
    }
}
