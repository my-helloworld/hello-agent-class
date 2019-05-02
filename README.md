# Hello Agent Class

DEMO: 为一个 springboot 应用添加`org.springframework.web.servlet.DispatcherServlet`调用耗时

构建:

```
mvn clean install
```

运行demo

```
java -javaagent:demo-agent/target/demo-agent-1.0-SNAPSHOT.jar \
   -jar demo-spring-app/target/demo-spring-app-1.0-SNAPSHOT.jar
```

执行一次调用

```
### curl http://127.0.0.1:8080\?name=chpengzh -i
HTTP/1.1 200 
Content-Type: text/plain;charset=UTF-8
Content-Length: 15
Date: Thu, 02 May 2019 08:43:03 GMT

Hello, chpengzh%
```

从console中可以看到调用栈与调用耗时日志

其中`org.springframework.web.servlet.DispatcherServlet.doService$decorated`为被监控的转化函数

```
...
org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1038)
org.springframework.web.servlet.DispatcherServlet.doService$decorated(DispatcherServlet.java:942)
org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java)
org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005)
org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:897)
...
统计方法:name=doService,status=200,cast=35ms
```