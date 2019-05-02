package com.chpengzh.agent;

import java.lang.instrument.Instrumentation;

public class MyAgent {

    /**
     * 该方法在main方法之前运行，与main方法运行在同一个JVM中
     * 并被同一个System ClassLoader装载
     * 被统一的安全策略(security policy)和上下文(context)管理
     */
    public static void premain(
        String agentOps,
        Instrumentation inst
    ) {
        System.out.println("=========我做了一件很危险的事！！========");
        inst.addTransformer(new MyTransformer());
    }

}