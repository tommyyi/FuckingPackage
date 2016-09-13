package com.example;

import com.example.basic.Operation;

import java.util.ArrayList;

/**
 *
 * Created by Administrator on 2016/7/5.
 */
public class ApkTool22_noChange extends Operation
{
    private static final String HOME = "D:\\360files\\1_Crack\\3_apktool2.2\\";

    public ApkTool22_noChange()
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
        System.out.print("\r\nwe ignore manifest changing\r\n");
    }
}
