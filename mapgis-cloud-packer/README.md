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

> 将与系统兼容的 release/xxx 下的目录发布到该系统下，然后运行启动脚本即可。
