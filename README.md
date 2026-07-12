# PulseFit — 连锁健身俱乐部运营中心

> Spring Boot 3 + Vue 3 全栈健身俱乐部运营管理系统，涵盖训练营、会员、教练与部门管理。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.3 + JDK 21 |
| 数据库 | MySQL 8.0 + MyBatis |
| 认证 | JWT Token |
| AOP | 自定义注解操作日志 |
| 文件存储 | 阿里云 OSS |
| 前端框架 | Vue 3 + Vue Router + Axios + Vite |
| UI 组件库 | Element Plus |

## 项目结构

```
pulsefit/
├── pulsefit-parent/        # Maven 父 POM（依赖管理与版本控制）
├── pulsefit-common/        # 公共模块（实体类、DTO、通用响应体）
├── pulsefit-utils/         # 工具模块（JWT 鉴权、OSS 上传）
├── pulsefit-server/        # 主服务模块
│   ├── controller/         # RESTful API 控制器
│   ├── service/            # 业务逻辑层
│   ├── mapper/             # MyBatis 数据访问层
│   ├── aop/                # 操作日志切面
│   ├── config/             # Web 与 CORS 配置
│   ├── interceptor/        # Token 拦截器
│   ├── filter/             # 请求过滤器
│   └── exception/          # 全局异常处理
└── frontend/               # Vue 3 + Element Plus 前端
    └── src/views/          # 页面：登录 / 运营总览 / 部门 / 员工 / 训练营 / 会员 / 操作审计 / 数据看板
```

## API 端点

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 登录认证 | POST | `/auth/login` | JWT 登录 |
| 运营部门 | GET/POST/PUT/DELETE | `/departments` | 部门 CRUD |
| 教练与员工 | GET/POST/PUT/DELETE | `/staff` | 员工管理（含履历） |
| 训练营 | GET/POST/PUT/DELETE | `/training-camps` | 训练营管理 |
| 会员 | GET/POST/PUT/DELETE | `/members` | 会员档案管理 |
| 数据看板 | GET | `/analytics/*` | 岗位/性别/训练营/会员等级统计 |
| 操作审计 | GET | `/operation-logs` | 操作日志查询 |
| 文件上传 | POST | `/uploads` | OSS 图片上传 |

## 数据库

| 表名 | 说明 |
|------|------|
| `pf_department` | 运营部门 |
| `pf_staff` | 教练与运营人员 |
| `pf_staff_experience` | 员工工作经历 |
| `pf_training_camp` | 训练营 |
| `pf_member` | 会员档案 |
| `pf_operation_log` | 操作审计日志 |

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Node.js 16+

### 后端启动

```bash
cd pulsefit-parent
mvn clean install -DskipTests
cd ../pulsefit-server
mvn spring-boot:run
```

后端默认运行在 `http://localhost:8080`。

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端开发服务器默认运行在 `http://localhost:5173`，API 请求自动代理到后端 8080 端口。

### 生产部署

```bash
# 前端构建
cd frontend && npm run build

# 将 dist/ 部署到 Nginx，配置反向代理 /api/ 到后端 8080
```

## 功能模块

- **运营总览** — 核心指标仪表盘 + 今日训练安排时间轴
- **运营部门** — 部门 CRUD
- **教练与员工** — 员工分页查询、条件筛选、批量操作、工作履历
- **训练营管理** — 训练营全生命周期管理（招募 → 进行 → 结束）
- **会员管理** — 会员档案、服务预警、风险积分
- **数据看板** — 岗位分布、性别比例、训练营人数、会员等级分布
- **操作审计** — AOP 自动记录操作人、方法、参数、耗时
- **登录认证** — JWT 无状态认证

## License

MIT
