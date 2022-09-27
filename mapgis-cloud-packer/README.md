<h1 align="center">服务器产品打包器</h1>

## 开始

打包之前需先添加支持环境，请执行`add-support.bat`会自动添加`\\192.168.17.59\03-Support`环境到 release 目录内。

> 执行前，请先确保该目录能访问，账号为 support/support

```bash
add-support.bat
```

:::tip
外部用户请访问[链接：https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg 提取码：toub](https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg)，获取`03-Support`支持环境内容添加到release目录内
:::

## 打包

```bash
package/package.bat
```

## 运行（临时）

```bash
# win-x86_64，其他类似，需要先启动Nacos、MySQL、Redis等基础服务
release/win-x86_64/bin/startup.bat
```

## 运行（正式）

> 将与服务器架构一致的 release/os-arch 下的目录发布到该服务器下，需要先启动Nacos、MySQL、Redis等基础服务，然后运行启动脚本即可。

## Docker打包

将与Linux服务器架构一致的产品包和Docker脚本拷贝到具备联网环境的该服务器下
> 比如linux-x86_64的产品包需要到x86_64架构的Linux环境下进行镜像制作（可通过uname -m查询架构）

形成如下的目录结构，确保docker、docker-package和release在同一目录下：

```text
├─docker
│  ├─linux-x86_64
├─docker-package
├─release
│  ├─linux-x86_64
├─docker-package(打包后自动生成)
```

```bash
# 进入到docker-package目录
cd docker-package

# 执行打包(根据服务器架构选择相应的打包脚本，比如当前是x86_64就选择package-linux-x86_64.sh脚本)
sh package-linux-x86_64.sh
```

## Docker Compose 运行（正式）
> 将与服务器架构一致的 docker-release/os-arch 下的目录发布到该服务器下

### 安装
进入应用根目录
执行以下命令，进行安装
```bash
sudo chmod +x install.sh && sudo ./install.sh
```

### 调整参数【必须】
编辑.env，根据需要修改相关参数，运行`sudo vi .env`

### 启动/停止
```bash
# 启动
sudo ./startup.sh
# 停止
sudo ./shutdown.sh
```

### 访问应用
[http://{DOCKER_HOST_IP}:{GATEWAY_PUBLIC_PORT}](http://{DOCKER_HOST_IP}:{GATEWAY_PUBLIC_PORT})

{DOCKER_HOST_IP}为Docker宿主机的IP，{GATEWAY_PUBLIC_PORT}为应用端口

## Kubernetes打包

将`k8s-package`拷贝到linux服务器下，然后将需要的依赖放置到`support`目录内，形成如下目录：

> 需要的依赖可以从\\192.168.17.59\06-K8S-Support获取，账号为 support/support

:::tip
外部用户请访问[链接：https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg 提取码：toub](https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg)，获取`06-K8S-Support`支持环境内容添加到release目录内
:::

```
├─k8s-package
│  ├─support(将依赖拷贝到该处)
│  │  ├─linux-x86-64
│  │  ├─linux-arm64
├─k8s-release(打包后自动生成)
```

```
# 进入到k8s-package目录
cd k8s-package

# 执行打包(根据服务器架构选择相应的打包脚本，比如当前是x86_64就选择package-linux-x86_64.sh脚本)
sh package-linux-x86_64.sh
```

## Kubernetes 运行（正式）

> 将与服务器架构一致的 k8s-release/os-arch 下的目录发布到该服务器下

### 安装

进入应用根目录
执行以下命令，进行安装

```bash
sudo chmod +x install.sh && sudo ./install.sh
```

### 调整参数【必须】

编辑values.yaml，根据需要修改相关参数，运行`sudo vi values.yaml`

### 启动/停止

```bash
# 启动
sudo ./startup.sh
# 停止
sudo ./shutdown.sh
```

### 访问应用

[http://{ip}:{appPort}}](http://{ip}:{appPort})

{ip}为外部访问k8s的IP，{appPort}为应用端口

