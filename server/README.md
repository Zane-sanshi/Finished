# 后端部署教程和使用说明

## 后端部署环境

* JDK 1.8
* Maven 3.9.5 及以上
* MySQL 6.0 及以上

## 功能

* 增删改查
* 文件上传
* 数据库配置
* 日志管理
* 权限控制

## 后端部署流程

（本项目为 Maven 项目，建议使用自动识别 Maven 项目并下载依赖的 IDE 来部署本项目，例如 IntelliJ IDEA）

1. 使用合适的 IDE 打开本项目；
2. 将位于 application.yml 的 `server.port`，也即服务器端口号改成与前端一致（前端端口号的查看方法见 web 文件夹的 README.md）；
3. 将位于 application.yml 的 `DB_NAME` 改成与部署项目所使用的数据库名称一致，如果还没创建数据库，请在完成该流程后查看“数据库相关”一栏；
4. 将位于 application.yml 的 `BASE_LOCATION` 改成本项目主目录 java_job 的绝对路径；
5. 如需变更后端的运行日志的输出位置，则需修改 logback-spring.xml 下的 `LOG_HOME` 的 value 值；

（下面是将项目部署到远程服务器中才需要做的东西）

6. 在 server 目录下依次运行命令 `maven clean` 和 `maven package`，生成后端项目的 jar 包；

7. 将 jar 包复制到远程服务器；

8. 将 upload 文件夹复制到远程服务器；

9. 迁移 mysql 数据库；

10. 根据自己远程服务器的配置，将 start.sh 中的 xx 字符替换掉后，在服务器上运行 start.sh；

### 数据库相关

如果在上文的准备中没有修改 `DB_NAME`，那么就可以根据下面的 sql 语句来执行，否则需要将 `java_job` 换成与 `DB_NAME` 修改后的内容一致的数据库名。

在部署项目前需要创建数据库：

`CREATE DATABASE IF NOT EXISTS java_job DEFAULT CHARSET utf8 COLLATE utf8_general_ci;`

执行本项目根目录下的 java_job.sql 会创建该项目所需的表以及创建管理员用户，管理员用户的的账号和密码均为 `admin123`。

如果要把本地数据库信息迁移到远程服务器，则需现在本地备份数据库：

`mysqldump -u root -p --databases java_job > java_job_backup.sql`

将 java_job_backup.sql 上传到远程服务器后，在服务器上执行数据库恢复命令：

`source java_job_backup.sql`


### 常见问题

#### MySQL 主键 id 过长

https://blog.csdn.net/qq_46728644/article/details/120772577

#### yml 不起作用

需要运行 `maven clean` 命令。 

#### 注意实体字段最好是 String 类型

实体字段最好是 String 类型，MyBatis-Plus 的 update 的时候，null 的不更新。





