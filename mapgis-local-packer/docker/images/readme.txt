加载镜像
1. 如果需要推送镜像到某镜像仓库，则在执行image-load.sh时传入该镜像仓库的地址，比如：10.0.1.123:5000，不传直接发布到本地
2. sudo chmod +x .image-load.sh && sudo ./image-load.sh
或
   sudo chmod +x .image-load.sh && sudo ./image-load.sh 10.0.1.123:5000