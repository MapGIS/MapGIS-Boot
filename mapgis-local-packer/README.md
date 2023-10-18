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
# win-x86_64，其他类似
release/win-x86_64/bin/startup.bat
```

## 运行（正式）

> 将与服务器架构一致的 release/os-arch 下的目录发布到该服务器下，然后运行启动脚本即可。

## 发布
```bash
install.bat
```
通过install.bat可以将打包好的资源按照产品发行的要求进行压缩，存放在项目的install目录内

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
├─docker-release(打包后自动生成)
```

设置打包的镜像版本，修改`docker/linux-x86_64/deploy.sh`中变量MAPGIS_IMAGE_TAG

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
[http://{DOCKER_HOST_IP}:{MAPGIS_APP_PORT}](http://{DOCKER_HOST_IP}:{MAPGIS_APP_PORT})

{DOCKER_HOST_IP}为Docker宿主机的IP，{MAPGIS_APP_PORT}为应用端口

## Kubernetes打包

将`k8s-package`拷贝到linux服务器下，然后将需要的依赖放置到`support`目录内，形成如下目录：

> 需要的依赖可以从\\192.168.17.59\06-K8S-Support获取，账号为 support/support

:::tip
外部用户请访问[链接：https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg 提取码：toub](https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg)，获取`06-K8S-Support`支持环境内容添加到k8s-package/support目录内
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

## 一键打包发布Docker相关包
支持一键打包和发布Docker镜像仓库、Docker包、Kubernetes包和支持在云管上部署的应用包

### 准备kubernetes Helm依赖
将需要的依赖放置到`k8s-package/support`目录内，形成如下目录：
:::tip
外部用户请访问[链接：https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg 提取码：toub](https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg)，获取`06-K8S-Support`支持环境内容添加到k8s-package/support目录内
:::
```
├─k8s-package
│  ├─support(将依赖拷贝到该处)
│  │  ├─linux-x86-64
│  │  ├─linux-arm64
```

### 准备免密登录打包服务器
在windows上，可使用Git Bash或Cygwin作为客户端，在上面设置SSH key，以便打包服务器调用
```shell
# 生成SSH key
ssh-keygen -t rsa

# 将公钥拷贝到服务器
ssh-copy-id username@ip

# 免密登录测试，第一次登录会提示输入密码并保存，后续不再需要输入
ssh username@ip "ls ~"
```

#### 确保服务器支持免密登录
当服务器不支持免密登录的时候需要进行配置
```shell
# 编辑sshd_config配置文件
vi /etc/ssh/ssd_config

# 将RSAAuthentication及PubkeyAuthentication设置为yes
RSAAuthentication yes
PubkeyAuthentication yes

# 保存配置，并重启sshd服务
service sshd restart
```

### 执行打包和发布命令
```shell
# 授权
chmod +x install-release.sh

# 执行
# install-release.sh 一共有6个参数
# 参数1：远程打包服务器IP
# 参数2：远程打包服务器用户名
# 参数3：镜像操作系统名称
# 参数4：镜像操作系统架构
# 参数5：镜像标签
# 参数6：包名上添加时间
# sh install-release.sh ip username os arch tag date
# 10.0.1.166 root linux x86_64 10.6.6.10-0808 ADD_DATE
sh install-release.sh 10.0.1.166 root linux x86_64 10.6.6.10-0808 ADD_DATE
# 10.0.1.188 root linux arm64 10.6.6.10-0808 ADD_DATE
sh install-release.sh 10.0.1.188 root linux arm64 10.6.6.10-0808 ADD_DATE
```
完成后会自动将包放置到install-local-release目录内