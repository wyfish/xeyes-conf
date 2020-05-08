
## Introduction
XEyes-CONF is a lightweight distributed configuration management platform, 
with features such as "lightweight, second dynamic push, multi-environment, cross-language, cross-room deployment, configuration listening, permission control, version rollback".
Now, it's already open source, real "out-of-the-box".

XEyes-CONF 是一个轻量级分布式配置管理平台，拥有"轻量级、秒级动态推送、多环境、跨语言、跨机房、配置监听、权限控制、版本回滚"等特性，开箱即用。

## Features
- 轻量: 部署简单，不依赖第三方服务；
- HA：配置中心支持集群部署，提升配置中心系统容灾和可用性。
- 提供配置中心, 通过Web界面在线操作配置数据；
- 多环境支持：环境之间相互隔离；
- 多数据类型配置；
- 跨语言：底层通过http服务（long-polling）拉取配置数据并实时感知配置变更，从而实现多语言支持。
- 跨机房;
- 实时性：不需要重启线上机器;
- 配置变更监听功能；
- 最终一致性；
- 配置备份: 配置数据同时在磁盘与MySQL中存储和备份;
- 支持 "API、 注解、XML占位符" 等多种方式获取配置；
- 兼容Spring原生配置； 
- 分布式;
- 项目隔离;
- 客户端断线重连；
- 空配置处理；
- 用户管理；
- 配置权限控制；
- 历史版本回滚；
- 配置快照；

## Development

欢迎大家的关注和使用，拥抱变化，持续发展。


## Contributing
Contributions are welcome! Open a pull request to fix a bug, or open an [Issue](https://github.com/wyfish/xeyes-conf/issues/) to discuss a new feature or change.

欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/wyfish/xeyes-conf/issues/) 讨论新特性或者变更。


## Copyright and License
This product is open source and free, and will continue to provide free community technical support. Individual or enterprise users are free to access and use.

- Licensed under the GNU General Public License (GPL) v3.


