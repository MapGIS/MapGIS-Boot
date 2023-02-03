# 项目打包

**单体版打包可参考**[mapgis-local-packer](https://github.com/MapGIS/MapGIS-Boot/blob/master/mapgis-local-packer)

**微服务版打包可参考**[mapgis-cloud-packer](https://github.com/MapGIS/MapGIS-Boot/blob/master/mapgis-cloud-packer)

## 传统打包

### 单体版

进入到`mapgis-local-packer`目录。

#### 打包前提

打包之前需先添加支持环境，请执行`add-support.bat`会自动添加支持环境到 release 目录内。

```shell
# 依赖支持环境
add-support.bat
```

:::tip
外部用户请访问[链接：https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg 提取码：toub](https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg)，获取`03-Support`支持环境内容添加到release目录内
:::

#### 打包

进入`package`目录，执行以下命令，进行打包：

```shell
# 打包
package.bat
```

打包成果放到了release目录内，通过`os-arch`进行分类存放，形成如下的目录结构。

```shell
├─package(打包脚本)
│  ├─package.bat
├─release(打包后自动生成)
│  ├─win-x86_64
│  ├─linux-x86_64
```

### 微服务版

进入到`mapgis-cloud-packer`目录。

#### 打包前提

打包之前需先添加支持环境，请执行`add-support.bat`会自动添加支持环境到 release 目录内。

```shell
# 依赖支持环境
add-support.bat
```

:::tip
外部用户请访问[链接：https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg 提取码：toub](https://pan.baidu.com/s/10kjngShwtrpdDnb7Mu4-lg)，获取`03-Support`支持环境内容添加到release目录内
:::


#### 打包

进入`package`目录，执行以下命令，进行打包：

```shell
# 打包
package.bat
```

打包成果放到了release目录内，通过`os-arch`进行分类存放，形成如下的目录结构。

```shell
├─package(打包脚本)
│  ├─package.bat
├─release(打包后自动生成)
│  ├─win-x86_64
│  ├─linux-x86_64
```

## Docker Compose打包

### 打包前提

先配置Docker环境，可参考[安装Docker和Docker Compose](/zh/guide/document/deploy.html#安装docker和docker-compose)

### 单体版

进入到`mapgis-local-packer`目录。

#### 环境准备

##### 调整镜像参数

编辑`docker/linux-arch/deploy.sh`中的镜像标签等参数。

##### 上传打包文件

将与Linux服务器架构一致的产品包和Docker脚本拷贝到具备联网环境的该服务器下。

> 比如linux-x86_64的产品包需要到x86_64架构的Linux环境下进行镜像制作（可通过uname -m查询架构）

形成如下的目录结构，确保docker、docker-package和release在同一目录下：

```text
├─docker
│  ├─linux-x86_64
├─docker-package
├─release(传统打包自动生成)
│  ├─linux-x86_64
├─docker-package(docker打包后自动生成)
```

#### 打包

```shell
# 进入到docker-package目录
cd docker-package
# 执行打包(根据服务器架构选择相应的打包脚本，比如当前是x86_64就选择package-linux-x86_64.sh脚本)*
sh package-linux-x86_64.sh
```

### 微服务版

进入到`mapgis-cloud-packer`目录。

#### 环境准备

##### 调整镜像参数

编辑`docker/linux-arch/deploy.sh`中的镜像标签等参数。

##### 上传打包文件

将与Linux服务器架构一致的产品包和Docker脚本拷贝到具备联网环境的该服务器下。

> 比如linux-x86_64的产品包需要到x86_64架构的Linux环境下进行镜像制作（可通过uname -m查询架构）

形成如下的目录结构，确保docker、docker-package和release在同一目录下：

```text
├─docker
│  ├─linux-x86_64
├─docker-package
├─release(传统打包自动生成)
│  ├─linux-x86_64
├─docker-package(docker打包后自动生成)
```

#### 打包

```shell
# 进入到docker-package目录
cd docker-package
# 执行打包(根据服务器架构选择相应的打包脚本，比如当前是x86_64就选择package-linux-x86_64.sh脚本)*
sh package-linux-x86_64.sh
```

## Kubernetes打包

### 单体版

进入到`mapgis-local-packer`目录。

#### 环境准备

将`k8s-package`拷贝到linux服务器下，然后将需要的依赖放置到`support`目录内，形成如下目录：

```text
├─k8s-package
│  ├─support(将依赖拷贝到该处)
│  │  ├─linux-x86-64
│  │  ├─linux-arm64
├─k8s-release(打包后自动生成)
```
#### 打包
```shell
# 进入到k8s-package目录
cd k8s-package

# 执行打包(根据服务器架构选择相应的打包脚本，比如当前是x86_64就选择package-linux-x86_64.sh脚本)
sh package-linux-x86_64.sh
```

### 微服务版

进入到`mapgis-cloud-packer`目录。

#### 环境准备

将`k8s-package`拷贝到linux服务器下，然后将需要的依赖放置到`support`目录内，形成如下目录：

```text
├─k8s-package
│  ├─support(将依赖拷贝到该处)
│  │  ├─linux-x86-64
│  │  ├─linux-arm64
├─k8s-release(打包后自动生成)
```

#### 打包

```shell
# 进入到k8s-package目录
cd k8s-package

# 执行打包(根据服务器架构选择相应的打包脚本，比如当前是x86_64就选择package-linux-x86_64.sh脚本)
sh package-linux-x86_64.sh
```
