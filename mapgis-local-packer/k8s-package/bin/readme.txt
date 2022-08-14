1. 环境要求
    a. Kubernetes 1.9及以上
    b. 所需硬件资源
        CPU：4核
        内存：16GB+
        硬盘：200GB+
    c. NFS Server
    d. 私有镜像仓库（如果可以访问阿里云镜像仓库，则不需要搭建）

2. 将k8s版的包放到linux服务器的某一目录，如：/opt

2. 安装
    进入应用根目录
    执行以下命令，进行安装
    sudo chmod +x install.sh && sudo ./install.sh

3. 调整参数【必须】
    编辑values.yaml，根据需要修改相关参数，运行sudo vi values.yaml

    # 必填，外部访问k8s的IP，可以是k8s集群内任意一台外部可访问的机器IP，如：192.168.130.10
    k8sPublicIp: 

    # 选填，应用端口，自定义范围30000-32767，默认为32000
    appPort: 32000

    # 必填，镜像仓库地址，默认为阿里云镜像仓库（外网推荐使用）：registry.cn-beijing.aliyuncs.com
    # 内网私有镜像仓库：<ip>:5000，<ip>为镜像仓库所在的机器IP，例如：192.168.177.1:5000
    imageRegistry: 

    # 必填，NFS Server的地址，用于持久化存储应用数据，例如：192.168.130.10
    nfsServer: 

    # 必填，NFS Server提供的挂载路径，默认是根路径: /，请根据实际情况填写，例如：/nfs/data
    nfsPath: /

    # 选填，用于存储应用数据，它跟NFS Server都用于存储数据，如果和NFS Server同时存在，优先使用NFS Server存储应用数据
    nfsStorageClassName=

4、启动/停止
    启动
    sudo ./startup.sh
    停止
    sudo ./shutdown.sh

5、访问应用
    http://{ip}:{appPort}
    其中{ip}为外部访问k8s的IP，{appPort}为应用端口