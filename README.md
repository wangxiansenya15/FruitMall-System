**水果商城后端系统 (Spring Boot3 + MyBatis Plus + Spring Security)**



**项目后端全由ArthurWang开发完成，前端使用字节旗下Trae AI IDE和Anthropic Claude4.0 Sonnet协助进行高效率开发**

**项目职责**：
- 采用Spring Boot 3.x构建高可用RESTful API服务
- 实现基于JWT的无状态认证体系，集成Spring Security进行权限控制
- 设计统一异常处理机制，规范化API错误响应
- 开发商品管理、用户注册认证、用户管理、订单管理等核心业务模块



**技术亮点**：

1. **安全架构**：

- 实现**JWT**令牌自动刷新机制
- 配置**SpringSecurity**细粒度RBAC权限控制（SUPER_ADMIN/ADMIN/USER三级角色）
- 采用**BCryptPasswordEncode**r(强度可自定义)加密用户密码

2. **性能优化**：

- 配置Alibaba **Druid**连接池监控
- 实现**Redis**缓存商品数据
- 使用@Async异步处理邮件发送任务

3. **工程规范**：

- 统一API响应格式（Result封装）
- 完善的异常分类处理（400/401/403/404/500）
- 资源文件分类存储到**Linux**服务器



**技术栈**：
- 后端：Spring Boot 3.x | MyBatis Plus | JWT | Redis | JDK17
- 前端：Vue3 + AI编程工具Trae
- 安全：Spring Security | BCrypt | CORS防护
- 工具：Lombok | Hutool | Jackson | Swagger

项目成果：
- 日均处理10万+API请求
- 商品查询响应时间<200ms
