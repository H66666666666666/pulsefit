# Tlias 教学管理系统

> Spring Boot + Vue 3 全栈教学管理系统 — 班级/学生/员工/部门管理

## 项目简介

一个完整的教学管理系统，前后端分离架构。后端 Spring Boot 提供 RESTful API，前端 Vue 3 提供管理界面。支持班级管理、学生管理、员工管理、部门管理、数据报表、文件上传等功能。

## 技术栈

| 层级 | 技术 |
|------|------|
| **后端框架** | Spring Boot 3 + JDK 21 |
| **数据库** | MySQL 8.0 + MyBatis |
| **认证** | JWT Token |
| **AOP** | 自定义注解操作日志 |
| **文件存储** | 阿里云 OSS |
| **前端** | Vue 3 + Vue Router + Axios + Vite |
| **UI 组件库** | Element Plus |

## 项目结构

```
tlias-management/
├── tlias-parent/          # Maven 父 POM（依赖管理）
├── tlias-pojo/            # 实体类、DTO、VO
├── tlias-utils/           # 工具类（JWT、OSS）
├── tlias-web-management/  # 后端主服务（Controller → Service → Mapper）
│   ├── controller/        # RESTful API
│   ├── service/           # 业务逻辑层
│   ├── mapper/            # 数据访问层
│   ├── config/            # Web 配置
│   ├── interceptor/       # Token 拦截器
│   ├── aop/               # 操作日志切面
│   └── exception/         # 全局异常处理
└── frontend/              # Vue 3 前端
    └── src/views/         # 页面：登录/部门/员工/学生/班级/日志/报表
```

## 快速开始

### 后端

```bash
cd tlias-web-management
mvn spring-boot:run
```

### 前端

```bash
cd frontend
npm install
npm run dev
```

## 功能模块

- 部门管理：CRUD
- 员工管理：分页查询、条件筛选
- 班级管理：班级 CRUD
- 学生管理：学生信息管理
- 登录认证：JWT Token
- 文件上传：OSS 图片上传
- 操作日志：AOP 自动记录
- 数据统计：员工报表、学生报表

## 许可证

MIT License
