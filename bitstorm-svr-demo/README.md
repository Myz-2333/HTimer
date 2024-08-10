# spring boot 模板服务

构建子服务项目模板

## 接入能力

* spring cloud nacos 服务注册
* spring cloud nacos 配置中心
* spring cloud open feign 服务通信
* lombok 简化开发

## 模板使用

1. 复制`bitstorm-svr-demo`修改项目名为新项目名称(建议本地文件夹复制，不要在idel中复制)
2. 修改新项目`pom.xml`文件`artifactId`节点为新项目名称 
3. 修改根项目`pom.xml`文件`modules`节点，加入新项目
4. 修改`package`包名，包括 import 的包名 
5. 修改`启动类名称`