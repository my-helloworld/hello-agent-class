package com.chpengzh.agent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 检测方法的执行时间
 */
public class MyTransformer implements ClassFileTransformer {

    private static final String HTTP_SERVLET = "org.springframework.web.servlet.DispatcherServlet";

    private static final String METHOD_NAME = "doService";

    private static final String MAGIC_SUFFIX = "$decorated";

    @Override
    public byte[] transform(
        ClassLoader loader,
        String className,
        Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain,
        byte[] classfileBuffer
    ) throws IllegalClassFormatException {
        className = className.replace("/", ".");

        if (HTTP_SERVLET.equals(className)) {
            try {
                return decorateServlet(className);
            } catch (NotFoundException | CannotCompileException | IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private byte[] decorateServlet(String className)
        throws NotFoundException, CannotCompileException, IOException {
        System.out.println("[你被魔改了你知道么?]" + className);

        // 使用全称,用于取得字节码类<使用javassist>
        CtClass ctclass = ClassPool.getDefault().get(className);
        CtMethod ctmethod = ctclass.getDeclaredMethod(METHOD_NAME);// 得到这方法实例

        ctmethod.setName(METHOD_NAME + MAGIC_SUFFIX);// 将原来的方法名字修改

        // 创建新的方法，复制原来的方法，名字为原来的名字
        CtMethod newMethod = CtNewMethod.copy(ctmethod, METHOD_NAME, ctclass, null);

        // 构建新的方法体
        String bodyStr = "{\n" +
            "    javax.servlet.http.HttpServletResponse resp = $2;\n" +
            "    long current = System.currentTimeMillis();\n" +
            "    try {\n" +
            "        " + METHOD_NAME + MAGIC_SUFFIX + "($$);\n" +
            "\n" +
            "    } finally {\n" +
            "        long cast = System.currentTimeMillis() - current;\n" +
            "        System.out.println(\"统计方法:name=" + METHOD_NAME + ",status=\"+ \n" +
            "            resp.getStatus() + \",cast=\" + cast +\"ms\");\n" +
            "        \n" +
            "    }\n" +
            "}\n";
        System.out.println(bodyStr);
        newMethod.setBody(bodyStr);// 替换新方法
        ctclass.addMethod(newMethod);// 增加新方法
        return ctclass.toBytecode();
    }
}