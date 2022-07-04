<h1 align="center">服务器产品打包器</h1>

## 开始

打包之前需先添加支持环境，请执行`add-support.bat`会自动添加`\\192.168.17.59\03-Support`环境到 release 目录内。

> 执行前，请先确保该目录能访问，账号为 support/support

```bash
add-support.bat
```

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

> 将与服务器架构一致的 release/xxx 下的目录发布到该服务器下，然后运行启动脚本即可。

## Docker镜像制作

将与Linux服务器架构一致的产品包和Docker脚本拷贝到具备联网环境的该服务器下
> 比如linux-x86_64的产品包需要到x86_64架构的Linux环境下进行镜像制作（可通过uname -m查询架构）

形成如下的目录结构，确保docker和release在同一目录下：

```text
├─docker
│  ├─linux-x86_64
├─release
│  ├─linux-x86_64
```

```shell
# 进入到docker脚本目录
cd docker/linux-x86_64
# 赋予权限
chmod -R 777 .
# 拷贝../../release/linux-x86_64下的包文件
./copy.sh 
# 制作镜像
./deploy.sh build
# 启动验证
./deploy.sh modules
# 发布到私有镜像仓库
./deploy.sh publish
```

## Docker Compose 运行（正式）
将`docker-compose.yml`和`.env`上传到服务器同一目录下

### 调整`.env`环境变量
```properties
# 私有镜像仓库地址，注意结尾需含/，默认为空
DOCKER_REGISTRY_URL=

# 应用端口
MAPGIS_PORT=8080
```

### 启动运行
```shell
# 进入到存放docker-compose.yml目录并执行赋予执行权限
chmod +x docker-compose.yml
# 前台启动
docker-compose up
# 后台启动
docker-compose up -d
```