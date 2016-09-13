package com.example;

public class JavaExec
{
    public static void main(String[] args)
    {
        System.out.println("/n Usage:java JavaExec command or  java JavaExec");

        apktool22_huobaoyingyuan();
    }

    private static void apktool22_huobaoyingyuan()
    {
        ApkTool22_huobaoyingyuan apkTool22=new ApkTool22_huobaoyingyuan();
        apkTool22.work();
    }

    private static void apktool22_noManifestChanged()
    {
        ApkTool22_noChange apkTool22NoChange =new ApkTool22_noChange();
        apkTool22NoChange.work();
    }
}