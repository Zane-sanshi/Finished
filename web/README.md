# 前端部署教程和使用说明

## 前端部署环境

* Node.js 16.14 及以上

## 前端部署流程

1. 将 constants.ts 中的 `BASE_URL`，也即服务器端口号，改成和后端一致（后端端口号的查看方法见 web 文件夹的 README.md）；
2. 如果要在本地部署，则在 web 目录下运行命令 `npm run dev` 即可；
3. 如果要在远程服务器上部署，则需要在 web 目录下运行命令 `vite build`，该命令会生成 dist 文件夹；
4. 然后将 dist 文件夹部署到远程服务器上。

## 配置文件的解释

1. env.development 开发环境配置
2. eslintrc.js 代码规范化提示
3. vite.config.js vite 开发服务器配置

## 常见问题

### 变量

https://blog.csdn.net/qq_41636947/article/details/117907448

### antd 的 css 引入方式

在 index.html 中引入了两个链接：

https://cdn.staticfile.org/ant-design-vue/3.2.20/antd.min.css

https://cdn.staticfile.org/echarts/4.3.0/echarts.min.js
