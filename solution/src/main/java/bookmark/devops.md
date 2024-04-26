### SpringBoot应用部署到eks

### AWS 准备工作：
1. 节点：ec2实例
2. 节点组：多个节点构成
>ec2作为宿主机，上面部署k8s集群，k8s内部运行pods；
>可以多台ec2同一个k8s集群

3. 新建eks集群
4. 通信：新建alb，对外提供服务；同一个k8s集群内的调用，可以用<service-name>.<name-space>：
   eg：realtime-tool-api.realtimetool-cn
#### 1. gitlab ci
1. mvn编译打包，上传至指定地址
2. 拉取镜像，配置启动参数
3. 多环境部署：编辑.gitlab-ci.yml，指定多环境传入的环境参数env
4. 编写kubectl命令，获取pods，指定image
#### 2. k8s cd