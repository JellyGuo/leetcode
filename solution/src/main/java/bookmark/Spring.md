### SpringBoot 相关
>在 Spring AOP 中，有 3 个常用的概念，Advices 、 Pointcut 、 Advisor ，解释如下：
Advices ：表示一个 method 执行前或执行后的动作。
Pointcut ：表示根据 method 的名字或者正则表达式等方式去拦截一个 method 。
Advisor ： Advice 和 Pointcut 组成的独立的单元，并且能够传给 proxy factory 对象。

#### SpringBoot中请求在到达controller前都会经过什么
>https://blog.csdn.net/qq_28064009/article/details/117074402
>https://blog.csdn.net/bluede2015/article/details/129778604

**拦截机制：Filter->Interceptor->ControllerAdvice->Aspect**
>https://juejin.cn/post/7236932516740579383

1. Filter: 可以获得Http原始的请求和响应信息，但是拿不到响应方法的信息。鉴权、数据校验、日志记录。按注册顺序执行。
    1. 实现Filter接口，重写doFilter方法
    ```java
    public class MyFilter implements Filter { 
        @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { 
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 获取请求头信息 
        String headerInfo = httpRequest.getHeader("Header-Info"); 
        // 解析请求头信息 
        // ... 
        chain.doFilter(request, response); 
        // 继续执行请求处理 } 
        // 可以重写init和destroy方法进行初始化和销毁操作 }
    ```
    2. 注册过滤器：可以创建配置类继承WebMvcConfigurerAdapter类，重写addFilters方法来注册过滤器
    ```java
    public class MyFilterConfig extends WebMvcConfigurerAdapter { 
    @Bean     
    public FilterRegistrationBean<MyFilter> myFilter() {                       FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>(); 
    registrationBean.setFilter(new MyFilter()); registrationBean.addUrlPatterns("/*"); // 指定过滤的URL 
    return registrationBean;
    } 
    
    //注册拦截器
    @Override 
    public void addInterceptors(InterceptorRegistry registry) {                             registry.addInterceptor(new MyInterceptor()); 
        }
    }
    ```
2. Interceptor: 可以获得Http原始的请求和响应信息，也拿得到响应方法的信息，但是拿不到方法响应中的参数的值。常用来：验证登录、预置数据、统计执行效率
    1. HandlerInterceptor：SpringMVC的拦截器，拦截请求地址，优先于MethodInterceptor
    ```java
    public class MyInterceptor implements HandlerInterceptor { 
    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception { 
    // 获取请求头信息 
    String headerInfo = request.getHeader("Header-Info"); 
    // 解析请求头信息 
    // ... 
    return true; // 返回true继续执行请求处理，返回false则不执行请求处理 } 
    // 可以重写 afterCompletion 和 postHandle 方法进行请求处理后的操作 }
    ```
    2. MethodInterceptor：SpringAOP，拦截controller中的方法
3. ControllerAdvice/RestControllerAdvice：主要用于全局的异常拦截和处理
4. Aspect：切面，主要是进行公共方法的拦截，可以拿得到方法响应中参数的值，但是拿不到原始的Http请求和相对应响应的方法,属于方法级别的拦截器。

>但是需要注意的是，这并不是绝对的顺序，具体的执行顺序还会受到过滤器链、拦截器链、AOP切入位置等因素的影响，例如如果在过滤器或拦截器中调用了chain.doFilter(request, response)或handlerInterceptor.preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)方法，则会依次调用过滤器链和拦截器链的下一个过滤器或拦截器。自定义AOP的执行顺序在ControllerAdvice之后，是因为一般情况下我们会将切入点选在Controller的方法上

#### 注解
##### Controller
**@GetMapping** 接受get方法请求
1. 直接使用参数获取
   postman请求 :
```
url/api/param1=x&param2=y
```
controller接受请求：
```java
api(String param1,String param2)
```
```java
api(@RequestParam("param1") String a, @RequestParam("param2") String b)
```
2. 使用map获取 如果@RequestParam注解则无法接收
   postman请求
```
localhost:8080/hello?name=x&age=y
```
```java
@GetMapping("/hello")
public String hello(@RequestParam Map<String, Object> params) {
String str = "name:" + params.get("name") + " age:" + params.get("age");
System.out.println(str);
return str;
}
```
3. 接收数组
```
localhost:8080/hello?age=x&age=y
```
```java
@GetMapping("/hello")
public String hello(Integer[] ages) {
}
```
4. 使用对象获取
```
localhost:8080/hello?name=x&age=y
```
```java
@GetMapping("/hello")
public String hello(User user) {
}
class User{
private String name;
private String age;
}
```
4.1 指定前缀 结合 @InitBinder
```
localhost:8080/hello?u.name=x&u.age=y
```
```java
@GetMapping("/hello")
public String hello(@ModelAttribute("u")User user) {
}
```
4.2 构造多个对象接受参数
```
localhost:8080/hello?name=x&age=y&number=1
```
```java
@GetMapping("/hello")
public String hello(User user,Phone phone) {
}
class Phone{
private String number;
}
```
**@PostMapping** 参数接受post请求
@RequestBody 接受json结构体

**@Configuration** 一般和 **@Bean**搭配，保证@Bean只会被创建一次
**InitializingBean** InitializingBean接口为bean提供了属性初始化后的处理方法，它只有一个afterPropertiesSet方法，凡是继承该接口的类，在bean的属性初始化后都会执行该方法
执行顺序：构造方法 > postConstruct >afterPropertiesSet > init方法
```java
@Component
public class MyInitializingBean implements InitializingBean {

public MyInitializingBean() {
System.out.println("我是MyInitializingBean构造方法执行...");//1
}

@Override
public void afterPropertiesSet() throws Exception {
System.out.println("我是afterPropertiesSet方法执行...");//3
}

@PostConstruct
public void postConstruct() {
System.out.println("我是postConstruct方法执行...");//2
}

public void init(){
System.out.println("我是init方法执行...");//4
}

@Bean(initMethod = "init")//指定init方法，先执行afterPropertiesSet再执行init-mthod
public MyInitializingBean test() {
return new MyInitializingBean();
}
}
```

## SpringBoot 框架构建应用后台整套解决方案
### 认证与鉴权 （Authentication 和 Authorization）
spring-cloud-starter-security
spring-security-oauth2-autoconfigure

### 基础概念
SpringSecurity ： 安全框架，可以根据db、redis、配置中保存的用户身份信息来校验用户的请求
校验后的凭证默认保存到session
JWT（Json Web Token）: 客户端传token给服务端，服务端获取解密后带的用户信息自动
区别cookie：保存在客户端 session：传sessionid，保存在服务端，增加存储成本

**核心配置类**
创建``SecurityConfig extends WebSecurityConfigurerAdapter``
- 返回passwordEncoder实例方法，用于加密密码和密文密码匹配
- configure方法：配置登录页面、权限放行等（可以配置用户名密码权限）
**用户信息类**
创建`` UserDetailsServiceImple extends UserDetailsService``返回Spring定义的UserDetails，框架自动和前端传入的进行匹配
**用户登录认证类**
UsernamePasswordAuthenticationFilter：在这里面获取UserDetails，然后用passwordEncoder来匹配密码
**登录成功/失败/未认证处理类**
``AuthenticationSuccessHandler/AuthenticationFailureHandler/AuthenticationEntryPoint``
**权限继承**
RoleHierarchy：``setHierarchy(Role_admin > Role_user)``
**用户权限保存**：登录认证之后，后续的请求为什么可以不用认证
``SecurityContextPersistenceFilter``
- 运行在UsernamePasswordAuthenticationFilter之前
- SecurityContext保存用户信息
- SecurityContextHolder setContext把SecurityContext存到threadlocal中
- 过滤链走完后，清空holder，把SecurityContext保存到session中
- 请求到来时，从session中获取SecurityContext,放到holder，请求结束，holder清空，放回session
- 新的线程中SecurityContextHolder.getContext().getAuthentication()获取不到用户信息
- 放行静态资源的地方不能加入登录url，否则不走过滤器链

**自动登录**
应用：SecurityConfig配置增加rememberMe()，在cookie中保存 base64(username：过期时间：密码md5值)
服务端先判断是否过期，然后根据username获取db密码做md5散列，确认令牌是否生效
原理：
令牌生成过程：
AbstractAuthenticationProcessingFilter#doFilter -> AbstractAuthenticationProcessingFilter#successfulAuthentication -> AbstractRememberMeServices#loginSuccess -> TokenBasedRememberMeServices#onLoginSuccess
认证过程：
RememberMeAuthenticationFilter

### 认证过程：
> https://zhuanlan.zhihu.com/p/365513384
>https://juejin.cn/post/6844903539295125518


1. AuthenticationFilter
    - 继承AbstractAuthenticationProcessingFilter
    - SpringSecurity提供 UsernamePasswordAuthenticationFilter 表单登录 和 BasicAuthenticationFilter httpBasic方式登录，可以自定义Filter，都需要继承继承AbstractAuthenticationProcessingFilter
    - 扩展：Security默认执行的过滤器有`SecurityContextPersistenceFilter`->`UsernamePasswordAuthenticationFilter`
    - 获取request信息， 生成未认证的AuthenticationToken，调用AuthenticationManager的authenticate()方法
    - 拿到Manager返回后的Authentication，放到SecurityContextHolder （threadlocal实现）
2. AuthenticationManager -> 传入未认证Authentication返回认证后的Authentication
    - 默认ProvideManager，维护了AuthenticationProvider列表
    - 列表有：RememberMe（自动登录）、Remote（oauth）、Dao（查默认库）
    - authenticate() 实际调用provider列表的authenticate()来认证,有一个通过就跳出
    - 跳出后copy一份返回的Authentication返回给上层的filter
3. AuthenticationProvider ->（认证） UserDetails（一般查询数据库获取） ->（认证通过） 生成认证成功的AuthenticationToken ->（存放） SecurityContextHolder
    - AbstractUserDetailsAuthenticationProvider默认daoprovider的实现是继承该类来比对
    - authenticate() 获取 req中未认证Authentication里面的参数
    - 调用UserDetailsService获取UserDetails
    - 比对成功，把UserDetails里的信息放到AbstractAuthenticationToken，set进重新包装一个Authentication返回
4. 通过WebSecurityConfigurerAdapter配置拦截路由

#### 自定义认证登录流程
> Filter指定拦截路由、AuthenticationManager、AuthenticationSuccessHandler
> Provider 指定UserDetailService
> SecurityConfigurerAdapter把provider加到通用manager的列表中，然后把filter加到过滤链中

1. 自定义认证登录token：

    ```java
    /**
     * 手机登录Token
     */
    public class MobileLoginAuthenticationToken extends AbstractAuthenticationToken {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
        private static final Logger logger = LoggerFactory.getLogger(MobileLoginAuthenticationToken.class.getName());
        private final Object principal;
        public MobileLoginAuthenticationToken(String mobile) {
            super(null);
            this.principal = mobile;
            this.setAuthenticated(false);
            logger.info("MobileLoginAuthenticationToken setAuthenticated ->false loading ...");
        }
        public MobileLoginAuthenticationToken(Object principal,
                                              Collection<? extends GrantedAuthority> authorities) {
            super(authorities);
            this.principal = principal;
            // must use super, as we override
            super.setAuthenticated(true);
            logger.info("MobileLoginAuthenticationToken setAuthenticated ->true loading ...");
        }
        @Override
        public void setAuthenticated(boolean authenticated) {
            if (authenticated) {
                throw new IllegalArgumentException(
                        "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
            }
            super.setAuthenticated(false);
        }
        @Override
        public Object getCredentials() {
            return null;
        }
        @Override
        public Object getPrincipal() {
            return this.principal;
        }
        @Override
        public void eraseCredentials() {
            super.eraseCredentials();
        }
    }
    ```

   注： setAuthenticated()：判断是否已认证
   - 在过滤器时，会生成一个未认证的AuthenticationToken，此时调用的是自定义token的setAuthenticated()，此时设置为false -> 未认证
   - 在提供者时，会生成一个已认证的AuthenticationToken，此时调用的是父类的setAuthenticated()，此时设置为true -> 已认证
2. 自定义认证登录过滤器 Filter
   ```java
   /**
    * 手机短信登录过滤器
    */
   public class MobileLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
       private boolean postOnly = true;
       private static final Logger logger = LoggerFactory.getLogger(MobileLoginAuthenticationFilter.class.getName());
       @Getter
       @Setter
       private String mobileParameterName;
       public MobileLoginAuthenticationFilter(String mobileLoginUrl, String mobileParameterName,
                                              String httpMethod) {
           super(new AntPathRequestMatcher(mobileLoginUrl, httpMethod));
           this.mobileParameterName = mobileParameterName;
           logger.info("MobileLoginAuthenticationFilter loading ...");
       }
       @Override
       public Authentication attemptAuthentication(HttpServletRequest request,
                                                   HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
           if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
               throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
           }
           //get mobile
           String mobile = obtainMobile(request);
           //assemble token
           MobileLoginAuthenticationToken authRequest = new MobileLoginAuthenticationToken(mobile);
           // Allow subclasses to set the "details" property
           setDetails(request, authRequest);
           return this.getAuthenticationManager().authenticate(authRequest);
       }
       /**
        * 设置身份认证的详情信息
        */
       private void setDetails(HttpServletRequest request, MobileLoginAuthenticationToken authRequest) {
           authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
       }
       /**
        * 获取手机号
        */
       private String obtainMobile(HttpServletRequest request) {
           return request.getParameter(mobileParameterName);
       }
       public void setPostOnly(boolean postOnly) {
           this.postOnly = postOnly;
       }
   }
   ```
   注：attemptAuthentication()方法：
   - 过滤指定的url、httpMethod
   - 获取所需请求参数数据封装生成一个未认证的AuthenticationToken
   - 传递给AuthenticationManager认证
3. 自定义认证登录提供者 Provider


       ```java
        /**
         * 手机短信登录认证提供者 
         */
        public class MobileLoginAuthenticationProvider implements AuthenticationProvider {
            private static final Logger logger = LoggerFactory.getLogger(MobileLoginAuthenticationProvider.class.getName());
            @Getter
            @Setter
            private UserDetailsService customUserDetailsService;
            public MobileLoginAuthenticationProvider() {
                logger.info("MobileLoginAuthenticationProvider loading ...");
            }
            /**
             * 认证
             */
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                //获取过滤器封装的token信息
                MobileLoginAuthenticationToken authenticationToken = (MobileLoginAuthenticationToken) authentication;
                //获取用户信息（数据库认证）
                UserDetails userDetails = customUserDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
                //不通过
                if (userDetails == null) {
                    throw new InternalAuthenticationServiceException("Unable to obtain user information");
                }
                //通过
                MobileLoginAuthenticationToken authenticationResult = new MobileLoginAuthenticationToken(userDetails, userDetails.getAuthorities());
                authenticationResult.setDetails(authenticationToken.getDetails());
                return authenticationResult;
            }
            /**
             * 根据token类型，来判断使用哪个Provider
             */
            @Override
            public boolean supports(Class<?> authentication) {
                return MobileLoginAuthenticationToken.class.isAssignableFrom(authentication);
            }
        }
        ```
        注：authenticate()方法
        - 获取过滤器封装的token信息
        - 调取UserDetailsService获取用户信息（数据库认证）->判断通过与否
        - 通过则封装一个新的AuthenticationToken，并返回
    4. 自定义认证登录认证配置
        ```java
        @Configuration(SpringBeanNameConstant.DEFAULT_CUSTOM_MOBILE_LOGIN_AUTHENTICATION_SECURITY_CONFIG_BN)
        public class MobileLoginAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
            private static final Logger logger = LoggerFactory.getLogger(MobileLoginAuthenticationSecurityConfig.class.getName());
            @Value("${login.mobile.url}")
            private String defaultMobileLoginUrl;
            @Value("${login.mobile.parameter}")
            private String defaultMobileLoginParameter;
            @Value("${login.mobile.httpMethod}")
            private String defaultMobileLoginHttpMethod;
            @Autowired
            private CustomYmlConfig customYmlConfig;
            @Autowired
            private UserDetailsService customUserDetailsService;
            @Autowired
            private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
            @Autowired
            private AuthenticationFailureHandler customAuthenticationFailureHandler;
            public MobileLoginAuthenticationSecurityConfig() {
                logger.info("MobileLoginAuthenticationSecurityConfig loading ...");
            }
            @Override
            public void configure(HttpSecurity http) throws Exception {
                MobilePOJO mobile = customYmlConfig.getLogins().getMobile();
                String url = mobile.getUrl();
                String parameter = mobile.getParameter().getMobile();
                String httpMethod = mobile.getHttpMethod();
                MobileLoginAuthenticationFilter mobileLoginAuthenticationFilter = new MobileLoginAuthenticationFilter(StringUtils.isBlank(url) ? defaultMobileLoginUrl : url,
                        StringUtils.isBlank(parameter) ? defaultMobileLoginUrl : parameter, StringUtils.isBlank(httpMethod) ? defaultMobileLoginHttpMethod : httpMethod);
                mobileLoginAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
                mobileLoginAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
                mobileLoginAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
                MobileLoginAuthenticationProvider mobileLoginAuthenticationProvider = new MobileLoginAuthenticationProvider();
                mobileLoginAuthenticationProvider.setCustomUserDetailsService(customUserDetailsService);
                http.authenticationProvider(mobileLoginAuthenticationProvider)
                        .addFilterAfter(mobileLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            }
        }
        ```
        
        注：configure()方法
        - 实例化AuthenticationFilter和AuthenticationProvider
        - 将AuthenticationFilter和AuthenticationProvider添加到spring security中。

### 授权过程
**1. 基于session**
如何在 request 之间共享 SecurityContext？
既然 SecurityContext 是存放在 ThreadLocal 中的，而且在每次权限鉴定的时候，都是从 ThreadLocal 中获取 SecurityContext 中保存的 Authentication。那么既然不同的 request 属于不同的线程，为什么每次都可以从 ThreadLocal 中获取到当前用户对应的 SecurityContext 呢？

* 在 Web 应用中这是通过 SecurityContextPersistentFilter 实现的，默认情况下其在每次请求开始的时候，都会从 session 中获取 SecurityContext，然后把它设置给 SecurityContextHolder。
* 在请求结束后又会将 SecurityContextHolder 所持有的 SecurityContext 保存在 session 中，并且清除 SecurityContextHolder 所持有的 SecurityContext。
* 这样当我们第一次访问系统的时候，SecurityContextHolder 所持有的 Security Context 肯定是空的。待我们登录成功后，SecurityContextHolder 所持有的 SecurityContext 就不是空的了，且包含有认证成功的 Authentication 对象。
*  待请求结束后我们就会将 SecurityContext 存在 session 中，等到下次请求的时候就可以从 session 中获取到该 SecurityContext 并把它赋予给 Security Context Holder 了。
* 由于 SecurityContextHolder 已经持有认证过的 Authentication 对象了，所以下次访问的时候也就不再需要进行登录认证了。


**2. 基于JWT**
> SpringSecurity+JWT方案文章
>https://blog.csdn.net/qq_44709990/article/details/123082560

    1. 新建JwtAuthenticationTokenFilter，configure方法中通过http 加在UsernamePasswordAuthenticationFilter 之前

3. 基于OAuth2


### OAuth2
##### 背景：传统web基于session来认证，移动端后使用oauth2；
##### 流程：
用户登录第三方
第三方请求授权服务器
授权服务器发放令牌给第三方应用
第三方应用通过令牌访问各个资源服务器，各个服务通过认证中心认证
相当于资源服务中的SpringSecurity配置remote模式，请求认证服务来鉴权
#### 授权模式：
1. 授权码模式：第三方登录
2. 简化模式
3. 密码模式
4. 客户端模式
#### 具体流程
第三方服务跳链至授权服务器提供的登录接口，授权服务通过后下发给第三方服务一个授权码，第三方服务再请求授权服务获取token，拿到token后请求资源服务器获取用户资源（资源服务器请求授权服务器验证token）
1. 第三方服务
   请求授权服务器获取token后再请求资源服务
2. 资源服务
    - 配置 RemoteTokenServices
3. 授权服务
    - 配置TokenStore实例：token往哪里存储 （redis、内存、jwt）
    - 配置子类继承AuthorizationServerConfigurerAdapter
1. AuthorizationServerSecurityConfigurer:配置token端点谁可以访问（资源服务器收到token后会校验token合法性，会访问这个端点）
2. ClientDetailsServiceConfigurer: 配置客户端信息，校验客户端（第三方平台）
3. AuthorizationServerEndpointsConfigurer：配置令牌访问端点和令牌服务
    - tokenServices 令牌存储
    - AuthorizationCodeServices 授权码存储
#### OAuth2+JWT
**之前模式存在问题：**
资源服务器请求授权服务来校验token，高并发清情况下，授权服务是瓶颈
方案：使用JWT存储token，相当于第三方和资源的交互，资源不需要再去请求认证
1. 实现 TokenEnhancer 来加强token
2. 修改AuthorizationServerTokenServices 实例，用于生成token，配置TokenEnhancerChain用于增强token
3. 资源服务器配置TokenStore


### Session

**1、Session其实分为客户端Session和服务器端Session。**
*     当用户首次与Web服务器建立连接的时候，服务器会给用户分发一个 SessionID作为标识。SessionID是一个由24个字符组成的随机字符串。用户每次提交页面，浏览器都会把这个SessionID包含在 HTTP头中提交给Web服务器，这样Web服务器就能区分当前请求页面的是哪一个客户端。这个SessionID就是保存在客户端的，属于客户端Session。
*     其实客户端Session默认是以cookie的形式来存储的，所以当用户禁用了cookie的话，服务器端就得不到SessionID。这时我们可以使用url的方式来存储客户端Session。也就是将SessionID直接写在了url中，当然这种方法不常用。

**2、sessionid如何产生？由谁产生？保存在哪里？**

*         sessionid是一个会话的key，浏览器 第一次 访问服务器会在服务器端生成一个session，有一个sessionid和它对应。tomcat生成的sessionid叫做jsessionid。
*         session在访问tomcat服务器**HttpServletRequest的getSession(true)的时候创建**，tomcat的ManagerBase类提供创建sessionid的方法：随机数+时间+jvmid；**它存储在服务器的内存中**，tomcat的StandardManager类将session存储在内存中，也可以持久化到file，数据库，memcache，Redis等。客户端只保存sessionid到cookie中，而不会保存session，session销毁只能通过invalidate或超时，关掉浏览器并不会关闭session。

**3、session会因为浏览器的关闭而删除吗？**

*         Cookie分为内存中Cookie（也可以说是进程中Cookie）和硬盘中Cookie。大部分的Session机制都使用进程中Cookie来保存Session id的，关闭浏览器后这个进程也就自动消失了，进程中的Cookie自然就消失了，那么Session id也跟着消失了，再次连接到服务器时也就无法找到原来的Session了。
*         当然，我们可以在登陆时点击下次自动登录，比如说CSDN的“记住我一周”，或者我们的购物车信息可以在切换不同浏览器时依然可用。这就要用到我们上文提到的另一种Cookie了——硬盘中Cookie，这时Session id将长期保存在硬盘上的Cookie中，直到失效为止。

**4、tomcat中session的创建：**
*           ManagerBase 是所有session管理工具类的基类，它是一个抽象类，所有具体实现session管理功能的类都要继承这个类，该类有一个受保护的方法，该方法就是创建sessionId值的方法：
* （tomcat的session的id值生成的机制是一个随机数加时间加上jvm的id值，jvm的id值会根据服务器的硬件信息计算得来，因此不同jvm的id值都是唯一的），
*         StandardManager 类是tomcat容器里默认的session管理实现类，它会将session的信息存储到web容器所在服务器的内存里。
*         PersistentManagerBase也是继承ManagerBase类，它是所有持久化存储session信息的基类，PersistentManager继承了PersistentManagerBase，但是这个类只是多了一个静态变量和一个getName方法，目前看来意义不大，对于持久化存储session，tomcat还提供了StoreBase的抽象类，它是所有持久化存储session的基类，另外tomcat还给出了文件存储FileStore和数据存储JDBCStore两个实现。
*         session是解决http协议无状态问题的服务端解决方案，它能让客户端和服务端一系列交互动作变成一个完整的事务，能使网站变成一个真正意义上的软件

#### 扩展：
**1、会话cookie和持久cookie的区别**

*         如果不设置过期时间，则表示这个cookie生命周期为浏览器会话期间，只要关闭浏览器窗口，cookie就消失了。这种生命期为浏览会话期的cookie被称为会话cookie。会话cookie一般不保存在硬盘上而是保存在内存里。
* 　　如果设置了过期时间，浏览器就会把cookie保存到硬盘上，关闭后再次打开浏览器，这些cookie依然有效直到超过设定的过期时间。
* 　　存储在硬盘上的cookie可以在不同的浏览器进程间共享，比如两个IE窗口。而对于保存在内存的cookie，不同的浏览器有不同的处理方式。

**2、保存session id的几种方式**

1.     保存session id的方式可以采用cookie，这样在交互过程中浏览器可以自动的按照规则把这个标识发送给服务器。
2.     由于cookie可以被人为的禁止，必须有其它的机制以便在cookie被禁止时仍然能够把session id传递回服务器，经常采用的一种技术叫做URL重写，就是把session id附加在URL路径的后面，附加的方式也有两种，一种是作为URL路径的附加信息，另一种是作为查询字符串附加在URL后面。网络在整个交互过程中始终保持状态，就必须在每个客户端可能请求的路径后面都包含这个session id。
3.     另一种技术叫做表单隐藏字段。就是服务器会自动修改表单，添加一个隐藏字段，以便在表单提交时能够把session id传递回服务器。

**3、session什么时候被创建**
*         一个常见的错误是以为session在有客户端访问时就被创建，然而事实是直到某server端程序(如Servlet)调用HttpServletRequest.getSession(true)这样的语句时才会被创建。

**4、session何时被删除**
session在下列情况下被删除：
*         A．程序调用HttpSession.invalidate()
*         B．距离上一次收到客户端发送的session id时间间隔超过了session的最大有效时间
*         C．服务器进程被停止
*         再次注意关闭浏览器只会使存储在客户端浏览器内存中的session cookie失效，不会使服务器端的session对象失效。

**5、getSession()/getSession(true)、getSession(false)的区别**

* getSession()/getSession(true)：当session存在时返回该session，否则新建一个session并返回该对象
* getSession(false)：当session存在时返回该session，否则不会新建session，返回null

**6、使用isNew来判断用户是否为新旧用户的错误做法**

* public boolean isNew()方法如果会话尚未和客户程序(浏览器)发生任何联系，则这个方法返回true，这一般是因为会话是新建的，不是由输入的客户请求所引起的。
* 但如果isNew返回false，只不过是说明他之前曾经访问该Web应用，并不代表他们曾访问过我们的servlet或JSP页面。
* 因为session是与用户相关的，在用户之前访问的每一个页面都有可能创建了会话。因此isNew为false只能说用户之前访问过该Web应用，session可以是当前页面创建，也可能是由用户之前访问过的页面创建的。
* 正确的做法是判断某个session中是否存在某个特定的key且其value是否正确

**7、session cookie和session对象的生命周期是一样的吗**

* 当用户关闭了浏览器虽然session cookie已经消失，但session对象仍然保存在服务器端

**8、是否只要关闭浏览器，session就消失了**

*         程序一般都是在用户做log off的时候发个指令去删除session，然而浏览器从来不会主动在关闭之前通知服务器它将要被关闭，因此服务器根本不会有机会知道浏览器已经关闭。服务器会一直保留这个会话对象直到它处于非活动状态超过设定的间隔为止。
*         之所以会有这种错误的认识，是因为大部分session机制都使用会话cookie来保存session id，而关闭浏览器后这个session id就消失了，再次连接到服务器时也就无法找到原来的session。
* 如果服务器设置的cookie被保存到硬盘上，或者使用某种手段改写浏览器发出的HTTP请求报头，把原来的session id发送到服务器，则再次打开浏览器仍然能够找到原来的session。
*         恰恰是由于关闭浏览器不会导致session被删除，迫使服务器为session设置了一个失效时间，当距离客户上一次使用session的时间超过了这个失效时间时，服务器就可以认为客户端已经停止了活动，才会把session删除以节省存储空间。
* 　　由此我们可以得出如下结论：
* 　　关闭浏览器，只会是浏览器端内存里的session cookie消失，但不会使保存在服务器端的session对象消失，同样也不会使已经保存到硬盘上的持久化cookie消失。

**9、session共享问题**

*         当下的互联网网站为了提高网站安全性和并发量，服务端的部署的服务器的数量往往是大于或等于两台，多台服务器对外提供的服务是等价的，但是不同的服务器上面肯定会有不同的web容器，由上面的讲述我们知道session的实现机制都是web容器里内部机制，这就导致一个web容器里所生成的session的id值是不同的，因此当一个请求到了A服务器，浏览器得到响应后，客户端存下的是A服务器上所生成的session的id，当在另一个请求分发到了B服务器，B服务器上的web容器是不能识别这个session的id值，更不会有这个sessionID所对应记录下来的信息，这个时候就需要两个不同web容器之间进行session的同步。
*         一般大型互联公司的网站都是有一个个独立的频道所组成的，例如我们常用的百度，会有百度搜索，百度音乐，百度百科等等，我相信他们不会把这些不同频道都给一个开发团队完成，应该每个频道都是一个独立开发团队，因为每个频道的应用的都是独立的web应用，那么就存在一个跨站点的session同步的问题，跨站点的登录可以使用**单点登录的（SSO）**的解决方案，但是不管什么解决方案，跨站点的session共享任然是逃避不了的问题。

**10、解决session相关问题的技术方案**
由上所述，session一共有两个问题需要解决：

1. session的存储应该独立于web容器，也要独立于部署web容器的服务器；
2. 如何进行高效的session同步。
   在讲到解决这些问题之前，我们首先要考虑下session如何存储才是高效，是存在内存、文件还是数据库了？文件和数据库的存储方式都是将session的数据固化到硬盘上，操作硬盘的方式就是IO，IO操作的效率是远远低于操作内存的数据，因此文件和数据库存储方式是不可取的，所以将session数据存储到内存是最佳的选择。因此最好的解决方案就是使用分布式缓存技术，例如：memcached和redis，将session信息的存储独立出来也是解决session同步问题的方法。

### 前后端分离 全局异常处理及统一结果封装
> https://blog.csdn.net/qq_44709990/article/details/123048867

##### 统一结果封装

```java
class Result<T>{
String code;
String msg;
T data;
static <T> Result<T> ok();
static <T> Result<T> failed();
}
```
##### 全局异常处理
>定义一个叫GlobalExceptionHandler的类来捕获和处理全局异常，该类需要使用@RestControllerAdvice注解, 可以用于定义@ExceptionHandler、@InitBinder、@ModelAttribute，并应用到所有@RequestMapping中。@RestControllerAdvice 是组件注解，他使得其实现类能够被classpath扫描自动发现。@RestControllerAdvice注解主要配合@ExceptionHandler使用，统一处理异常情况。

```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(value = RuntimeException.class)
public Result handler(RuntimeException e) {
log.error("运行时异常：----------------{}", e.getMessage());
return Result.fail(e.getMessage());
}

@ResponseStatus(HttpStatus.FORBIDDEN)
@ExceptionHandler(value = AccessDeniedException.class)
public Result handler(AccessDeniedException e) {
log.info("security权限不足：----------------{}", e.getMessage());
return Result.fail("权限不足");
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(value = MethodArgumentNotValidException.class)
public Result handler(MethodArgumentNotValidException e) {
log.info("实体校验异常：----------------{}", e.getMessage());
BindingResult bindingResult = e.getBindingResult();
ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
return Result.fail(objectError.getDefaultMessage());
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(value = IllegalArgumentException.class)
public Result handler(IllegalArgumentException e) {
log.error("Assert异常：----------------{}", e.getMessage());
return Result.fail(e.getMessage());
}
}
```



### 数据库
#### 读写分离、分库分表 shardingsphere
#### mybatis-plus/spring-data-jpa
**mybatis-plus**
1. 编写pojo T
- @TableId指定主键（自增、随机)
- @TableField指定表字段
- 默认开启了下滑线-驼峰转换，可配置关闭mybatis-plus.configuration.map-underscore-to-camel-case=false
2. 编写TMapper extends BaseMapper<T>，可以直接使用该mapper查询
3. 编写TService extends ServiceImpl<<M extends BaseMapper<T>, T> implements IService<T>
   BaseMapper\ServiceImpl\IService 是组件封装好的类和接口
4. MyBatisPlusConfig添加@MapperScan 指定mapper扫描路径，注册乐观锁插件OptimisticLockerInterceptor
5. 编写MyMetaObjectHandler implements MetaObjectHandler 用于处理@TableField中的fill类型逻辑（自动添加创建/更新时间）
6. 乐观锁
- 表添加version字段
- pojo字段添加@version注解
- MyBatisPlusConfig注册插件
- 仅支持 updateById(id) 与 update(entity, wrapper) 方法

7. 自定义SQL
- mapper接口中添加方法，方法上加@Select()之类的注解
- sql写入xml中，application.xml中添加扫描mapper.xml路径，

8. 分页查询，MyBatisPlusConfig添加拦截器PaginationInterceptor
9. 性能更新，MyBatisPlusConfig添加拦截器PerformanceInterceptor
10. 条件构造器Wrapper
- QueryWrapper<T> wrapper:参数字符串，指定db字段名
- LambdaQueryWrapper<T> lambdaQueryWrapper：指定pojo属性

**spring-data-jpa** 基于hibernate，ORM框架，直接封装了CRUD操作
``xxxRepository extends JpaRepository<T, ID>``
@Query(value=" 这里就是查询语句")
复杂排序、分页查询 JpaSpecificationExecutor


#### druid 数据库连接池 （springboot默认hikari连接池）
数据库连接池的意义在于，能够重复利用数据库连接（有点类似线程池的部分意义），提高对请求的响应时间和服务器的性能。
连接池中提前预先建立了多个数据库连接对象，然后将连接对象保存到连接池中，当客户请求到来时，直接从池中取出一个连接对象为客户服务，当请求完成之后，客户程序调用close()方法，将连接对象放回池中。
druid为阿里巴巴的数据源，（数据库连接池），集合了c3p0、dbcp、proxool等连接池的优点，还加入了日志监控，有效的监控DB池连接和SQL的执行情况
#### 多数据源：dynamic-datasource-spring-boot-starter
>https://blog.csdn.net/w57685321/article/details/106823660

使用方法：配置yml文件、加上@DS注解
注意点：
1. 不支持事务切换数据源
   原因 原理是实现DataSource接口实现getConnection方法，但是 spring在处理事务时会对数据源缓存，默认缓存primary数据源的连接

#### 数据库事务 @Transactional spring事务传播 和分布式事务 seata方案
>https://blog.csdn.net/csdnlaiyanqi/article/details/121478081
>
1. @Transactional注解属性
- name：指定事务管理器TransactionManager
- propagation：事务传播行为
- isolation：事务隔离度
- timeout：超时时间，超过事务未完成，则回退
- read-only：指定只读事务，默认false
- rollbackFor：指定触发事务回滚的exception
- noRollbackFor：默认回滚未检查异常，若想抛出某种异常但不回滚事务，可用该注解
2. Spring事务实现机制：核心AOP触发时机
- Spring AOP代理：
- CglibAopProxy：调用内部类DynamicAdvisedInterceptor的intercept方法
- JdkDynamicAopProxy：调用invoke方法
- 事务管理器：
- 接口：PlatformTransactionManager
- 抽象类：AbstractPlatformTransactionManager
- 具体实现类：DataSourceTransactionManager 管理JDBC的Connection（SpringBoot自动配置）
- 代理对象决定注解方法是否由拦截器TransactionInterceptor使用拦截，加入事务并由事务管理器来负责事务回滚
- 编程式事务方式：
```java
public void test() {
TransactionDefinition def = new DefaultTransactionDefinition(); TransactionStatus status = transactionManager.getTransaction(def);
try {
// 事务操作
// 事务提交
transactionManager.commit(status);
} catch (DataAccessException e) {
// 事务提交
transactionManager.rollback(status);
throw e; } }
```
3. 使用注意：
1. 默认配置下，只会回滚运行时、未检查异常（继承自RuntimeException）或者Error
2. 只能应用到public方法
3. 并发性：isolation定义，默认DEFAULT，即通过数据库的隔离级别
4. 避免Spring AOP自调用问题：只能外部调用，如果由同一个类的非注解方法调用，事务会被忽略不会回滚 - 可使用AspectJ解决
5. 事务传播：
1. REQUIRED:如果当前存在事务，则加入该事务，如果当前不存在事务，则创建一个新的事务。( 也就是说如果A方法和B方法都添加了注解，在默认传播模式下，A方法内部调用B方法，会把两个方法的事务合并为一个事务 ）
2. SUPPORTS:如果当前存在事务，则加入该事务；如果当前不存在事务，则以非事务的方式继续运行。
3. MANDATORY:如果当前存在事务，则加入该事务；如果当前不存在事务，则抛出异常。
4. REQUIRES_NEW:重新创建一个新的事务，如果当前存在事务，暂停当前的事务。( 当类A中的 a 方法用默认Propagation.REQUIRED模式，类B中的 b方法加上采用 Propagation.REQUIRES_NEW模式，然后在 a 方法中调用 b方法操作数据库，然而 a方法抛出异常后，b方法并没有进行回滚，因为Propagation.REQUIRES_NEW会暂停 a方法的事务 )
5. NOT_SUPPORTED:以非事务的方式运行，如果当前存在事务，暂停当前的事务。
6. NEVER:以非事务的方式运行，如果当前存在事务，则抛出异常。
6. 事务方法中如果有rpc调用、消息发送、缓存更新、文件写入操作，这些操作无法回滚，且会占用数据库连接
### 服务调用与负载均衡 feign和ribbon
### 消息kafka
> https://cloud.tencent.com/developer/article/1542310

1. spring-kafka
2. yml配置bootstrap-servers、consumer、producer
3. 生产者：
1. map存放配置：服务器地址、key/value的序列化方式
2. 创建kafka生产者工厂，一般是DefaultKafkaProducerFactory，构造方法传入配置
3. 创建kafkaTemplate，构造方法传入工厂
```java
kafkaTemplate.addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {
@Override
public void onFailure(Throwable throwable) { ...... }
@Override
public void onSuccess(SendResult<Object, Object> objectObjectSendResult) { .... }
});
```
4. 事务消息：
1. 配置激活spring.kafka.producer.transaction-id-prefix=kafka_tx.
2. kafkaTemplate.executeInTransaction,在方法上面加@Transactional注解也会生效

4. 消费者：
1. map存放配置
2. 创建 ConsumerFactory，一般为DefaultKafkaConsumerFactory
3. 创建监听器容器工厂
4. @KafkaListener(topics = "my-topic", groupId = "my-group-id")
- 同时可以指定消费哪些topic和分区
- 分区偏移量
- 消费线程并发度
- 异常处理器errorHandler，指定bean实现KafkaListenerErrorHandler接口
```java
@Bean
public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() { ConcurrentKafkaListenerContainerFactory<String, String> factory
= new ConcurrentKafkaListenerContainerFactory<>(); factory.setConsumerFactory(consumerFactory());
return factory;
}
```
5. 手动ack
- 关闭自动提交：spring.kafka.consumer.enable-auto-commit=false
- 设置消费模式：spring.kafka.listener.ack-mode=manue
```java
@KafkaListener(id = "webGroup", topics = "topic-kl")
public String listen(String input, Acknowledgment ack) {
ack.acknowledge();
}
```
6. 生命周期：KafkaListenerEndpointRegistry：start()、pause()、resume()
   ``registry.getListenerContainer(listenerID)``
   listenerID是注解中的id字段
7. 消息转发
```java
@KafkaListener(id = "webGroup", topics = "topic-1")
@SendTo("topic-2")
public String listen(String input) {
logger.info("input value: {}", input);
return input + "hello!";
}
```
8. 消息重试和死信队列
   topic-kl监听到消息会，会触发运行时异常，然后监听器会尝试三次调用，当到达最大的重试次数后。消息就会被丢掉重试死信队列里面去。死信队列的Topic的规则是，业务Topic名字+“.DLT”
```java
@Autowired
private KafkaTemplate template;
@Bean
public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
ConsumerFactory<Object, Object> kafkaConsumerFactory,
KafkaTemplate<Object, Object> template) {
ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
configurer.configure(factory, kafkaConsumerFactory);
//最大重试三次
factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(template), 3));
return factory;
}
@GetMapping("/send/{input}")
public void sendFoo(@PathVariable String input) {
template.send("topic-kl", input);
}
@KafkaListener(id = "webGroup", topics = "topic-kl")
public String listen(String input) {
logger.info("input value: {}", input);
throw new RuntimeException("dlt");
}
@KafkaListener(id = "dltGroup", topics = "topic-kl.DLT")
public void dltListen(String input) {
logger.info("Received from DLT: " + input);
}
```
### 邮件/短信/公众号推送
#### 邮件
>https://blog.csdn.net/qq_44709990/article/details/123478866
1. 组件：spring-boot-starter-mail
2. yml配置：spring.mail.host/protocol(smtp)...
3. 代码： JavaMailSender，附件MimeMessage


### 定时任务
>https://blog.csdn.net/qq_44709990/article/details/123471552

#### Spring注解@Scheduled

使用@Scheduled注解需要注意几个点，
- CronManageTask需使用@Component注解，且此类中不能包含其他带任何注解的方法；
- cronManage()方法不能有参数、不能有返回值；
- 需添加@EnableScheduling注解到启动类上面。

注：在集群环境下，任务会被重复调度

#### SpringBatch
批处理框架，应用场景：
1. 从DB/文件中读取大量数据
2. 处理数据
3. 把修改后的数据写回

##### 总体架构

JobRepository{JobLauncher->Job->Step}->{ItemReader/ItemProcessor/ItemWriter}

**JobRepository 和  JobLauncher**

1. 定义的job在jobRepository里，简单理解为 Job的DAO层
2. JobLauncher是作业执行的入口接口

**Job**

1. Job是抽象接口，实现类SimpleJob （eg：EndOfDay job）
2. JobInstance由job+jobParameters组成，JobInstance 是Job每次逻辑上执行的一个实体，每次执行形成一个JobInstance，而JobInstance又对应一系列JobExecution （EndOfDay job for 20210101）
3. JobExecution是单次Job执行的技术上的概念，如果失败，则第二次执行JobInstance的参数是一样的，JobExecutance是不同的 （first attempt of EndOfDay job for 20210101）

**Step**
1. Step：每个job执行的最小逻辑单元
- chunk size: 数据积累到size时才commit提交到数据库
2. StepExecution：同JobExecution类似

#### Quartz 任务调度框架

##### 核心类：
- 任务 Job：实现execute()方法
- 触发器 Trigger：包括SimpleTrigger和CronTrigger
- 调度器 Scheduler：负责基于Trigger触发器来执行Job任务

JobDetail 的作用是绑定Job，定义的是任务数据，每次执行会创建一个新的Job实例，避免并发问题

JobExecutionContext：
- 当 Scheduler 调用一个 job，就会将 JobExecutionContext 传递给 Job 的 execute() 方法;
- Job 能通过 JobExecutionContext 对象访问到 Quartz 运行时候的环境以及 Job 本身的明细数据；

##### 参数传递
1. job中获取上下文参数：在builder建造过程中
2. job状态参数：多次调用期间持有一些状态信息

使用usingJobData，job中使用JobDataMap获取

##### 分布式锁

在集群环境下，Quartz 集群中的每个节点是一个独立的 Quartz 应用，没有负责集中管理的节点，而是通过数据库表来感知另一个应用，利用**数据库锁**的方式来实现集群环境下进行并发控制，每个任务当前运行的有效节点有且只有一个

### Prometheus 监控中心