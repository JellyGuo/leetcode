Spring
IoC 控制反转，容器、java 反射、xml 解析器=>思想：工厂模式获取 bean，而不是主动 new
AOP：切面，不侵入业务代码，原理：动态代理

SpringBoot
SpringBoot启动项目后执行方法的几种方式：
    1.继承 CommandLineRunner或 ApplicationRunner接口，实现 run 方法，可以通过@Order 注解决定执行顺序
    2.JDK提供的@PostConstruct注解 静态代码块，spring 在扫描 component 时会初始化类
    3.ApplicationContextAware/ApplicationListener/InitializingBean接口
启动方式：4种，其中 3 种借助内置 tomcat 容器，1种借助外部 tomcat 容器
    1.App.java 通过SpringBootApplication注解
    @SpringBootApplication
    class App{
        main(String[] args){
            SpringApplication.run(App.class,args)
        }
    }
    2.@SpringBootApplication=@SpringBootConfiguration+@EnableAutoConfiguration 和 @ComponentScan(basePackages="com")
    3.注解放到 controller 中启动
@SpringBootConfiguration：表示一个类提供 SpringBoot应用程序@Configuration被标注的类等于在Spring的XML配置文件中（applicationContext.xml）,装配所有bean事务，提供了一个Spring的上下文环境
@EnableAutoConfiguration 自动装配核心：原理  在类路径的META-INF/spring.factories文件中找到所有的对应配置类，然后将这些自动配置类加载到spring容器中。
    @AutoConfigurationPackage：该注解上有一个@Import({Registrar.class})注解，
        其中Registrar类的作用是将启动类所在的包下的所有子包组件扫描注入到spring容器中。（因此这就是为什么将controller、service等包放在启动类的同级目录下的原因）
    @Import({AutoConfigurationImportSelector.class})：
        其中AutoConfigurationImportSelector类中有一个getCandidateConfigurations()方法，
        这个方法通过SpringFactoriesLoader.loadFactoryNames()查找位于META-INF/spring.factories文件中的所有自动配置类并加载这些类。
@ComponentScan
    组件扫描，自动扫描和装配Bean，扫描SpringApplication的run方法中的App.class所在的包路径下的文件，因此将启动类（main）放在跟包路径下。它去寻找带有@Component注解的类，并为其创建bean。
启动流程：是IOC容器的启动过程，本质是创建和初始化bean工厂(BeanFactory)（BeanFactory是Spring IOC的核心，Spring使用beanFactory来实例化，配置和管理bean）
        对于web程序，IOC容器启动过程即是建立上下文的过程，web容器会提供一个全局的servletContext上下文环境。
    https://www.jianshu.com/p/bbb2cbe2c49a
    https://blog.csdn.net/u014252478/article/details/88789852
    https://blog.csdn.net/weixin_46047193/article/details/123557570
    1.首先从main找到run()方法，在执行run()方法之前new一个SpringApplication对象
    2.进入run()方法，创建应用监听器SpringApplicationRunListeners开始监听
    3.然后加载SpringBoot配置环境(ConfigurableEnvironment)，然后把配置环境(Environment)加入监听对象中
    4.然后加载应用上下文(ConfigurableApplicationContext)，当做run方法的返回对象
    5.最后创建Spring容器，refreshContext(context)，实现starter自动化配置和bean的实例化等工作。
    https://www.cnblogs.com/Narule/p/14253754.html
    1.初始化配置：通过类加载器，（loadFactories）读取classpath下所有的spring.factories配置文件，
                创建一些初始配置对象；通知监听者应用程序启动开始，
                创建环境对象environment，用于读取环境配置 如 application.yml
    2.创建应用程序上下文-createApplicationContext，创建 bean工厂对象
    3.刷新上下文（启动核心）
        3.1 配置工厂对象，包括上下文类加载器，对象发布处理器，beanFactoryPostProcessor
        3.2 注册并实例化bean工厂发布处理器，并且调用这些处理器，对包扫描解析(主要是class文件)
        3.3 注册并实例化bean发布处理器 beanPostProcessor
        3.4 初始化一些与上下文有特别关系的bean对象（创建tomcat服务器）
        3.5 实例化所有bean工厂缓存的bean对象（剩下的）
        3.6 发布通知-通知上下文刷新完成（启动tomcat服务器）
    4.通知监听者-启动程序完成

启动中，大部分对象都是BeanFactory对象通过反射创建

Spring bean的生命周期
new->属性注入->Init生命周期初始化方法->初始化回调方法->代理AOP->放入单例池 singletonObject

Spring Security+JWT 安全校验
Ali Druid  数据库连接池+jdbc
SpringDataJPA(spring-boot-starter-data-jpa)
    JpaRepository、@Query
SpringDataRedis(spring-boot-starter-data-redis)
    redisTemplate
(spring-boot-start-cache)https://juejin.cn/post/7220292698854752313
    @EnableCaching @Cacheable @CachePut 或@CacheEvict

MapStruct 用于自动生成 entity-dto 的转换代码
swagger 用于自动生成 api 文档

Java Log框架
                                    log4j
Facade(门面) -slf4j -（真正的实现）-   logback
                                    log4j2



https://blog.csdn.net/wangyuan9826/article/details/123326270
https://blog.csdn.net/langfeiyes/article/details/125171275
https://blog.csdn.net/u013128651/article/details/80194496
    静态代理：新建代理类，传入目标类作为属性，实现目标类接口，在接口内增强代码，实际调用目标类方法
    class Math implements IMath
    class MathProxy implements IMath{
        IMath math = new Math();
        public int add(int x,int y){
            //在接口内增强代码
            return math.add();
        }
    }
    动态代理：1.jdk 动态代理：目标类必须实现接口：reflect.Proxy类和 reflect.InvocationHandler类
    （1）通过 Proxy.getProxyClass()方法获取代理类 Class对象 aClazz
    （2）通过反射 aClazz.getConstructor()获取构造器对象；
    （3）定义 InvocationHandler类并实例化，当然也可以直接只用匿名内部类；
    （4）通过反射 constructor.newInstance()创建代理类对象
    （5）调用代理方法：通过实现 InvocationHandler中的 invoke 方法来增强代码
        a）通过目标类加载器和目标类 class 对象，拷贝到一个新的 class 对象中，新的class 对象带有有参构造器，可以传入 InvocationHandler对象
        和public static Class<?> getProxyClass(ClassLoader loader,Class<?>... interfaces)
        b）获取有参构造器：Constructor<XXX> cs = XXX.class.getConstructor(Class<?>... parameterType) xxx.class是 1 中获取的代理类 class 对象
        c）创造代理类实例：cs.newInstance(new InvocationHandler())
        d）代理类实例.方法名()
        封装方法：
        public static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)
    2.CGLIB动态代理 代理类不用实现目标类接口
        asm 开源包加载目标类 class 文件，通过修改字节码生成子类来处理
类加载过程：
    加载：.java文件编译成.class文件，通过类加载器加载到 jvm 中，并创建一个 class 对象存储在方法区中，实例对象存储在堆中
    链接：将 java 类的二进制代码合并到 JRE（JVM的运行状态中），包含验证（符合 JVM规范）
            准备（为类变量（static）分配内存并设置类变量默认初始值，在方法区中分配） static 静态变量可以理解为 class 对象的属性,属于全体实例
            解析（虚拟机常量池中的符号引用（变量名）替换为直接引用（地址）的过程）
    初始化：执行类构造器<cinit>()方法 类构造器<cinit>()方法是由编译期自动收集类中所有类变量的赋值动作和静态代码块中的语句合并产生。
            （类构造器是构造类信息的，不是构造该类对象的构造器）
            当初始化一个类的时候，如果发现其父类还未初始化，则先触发父类的初始化
            虚拟机会保证一个类的<cinit>()方法在多线程环境中被正确加锁和同步
    类缓存：类加载器加载完会缓存 class 对象，gc 可以回收这些 class 对象
    class A{        //第二部链接后，m=0
        static {    //第三部：初始化后 m 的值由<cinit>()方法执行决定
            m=300;  //这个 A 的类构造器<cinit>()方法由类变量的赋值和静态代码块中的语句按照顺序合并产生，
        }           // 类似于<cinit>(){ m=300;m=100;}
        static int m = 100;
    }
类初始化时机：
    主动引用(一定发生类的初始化)：
        a）虚拟机启动时，先初始化 main 方法所在的类
        b）new 一个类的对象
        c）调用类静态成员和静态方法
        d）对类进行反射调用
        e）先初始化父类
    被动引用(不会发生类的初始化)：
        a）当访问一个静态域时，只有真正声明这个域的类才会被初始化
        b）当通过子类引用父类的静态变量，不会导致子类初始化
        c）通过数组定义类引用，不会触发此类的初始化
        d）引用常量不会触发此类的初始化（常量在链接阶段就存入调用类的常量池中）
获取 class 对象的方法：
    1.XXX.class
    2.XXX xxx = new XXX(); xxx.getClass();
    3.Class.forName("com.XXX") 区别：会执行静态方法初始化类
    4.classLoader.loadClass() 不会执行静态方法
类加载器有：Bootstrap 加载器 (加载 java核心库)
          扩展类加载器ExtClassLoader(java.ext.dirs包下面的类)
          系统类加载器 AppClassLoader(加载java.class.path下的类)

          双亲委派：AppClassLoader交给 parent，parent 再交给 parent，没有 parent 时往下交给 AppClassLoader
          3 次破坏（SPI(JDBC),Tomcat）

JVM和 GC https://blog.csdn.net/m0_37635053/article/details/115400580
    垃圾回收方式：
        1.标记-清除：发生在老年代，优点：占用空间少，缺点：会产生内存碎片
        2.标记-整理：发生在老年代，优点：占用空间少，无内存碎片，缺点：对象移动，消耗资源
        3.复制：发生在新生代 Eden 和 S1\S2,Eden 的对象定期移动到 S 优点：无内存碎片，缺点：需要 2 倍空间
    垃圾回收器种类：
        STW：stop the world 进程内所有线程停止运行，正在进行的请求要等待 STW结束
        CurrentGC：在某些阶段也需要 STW，并且 GC线程和应用线程同时执行，1. cpu 利用率高，2.应用线程会继续产生内存分配，所以要提前执行
                来保证 heap 内可用内存足以支撑正在同时运行的应用线程对内存分配的需求
        串行：单线程垃圾收集
        并行：多线程垃圾收集
        CMS 和用户线程并行标记清除算法
        G1 拆分成多个区域并发进行标记整理算法，适合大堆
    垃圾回收类型：
        Young区的叫 MinorGC，不管选哪种收集器，都会 STW
        Old 区的叫 MajorGC
        Young和 Old 一起叫 FullGC
        触发 FullGC条件：1.检查 MinorGC之后进入老年代的对象放不下
                        2.MinorGC之后剩余对象太多老年代放不下
        进入老年代的触发条件：（代码优化方向） 对象不可达算法
                        1.对象年龄-减少对象被重复引用的次数
                        2.大对象-避免产生大对象

通信协议
    https://blog.csdn.net/u013078871/article/details/118680029
    https://www.zhihu.com/question/41609070
    https://blog.csdn.net/zhanyd/article/details/120989711
进程间通信的几种解决方案：
    1.管道（Pipe）或者具名管道（Named Pipe）
      管道类似于两个进程间的桥梁，可通过管道在进程间传递少量的字符流或字节流。普通管道只用于有亲缘关系的进程（由一个进程启动的另外一个进程）间的通信，具名管道摆脱了普通管道没有名字的限制，除具有管道的所有功能外，它还允许无亲缘关系的进程间的通信。
      管道典型的应用就是命令行中的“|”操作符，
      譬如：ps -ef | grep java
      ps与grep都有独立的进程，以上命令就是通过管道操作符“|”将ps命令的标准输出连接到grep命令的标准输入上。

    2.信号（Signal）
      信号用于通知目标进程有某种事件发生。除了进程间通信外，进程还可以给进程自身发送信号。信号的典型应用是kill命令，
      譬如：kill -9 pid
      以上命令即表示由Shell进程向指定PID的进程发送SIGKILL信号。

    3.信号量（Semaphore）
      信号量用于在两个进程之间同步协作手段，它相当于操作系统提供的一个特殊变量，程序可以在上面进行wait()和notify()操作。

    4.消息队列（Message Queue）
      以上三种方式只适合传递少量消息，POSIX标准中定义了可用于进程间数据量较多的通信的消息队列。进程可以向队列添加消息，被赋予读权限的进程还可以从队列消费消息。消息队列克服了信号承载信息量少、管道只能用于无格式字节流以及缓冲区大小受限等缺点，但实时性相对受限。

    5.共享内存（Shared Memory）
      允许多个进程访问同一块公共内存空间，这是效率最高的进程间通信形式。原本每个进程的内存地址空间都是相互隔离的，但操作系统提供了让进程主动创建、映射、分离、控制某一块内存的程序接口。当一块内存被多进程共享时，各个进程往往会与其他通信机制，譬如与信号量结合使用，来达到进程间同步及互斥的协调操作。

    6.本地套接字接口（IPC Socket）
      消息队列与共享内存只适合单机多进程间的通信，套接字接口则是更普适的进程间通信机制，可用于不同机器之间的进程通信。套接字（Socket）起初是由UNIX系统的BSD分支开发出来的，现在已经移植到所有主流的操作系统上。出于效率考虑，当仅限于本机进程间通信时，套接字接口是被优化过的，不会经过网络协议栈，不需要打包拆包、计算校验和、维护序号和应答等操作，只是简单地将应用层数据从一个进程复制到另一个进程，这种进程间通信方式即本地套接字接口（UNIX Domain Socket），又叫作IPC Socket。
RPC：一种思想，用于分布式系统间的调用，远程方法调用，实现像本地方法调用一样 rpc 框架分为：表示数据 (idl：json/protobuf)和传输数据（后端进程通信）
    rpc 可以基于 http2协议（链接复用）（gRpc）或者自研 tcp 协议（dubbo）
    java 的 socket 就是 tcp 协议之上包装的接口实现方法，是支持 TCP/IP协议的网络通信的基本操作单元，TCP本身是二进制数据流
    restful http 调用方式：post\get\delete等
TCP：https://zhuanlan.zhihu.com/p/108504297
    TCP端口号：（源 IP（网络层携带的信息），源端口号）+（目的 IP，目的端口号），两个 16位存储，1024是系统保留，1024～65535是用户使用）
    TCP的序号和确认号：
        32位序号 seq：某一个传输方向上的字节流的每个字节的序号，通过这个来确认发送的数据有序(发送多少就加多少)
        32位确认号 ack：TCP对上一次seq序号做出的确认号，用来响应TCP报文段，给收到的TCP报文段的序号seq加1。
    TCP的标志位：
        SYN：简写为S，同步标志位，用于建立会话连接，同步序列号；
        ACK： 简写为.，确认标志位，对已接收的数据包进行确认；
        FIN： 简写为F，完成标志位，表示我已经没有数据要发送了，即将关闭连接；
    三次握手：三次握手的目的是连接服务器指定端口，建立 TCP 连接，并同步连接双方的序列号和确认号，交换 TCP 窗口大小信息。在 socket 编程中，客户端执行 connect() 时。将触发三次握手。
        1.客户端发送 SYN=1,随机生成seq=J
        2.服务端通过 SYN=1知道客户端请求建立链接，把 SYN=1,ACK=1,ack=J+1,随机生成 seq=K发回客户端
        3.客户端检查 ack是否=J+1，ACK是否是 1，是的话 把 ACK=1,ack=K+1发送给服务端，服务端检查 ack是否=K+1，ACK是否是 1，完成,3 次握手，1知道客户端请求建立链接
    原因：为什么需要 3 次？
        连接是双向的，第 2 步告诉 client 可以请求 server 了，同时发起 server 到 client 的请求，需要第 3 步 client 告诉 server 连接也是成功的
        避免 1 中由于网络等原因滞留的信号在传给服务端时，服务端直接确认并建立连接，此时客户端以为失败，服务端以为成功，一直等待客户端发送，浪费资源
    四次挥手关闭链接：由于TCP连接是全双工的，因此，每个方向都必须要单独进行关闭
                   收到 FIN只代表该方向上无数据（不会接收），但是可以发送
                   在socket编程中，这一过程由客户端或服务端任一方执行close来触发
        1.客户端发送 FIN报文，设置序列号 seq
        2.服务端收到 FIN报文，返回 ACK,ack=seq+1，同意客户端关闭请求，刺激客户端到服务端没有数据传输
        3.服务端发送 FIN报文，请求关闭连接
        4.客户端收到 FIN报文，返回 ACK，服务端收到后关闭连接，客户端等待 2MSL后关闭
    为什么等待 2MSL：报文段最大生存时间，它是任何报文段被丢弃前在网络内的最长时间
        1.保证 TCP协议的全双工连接能够可靠关闭：
            由于 IP协议的不可靠性或者其他网络原因，导致了 Server 端没有收到 Client 端的 ACK报文，那么 Server端会在超时之后重新发起 FIN
            如果此时 Client 端的连接已经关闭处于 CLOSED状态，那么重发的 FIN就找不到对应连接，从而导致连接错乱
            所以Client端发送完最后的 ACK不能直接进入 CLOSED状态，而要保持 TIME_WAIT，当再次收到 FIN的时候能够保证对方收到 ACK，最后正确关闭连接。
        2.保证这次连接的重复数据段从网络中消失
            如果Client端发送最后的ACK直接进入CLOSED状态，然后又再向Server端发起一个新连接，这时不能保证新连接的与刚关闭的连接的端口号是不同的，
            也就是新连接和老连接的端口号可能一样了，那么就可能出现问题：如果前一次的连接某些数据滞留在网络中，这些延迟数据在建立新连接后到达Client端，
            由于新老连接的端口号和IP都一样，TCP协议就认为延迟数据是属于新连接的，新连接就会接收到脏数据，这样就会导致数据包混乱。
            所以TCP连接需要在TIME_WAIT状态等待2倍MSL，才能保证本次连接的所有数据在网络中消失。

锁与线程安全
synchronized
    synchronized修饰实例方法，锁对象实例，同一时刻只有一个线程进入其中某个实例的一个同步方法，多个实例间同步方法可以并发
    synchronized修饰类方法，锁类，同一时刻只有一个线程进入该类（所有实例）的某一个同步方法，多个实例间不能并发
    synchronized修饰实例方法代码块，（this）的话锁对象实例，多个同步代码块中的某一个代码块一个时刻只能有一个线程执行，除了这个代码块之外的代码还可以并行
    synchronized修饰静态方法代码块，锁类，所有实例、类的调用同一时刻只能有一个线程执行同步代码块
    （）后写什么
    不建议用字符串常量、基本数据类型作为锁对象，因为常量存在方法区，所有线程共享
    要锁有共享的对象

    wait()/notify() Object的方法，持有 Object 对象锁的线程可以调用（否则抛异常），一般放到 synchronized(obj)代码中，
    调用 wait()会释放对象锁，其他线程可以进入对象实例的其他或者统一个同步代码块/方法中，其他线程可以获得对象控制权然后执行 notify()方法
    在 while 循环而不是 if 中调用，这样在被唤醒后可以重复检查条件
    notify()只通知等待队列的第一个线程，notifyall()通知所有等待线程
    其余等待线程被唤醒后（唤醒线程 notify()执行完并未释放，要执行完所有 synchronized才释放锁），只有一个线程可以获得对象锁，其余要等待这个线程执行完毕才可执行

    AQS{
        cas();设置资源 state 0没有线程持有，1 有线程持有 >1该线程的重入次数
        acquire(){
            if(!tryAcquire()) 放入 CLH等到队列
        }
        未实现tryAcquire();
    }
    ReentrantLock 成员变量默认 NonFairSync
    调用关系 ReentrantLock.lock()方法
            ->调用成员 Sync的 lock 方法
            ->调用 AQS的 acquire（）方法
            ->调用子类（FairSync/NonFairSync）实现的 tryAcquire()方法
                ->FairSync 调用自身实现的 tryAcquire()
                ->NonFairSync 调用 Sync实现的 nonfairTryAcquire
    ReentrantLock{
        Sync extends AQS implements Lock{
            abstract lock();
            实现 nonfairTryAcquire();
        }
        NonFairSync extends Sync{
            实现 lock(){
                if(state && cas) 成功获取资源，设置持有锁的线程
                else acquire();
            }
            实现 tryAcquire(){
                if(state=0 && cas) 成功获取资源，设置持有锁的线程
                else if(=持有锁的线程) state++;
            }
        }
        FairSync extends Sync{
            实现了 lock(){
                acquire();
            }
            实现 tryAcquire(){
                if(state=0 && cas && 是 CLH队首的线程) 成功获取资源，设置持有锁的线程
                else if(=持有锁的线程) state++;
            }
        }
    }


### ThreadLocal

InheritableThreadLocal继承关系图
使用InheritableThreadLocal操作的是Thread内的InheritableThreadLocalMap
使用ThreadLocal操作的是Thread内的ThreadLocalMap

子线程用父线程的InheritableThreadLocal实例的话，可以读到父线程中set的值
**原因**：子线程init时拷贝了父线程的InheritableThreadLocalMap到自身线程的InheritableThreadLocalMap中
子线程新建InheritableThreadLocal实例的话，get到是空的
**原因**：子线程中的InheritableThreadLocalMap的key是InheritableThreadLocal实例，
第一个entry是[父线程InheritableThreadLocal实例，父线程set的值]，
第二个entry是[子线程新建的InheritableThreadLocal实例，null]


![c8f363a8e4dff2849d6263701c55e188.jpeg](evernotecid://7C421C31-405D-49A0-9EBC-98E479245B63/appyinxiangcom/50728397/ENResource/p4)


**其他线程用同一个threadLocal实例的赋值不会影响之前线程保存的值**

![d2d0f5c920d407f801f17ddaf48266cb.png](evernotecid://7C421C31-405D-49A0-9EBC-98E479245B63/appyinxiangcom/50728397/ENResource/p3)



### InheritableThreadLocal
**特点**
子线程可以继承父线程的inheritableThreadLocal设置的值
子线程修改值后父线程不影响
**原理**
在Thread中有两个ThreadLocalMap，一个给ThreadLocal操作，一个给InheritableThreadLocal操作；
线程创建的时候调用init()方法，在这里复制父线程的ThreadLocalMap
**但是在线程复用的线程池中是没有办法使用的**



![947cd6ea3def354da989de38f25db23b.png](evernotecid://7C421C31-405D-49A0-9EBC-98E479245B63/appyinxiangcom/50728397/ENResource/p2)

### TransmittableThreadLocal 

https://zhuanlan.zhihu.com/p/146124826
https://github.com/alibaba/transmittable-thread-local

对于使用线程池 池化线程复用的情况，用于提交线程的上下文传递到执行线程的threadlocal中
基本思路：
1. 包装runnable，把主线程上下文传给执行任务
2. 获取主线程context，暂存执行线程context，把主线程set进去
3. 执行完毕后把暂存的context重新放回


## 基于GuavaCache构建本地缓存

1. 注解配置mode、time、size
2. concurrentHashMap缓存单例



## 规则表达式调研

#### 1.1 工具类方案

hutool封装了统一的工具类ExpressionUtil，内部采用单例、工厂类、Facade门面设计模式，提供不同实现引擎，每个引擎引用外部jar包：

| jar                | 效率                         |
| ------------------ | ---------------------------- |
| googlecode.aviator | 初始化时间较久，执行效率最高 |
| commons-jexl3      | 初始化次之，执行效率次之     |
| mvel2              | 初始化最快，执行效率最慢     |

**总结：使用aviator单例预加载**

#### 1.2 根据规则配置动态生成java代码

<https://www.cnblogs.com/barrywxx/p/13233373.html>

1. JDK
2. Groovy：  groovy脚本加载到db，在db中修改同步至本地缓存

```java
GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
Class<?> clazz = groovyClassLoader.parseClass(javaString);
Object obj = clazz.newInstance();
Method method = clazz.getDeclaredMethod("sayHello");
method.invoke(obj);
Object val = method.getDefaultValue();
```

### 跨域
>https://blog.csdn.net/qq_38128179/article/details/84956552

1. 含义：当一个请求url的协议、域名、端口三者之间任意一个与当前页面url不同即为跨域（www.test.com和blog.test.com主域相同子域不同）
2. 非同源限制：
    - 不能访问非同源的cookie、localStorage、indexeddb
    - 不能接触非同源网页的dom
    - 无法向非同源地址发送ajax请求
3. 解决方案：
    1. 子域共享cookie：两个页面都设置document.domain
    2. 跨文档通信api：window.postMessage()
    调用postMessage方法实现父窗口http://test1.com向子窗口http://test2.com发消息（子窗口同样可以通过该方法发送消息给父窗口），它可用于解决以下方面的问题：
        - 页面和其打开的新窗口的数据传递
        - 多窗口之间消息传递
        - 页面与嵌套的iframe消息传递
        - 上面三个场景的跨域数据传递
    3. JSONP:客户端和服务端跨源通信的常用方法，但只支持get不支持post
    4. CORS:跨域资源分享，属于跨源ajax请求的根本解决方法
        1. 普通跨域请求：只需服务器端设置Access-Control-Allow-Origin
        2. 带cookie跨域请求：前后端都需要进行设置
     ```java
    /*
     * 导入包：import javax.servlet.http.HttpServletResponse;
     * 接口参数中定义：HttpServletResponse response
     */

    // 允许跨域访问的域名：若有端口需写全（协议+域名+端口），若没有端口末尾不用加'/'
    response.setHeader("Access-Control-Allow-Origin", "http://www.domain1.com"); 

    // 允许前端带认证cookie：启用此项后，上面的域名不能为'*'，必须指定具体的域名，否则浏览器会提示
    response.setHeader("Access-Control-Allow-Credentials", "true"); 

    // 提示OPTIONS预检时，后端需要设置的两个常用自定义头
    response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With");
      ```
      5. webpack本地代理：vue3+vite 在vite.config.ts中配置代理
      ``http://localhost:8080/api/getUser.php`` 的请求就是后端的接口 ``http://192.168.25.20:8088/getUser.php``
      6. websocket: Websocket 是 HTML5 的一个持久化的协议，它实现了浏览器与服务器的全双工通信，同时也是跨域的一种解决方案。WebSocket 和 HTTP 都是应用层协议，都基于 TCP 协议。但是 WebSocket 是一种双向通信协议，在建立连接之后，WebSocket 的 服务器与 客户端都能主动向对方发送或接收数据。同时，WebSocket 在建立连接时需要借助 HTTP 协议，连接建立好了之后 client 与 server 之间的双向通信就与 HTTP 无关了。
      7. nginx反向代理：
          - Nginx 实现原理类似于 Node 中间件代理，需要你搭建一个中转 nginx 服务器，用于转发请求。
          - 使用 nginx 反向代理实现跨域，是最简单的跨域方式。只需要修改 nginx 的配置即可解决跨域问题，支持所有浏览器，支持 session，不需要修改任何代码，并且不会影响服务器性能。
          - 我们只需要配置nginx，在一个服务器上配置多个前缀来转发http/https请求到多个真实的服务器即可。这样，这个服务器上所有url都是相同的域 名、协议和端口。因此，对于浏览器来说，这些url都是同源的，没有跨域限制。而实际上，这些url实际上由物理服务器提供服务。这些服务器内的 javascript可以跨域调用所有这些服务器上的url。
         -  配置nginx.conf
      ```yml
        server {
            #nginx监听所有localhost:8080端口收到的请求
            listen       8080;
            server_name  localhost;
            # Load configuration files for the default server block.
            include /etc/nginx/default.d/*.conf;
            #localhost:8080 会被转发到这里
            #同时, 后端程序会接收到 "192.168.25.20:8088"这样的请求url
            location / {
                proxy_pass http://192.168.25.20:8088;
            }
            #localhost:8080/api/ 会被转发到这里
            #同时, 后端程序会接收到 "192.168.25.20:9000/api/"这样的请求url
            location /api/ {
                proxy_pass http://192.168.25.20:9000;
            }
            error_page 404 /404.html;
                location = /40x.html {
            }
            error_page 500 502 503 504 /50x.html;
                location = /50x.html {
            }
        }                                                                               
    
      ```

## 问题排查

1. JVM OOM：
   1.1 堆
   1.1.1  内存溢出
   1.1.2 内存泄露：无法合理释放
    2. 栈
        1. stackoverflow：递归太深
        2. 剩余内存不够
    3. 方法区 OOM MetaSpace

2. Docker OOM：
    1. 堆内存（jvm）+堆外内存（direct memory），而堆外内存包括对linux内核文件的打开（fd）,如果stream流未正确关闭，内核会开劈越来越多的堆外内存，造成docker oom