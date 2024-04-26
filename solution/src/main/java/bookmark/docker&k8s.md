# Docker

## 基础概念
### Dockerfile
### Image
本质来说就是一系列文件
#### 存储格式
linux联合文件系统：分层的文件系统，可以将不同的目录挂到同一个虚拟文件系统下
镜像存储也是分层的：

* 下面是os文件
* 上层是应用环境文件（jdk等）
* 再上面是代码应用

每一层都是只读的，每一层加载完成后，这些文件被看成同一个目录，相当于只有一个文件系统

### Container
由image执行起来的一个实例，文件系统是image的文件，下面n层都是只读，最上面一层是可写的

程序运行起来，允许对文件新增、修改等，容器在最上面创建了一层可读可写的文件系统

*     把底层文件拷贝到最上面一层进行修改
*     读取文件时，从上往下逐层查找

### 仓库
管理镜像的云端存储

### 运行流程
1. 编写dockerfile，每一行就是一层
2. docker build . -t ${tagname} : 把dockerfile编译成image
3. docker pull 从仓库拉取某一镜像
4. docker run 根据image运行容器，是image的一个实例
5. docker commit 保存容器中的改动成另一个镜像
其他常用命令：
docker ps 查看当前运行的容器
docker exec 进入到正在运行的容器
## docker 网络
linux的命名空间来进行资源隔离：

* pid namespace就是用来隔离进程
* mount namespace是用来隔离文件系统
* network namespace 是用来隔离网络的.每一个network namespace都提供了一个独立的网络环境,包括网卡路由iptable规则等

### 网络模式
#### Bridge模式

容器内的端口和主机的端口映射，容器内的端口可以在主机上访问到

#### Host模式

容器将不会获得一个独立的network namespace,而是和主机共同使用一个,这个时候容器将不会虚拟出自己的网卡,配置出自己的ip.而是使用宿主机上的ip和端口.也就是说在docker上使用网络和在主机上使用网络是一样的

#### None模式

没有网络,这种情况docker将不会和外界的任何东西进行通讯.

# K8S

## Pod
运行一个或多个容器，是一个进程组