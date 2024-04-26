
## 灰度发布方案调研

**基于spring cloud gateway:**

原理：filter->servlet->interceptor->controller
扩展：网关、鉴权、限流、熔断、登录、操作日志

#### 3.1 网关服务

- 组件有SpringCloudGateway、SpringCloudZuul
- 由网关收口外部请求，统一服务路由和负载均衡

**方案:**

**过滤器filter(pre、routing、post、error):**

1. 拦截请求
2. 转发给自定义负载均衡器
3. scheme置为null，不会进行额外处理，直接进入下一个filer，不会再被其他filter篡改请求

>先执行自定义的``GrayLoadBalancerClientFilter``，后执行**Gateway**内置的``ReactiveLoadBalancerClientFilter``

**负载均衡器**

自定义负载均衡器，根据请求体内容，指向灰度服务/生产服务
实现父类``ReactorServiceInstanceLoadBalancer``

>内置负载均衡器，需依赖springcloudloadblancer，ribbion被移除

**缺点：** 服务信息及负载配置在gateway本地

#### 3.2 结合服务注册中心

- 组件有nacos、SpringCloudEureka、ZooKeeper、Etcd
- 服务信息和负载配置动态配置在注册中心
 
**方案:**
| nacos注册      | service code | service version |
| -------------- | ------------ | --------------- |
| service A      | code A       | 1               |
| service A beta | code A       | 2               |
| gateway        | code B       | -               |

bootstrap.yml（bootstrap.properties）与application.yml（application.properties）**执行顺序:**
bootstrap.yml 先于 application.yml 加载
>bootstrap.yml（bootstrap.properties）用来在程序引导时执行，应用于更加早期配置信息读取，如可以使用来配置application.yml中使用到参数等

> application.yml（application.properties) 应用程序特有配置信息，可以用来配置后续各个模块中需使用的公共参数等。

Nacos配置中使用application.yml会出现什么问题呢?
在程序启动的时候无法读取到配置信息，而又引入了注册和配合中心，所以会一直默认查找本地的信息。

#### 3.3 服务内部调用的灰度实现方案

- 组件: SpringCloudFeign、SpringCloudRibbon
 
**方案:**

  1. 微服务启动时将自己注册到服务注册中心
  2. feigh根据注解生成动态代理，调用方法时底层生成 /api/xxx的http协议格式 (此出带上灰度标签，透传至下一服务)
  3. 最终调用ribbon从本地nacos注册表缓存中，根据服务名获取服务ip，负载均衡算法选出一台ip，拼接生成url：127.0.0.1：8080/api/xxx,最后基于httpclient调用请求

#### 3.4 搭建灰度管理后台

- 原存于注册中心的灰度策略剥离开，注册中心只保留全局灰度开关
- 网关服务拉取配置信息

**方案:**

1. 配置灰度规则：更丰富的灰度策略：国家、省份；更新全局开关至注册中心
2. 设置灰度服务器：调用nacos接口获取服务器ip，指定灰度机器
3. 同步策略：变更时通知网关服务更新

#### 3.5 总体流程

1. **在管理后台设置灰度规则**-*规则数据落地至db*;
    **指定灰度服务器**-*配置数据同步至nacos*
2. **gateway服务开发自定义前置过滤器**：*过滤所有进来的请求，判断是否匹配灰度规则*
   1. 初始化规则-从nacos获取全局开关
   &emsp; - 开：调用nacos接口获取全量有效灰度规则
   &emsp; - 关：跳过当前过滤器，执行下一过滤器
   2. 获取请求头
   3. 过滤灰度规则：删除空的、无效的过滤规则
   4. 匹配灰度规则：匹配请求头和全量灰度规则-匹配上：
   &emsp; -> 把灰度标签加到网关请求头``requestContext`` [^1]
   &emsp; -> 把灰度标签加到ribbon过滤器上下文

3. **ribbon设置**：*是客户端负载均衡, 通过对ribbon上下文中的灰度标签和微服务列表中灰度标签的比较, 来选择一台灰度服务器, 作为目标跳转服务器*
    ``MetadataBalancerRule extends AbstractLoadBalancerRule``-实现``choose(Object key)``方法：
   1. 根据服务名获取可用服务器列表：``reachableInstanceList``
   2. 获取上下文中写入ribbon的灰度规则
   3. 循环遍历服务列表 判断服务元数据是否满足灰度规则，满足：加入grayInstanceList
   4. 判断``grayInstanceList``是否为空-不为空：从中选一台服务器：可根据负载均衡策略选（系统自带：``roundRobbinRule``、``randomRule``,可根据nacos配置的权重，自定义带权重的lb）-为空：从``reachableInstanceList``中选取一台
   5. 返回server

4. **自定义Feign拦截器，实现参数（灰度标签）的透传:** *feign的实质是拦截器, feign将拦截所有的请求跳转, 主要作用是用来做header参数透传, 保证服务间的调用也可以正确选择灰度服务器*
    ``FeginRequestInterceptor implements RequestInterceptor``-实现``apply()``方法
   1. 获取gray_header灰度请求头
   2. 将灰度标签透传到下一个请求的header
   3. 将灰度标签放入ribbon请求上下文

[^1]:requestContext是整个网关请求共享的上下文，后面过滤器中也有效
demo

## 4. AWS上的网关与负载均衡

### 4.1 Route 53 (DNS): 服务发现和服务路由

**指定公网路由**

![Route 53 DNS](image-1.png)

**公网域名指向CloudFront服务器**

>pmc.test1.247tsa.dps.kone.cn 路由到 d2za3m832g9o2e.cloudfront.cn CloudFront 服务器
 
![公网域名指向CDN服务器](image-3.png)

 

### 4.2 CloudFont (CDN)

**GSB：对外DNS解析**
**SLB: 对内，重定向，Nginx反向代理、内网穿透，负载均衡**

![查询CludFront配置](image-4.png)

>根据路径模式配置源：
路径：DNS名称（到ECS的ELB）
/api/*  ： pmc-e-Servi-6OFIRV94WNNU-660412877.cn-north-1.elb.amazonaws.com.cn
   
![根据服务URL路由至对应的LB服务](image-5.png)

 1. *用户点击URL-> 本地DNS解析-> 把域名解析权通过CNAME指向CDN专用DNS服务器*
 2. *CDN的DNS服务器把GSB的设备IP发送给用户*
 3. *用户向GSB设备发起URL请求*
 4. *GSB根据用户IP地址及URL，转发给一台区域负载均衡设备*
 5. *区域负载均衡根据用户ip及url给用户选择就近且有负载能力的CDN缓存服务器IP*
 6. *GSB返回给用户服务器IP*
 7. *用户向缓存服务器发送请求，缓存服务器没内容，向上级服务器请求直到源服务器*
   
   ![CDN负载均衡](image.png)

### 4.3 EC2的负载均衡器（ALB）与目标组

![Alt text](image-2.png)

**负载均衡器**

弹性负载均衡的工作原理

1. 客户向您的应用程序提出请求。
2. 负载均衡器中的**侦听器接收与您配置的协议和端口匹配**的请求。
3. 接收侦听器根据您指定的规则评估传入的请求，如果适用，请求将路由到相应的目标组。您可以使用 HTTPS 侦听器将 TLS 加密和解密的工作卸载到负载均衡器。
4. 一个或多个目标组中健康的目标将根据负载均衡算法和您在侦听器中指定的路由规则接收流量。

> 转发到目标组pmc-backend-service-tg
已注册目标172.31.83.247

![转发目标组](image-10.png)

**创建目标组注册目标**
通过目标组与VPC绑定，把外部请求打入到私有资源中
![创建目标组](image-12.png)
![注册目标](image-11.png)

### 4.4 私有资源（VPC）

VPC 是由 AWS 对象（如 Amazon EC2 实例）填充的 AWS 云的隔离部分。
配置硬件网关地址

![VPC资源地图](image-9.png)

### 4.5 ECS

**定义任务**:（pmc-backend-service）

- 指定系统配置: 操作系统、CPU、内存
- 创建容器：创建容器名称、URI、指定端口号

    >映像URI:655527736351.dkr.ecr.cn-north-1.amazonaws.com.cn/kone/pmc/backend:web-test-76b33a04

![任务定义](image-6.png)

ECS创建集群，集群内**创建服务**：

- 选择部署配置：可由定义的任务决定
- 选择负载均衡器：绑定ALB，目标组
 
![集群绑定任务](image-7.png)

![指定LB](image-8.png)

**总结**:
至此，链路清晰：

1. 用户访问公网域名，通过Route53解析并负载至CloudFront
2. CloudFront通过配置路由和LB服务器地址，路由至EC2的负载均衡器服务器
3. 负载均衡器注册目标组，监听相同端口号，转发至VPC内服务 （硬件参数）
4. 流量通过LB负载至ECS某一服务关联的集群，同时集群内设置的任务创建了容器，流量最终导入容器内

>CI/CD流程中，dockerfile指定映像URI，更新至具体某一容器

**本质是通过LB与服务关联，每新增一个服务，需配置一套LB规则**
![LB与服务](image-13.png)

### 4.6 API Gateway

>API Gateway负责处理接受和处理成千上万个并发 API 调用过程中的所有任务:
包括流量管理、CORS 支持、授权和访问控制、限流、监控以及 API 版本管理

**通过路由和后端资源关联**
![API gateway与服务](image-14.png)

**集成：** 将路由连接到后端资源
**私有集成：** 能够与 VPC 中的私有资源（如 Application Load Balancer 或基于 Amazon ECS 容器的应用程序）创建 API 集成
**VPC 链接：** 允许 API 网关访问 Amazon VPC 中的私有资源（封装 API Gateway 与目标 VPC 资源之间的连接）

>集成绑定了VPC链接，VPC链接定向的 VPC 的 Network Load Balancer（pmc-KoneView-nlb）

![API Gateway](image-15.png)

每个账号有一个vpc 叫做 lz-iot-VPC，这个vpc底下有4个子网 划分网段，2个私有 2个公有
ECS的容器是运行在私有子网中
![Alt text](<MicrosoftTeams-image (1)-1.png>)
![Alt text](<MicrosoftTeams-image (2)-1.png>)
![Alt text](<MicrosoftTeams-image (3)-1.png>)

可以通过API Gateway配置金丝雀发布规则，但是针对地区和用户的功能需要结合代码

![Alt text](MicrosoftTeams-image-1.png)


## 5. 微服务解决方案
| 功能             | Alibaba  | SpringCloud             | 其他              |
| ---------------- | -------- | ----------------------- | ----------------- |
| 服务注册服务发现 | Nacos    | SpringCloudEureka       | zookeeper、etcd   |
| API网关          | -        | SpringCloudGateway      | SpringCloudZuul   |
| 配置中心         | Nacos    | SpringCloudConfig       | Apollo            |
| 安全认证         | -        | SpringCloudSecurity     |                   |
| 熔断限流         | Sentinel | SpringCloudHystris      |                   |
| 服务调用         | Dubbo    | SpringCloudFeign        | OpenFeign         |
| 负载均衡         | -        | SpringCloudLoadBalancer | SpringCloudRibbon |
| 消息             | RocketMQ | RabbitMQ                | Kafka             |





## 一、现状
#### 1. 版本发布
**流程图**
![版本更新检测](%E7%94%A8%E6%88%B7APP%E7%89%88%E6%9C%AC%E6%9B%B4%E6%96%B0-1.png)
1. IOS APP 走 APP Store市场
   
> IOS系统平台规则中产品内不可设计自动检测更新，必须通过线上市场用户才能获取更新
2. 安卓版本上传S3，同时DB中插入新版本记录
   1. 用户打开APP时，触发版本检测流程
   2. 调用接口获取DB中最新版本信息（包含：版本号、版本强制更新等参数），与应用当前版本比较
   3. 若需要更新，判断是否强制更新
   4. 更新过程是从S3拉取新版代码，覆盖旧版的过程
#### 2. 业务功能开关
1. **白名单配置**，办事处code存入DB
2. 针对每个请求，代码中从DB获取办事处code，校验是否需要外露某些节目/功能
## 二、改造方案
### 前提一：版本分发与业务开关功能分开
### 方案一
#### 概述
前后端A/B实验，应用流量分流，不同用户群可配置不同实验版本
#### 具体步骤
1. 一套代码，统一版本，对S3上的代码包和版本校验逻辑无改动
2. 新建实验数据表，实验号、实验版本、实验规则
3. 构建实验平台，用于维护实验版本
4. 新建实验分流服务，封装分流器SDK给前后端，用于传入参数获取实验版本
5. 埋点采集、传输、落地，数据清洗、计算分析、报告输出
#### 架构图
![AB实验架构图](A_B%E5%AE%9E%E9%AA%8C%E6%9E%B6%E6%9E%84%E5%9B%BE-1.png)
#### 流程图
![AB实验流程图](A_B%E5%AE%9E%E9%AA%8C%E6%95%85%E4%BA%8B%E7%BA%BF-1.png)
#### 优点
- 版本相同，版本包管理方便
- 配置问题、代码问题引发的回退不需要用户回退，服务端回退即可
#### 缺点
- 对代码有侵入性，线上代码包需要同时保留多版本的代码
- 样本量时小对统计意义不大
- 数据计算分析需要较为专业的统计学知识
- 数据采集平台构建难度较大
#### 改动成本
| 实验管理系统构建  | 工作量  |
|---|---|
| 实验配置表设计与发布   | 1人日  |
| 实验平台服务端（规则拉取、规则匹配）   | 5人日  |
| 分流器SDK（分桶缓存、参数包装、服务调用）   | 3人日  |
| 实验配置后台界面   | 5人日  |
| 业务代码改造  | 工作量  |
|---|---|
| 前后端使用SDK传入参数，做逻辑分支   | 3人日  |
#### 总结
1. 借用分流思想，构建实验平台，用于统一收口代码中的错综复杂的分支逻辑，后续新增分支逻辑不侵入业务代码
2. 采用**配置平台**代替分流后台服务，更轻量化的实现
3. 适合长时间验证测试
### 方案二
#### 概述
微服务架构下，利用网关与动态配置平台，实现金丝雀发布
#### 原理
![Alt text](gateway-1.png)
> 1. 添加网关层：客户端调用各个服务，在各个服务之上添加一层网关服务，用于统一逻辑处理，再转发到各个服务利用网关服务
> 2. 网关基于过滤器filter实现，执行顺序：pre filter-业务服务-post filter
#### 微服务架构
![Alt text](microservice-1.png)
详情：
canary.md
#### 结果步骤
![Alt text](gatewayresult-1.png)
#### 总结
1. 需要自己搭建且运营Gateway服务，开发及运维难度大
2. 借助AWS的API Gateway可以实现金丝雀发布
3. 适合短期灰度验证，不适合长期灰度
### 前提二、版本分发与业务开关功能合并
### 方案三
#### 概述
同一版本号不同代码包，通过线下方式发送给指定员工
#### 具体步骤
1. 不同代码打包成相同的版本包：S3资源层面，和现有包共存：一个版本，两份代码
2. DB中不维护最新包版本信息，实验版和对照版用户均不会触发版本检测自动更新功能
3. 针对新版代码生成链接二维码，通过线下方式提供下载，下载后用实验版代码覆盖原代码
4. 代码层面支持部署两套服务，也可以一套服务 根据包相关参数做节目/功能控制
5. 版本回退可保证回退至DB中前一版本
### 方案四
#### 概述
通过S3上上传不同版本组合，DB中配置版本组、版本号、版本规则，改造现有版本比较功能
#### 具体步骤
1. 分别上传不同版本代码包，与CKFM主版本取**笛卡尔积**，生成多类型版本包存入S3
![版本组合](%E6%9C%AA%E5%91%BD%E5%90%8D%E6%96%87%E4%BB%B6-1.png)
2. **改造现有表结构**，新增版本组概念：以CKFM主版本为版本组，内嵌的PMC版本为内版本号，指定每种版本的适配人群规则
   1. 主版本更新，需要重新组合所有内版本，生成新的多版本包；
   2. 主版本更新，需要DB中同步对之前规则数据，产生新的版本规则数据；可触发所有用户版本更新；
   3. 内版本更新，组合主版本生成新的单版本包；
   4. 内版本更新，DB插入一条新纪录，可选择同步之前B版规则或者导入新规则
3. **搭建版本控制平台**，用于维护版本规则
4. **构建规则引擎**，用于表达式匹配：方案aviator、jexl3、mvel2
5. 启动APP触发版本校验时，需**上传数据**：用户ID\设备机型等信息用于规则匹配
6. 版本校验**代码新增逻辑**：根据3中上传信息，与数据库维护信息比对，选择对应版本，从S3下载对应版本
7. **版本回退**：出问题时版本回退，选择上一主版本以及上一版本内版本生成的版本号，避免版本错乱；
#### 结果步骤
![Alt text](%E7%89%88%E6%9C%AC%E6%A3%80%E6%9F%A5%E6%95%85%E4%BA%8B%E7%BA%BF-1.png)
#### 缺点
1. IOS 安卓 **版本不一致**
2. CKFM和PMC 版本交叉 笛卡尔积，**版本数量较多**，后续维护成本高
3. **规则匹配效率问题**，版本校验逻辑复杂，减慢APP开启速度，影响用户体验
故事线
   ：运营方式、工作流程
改造成本
   ：改动点、人力、合作问题
上传conf

