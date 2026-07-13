# PulseFit — 连锁健身俱乐部运营中心

> Spring Boot 3 + Vue 3 全栈健身俱乐部运营管理系统，涵盖课程预约、会员卡消费、器材报修、财务统计、员工与部门管理。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.3 + JDK 21 |
| 数据库 | MySQL 8.0 + MyBatis |
| 认证 | JWT Token（含 sysRole RBAC） |
| AOP | 自定义注解操作日志 |
| 文件存储 | 阿里云 OSS |
| 前端框架 | Vue 3 + Vue Router + Axios + Vite |
| UI 组件库 | Element Plus |

## 项目结构

```
pulsefit/
├── pulsefit-parent/        # Maven 父 POM
├── pulsefit-common/        # 公共模块（实体类、DTO、通用响应体）
├── pulsefit-utils/         # 工具模块（JWT 鉴权、OSS 上传）
├── pulsefit-server/        # 主服务模块
│   ├── controller/         # 15 个 RESTful API 控制器
│   ├── service/            # 业务逻辑层
│   ├── mapper/             # MyBatis 数据访问层 (13 个)
│   ├── aop/                # 操作日志切面
│   ├── config/             # Web 与 CORS 配置
│   └── exception/          # 全局异常处理
├── frontend/               # Vue 3 + Element Plus 前端
│   └── src/views/          # 13 个功能页面
└── database/               # SQL 建表脚本
```

## API 端点

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 登录认证 | POST | `/auth/login` | JWT 登录（含 sysRole） |
| 运营部门 | GET/POST/PUT/DELETE | `/departments` | 部门 CRUD |
| 教练与员工 | GET/POST/PUT/DELETE | `/staff` | 员工管理（含履历） |
| 训练营 | GET/POST/PUT/DELETE | `/training-camps` | 训练营管理 |
| 会员 | GET/POST/PUT/DELETE | `/members` | 会员档案管理 |
| **课程排班** | GET/POST/PUT/DELETE | `/courses` | 课程管理（NEW） |
| **课程预约** | GET/POST/PUT | `/bookings` | 预约 + 签到扣费（NEW） |
| **会员卡** | GET/POST/PUT | `/member-cards` | 次卡/月卡/季卡/年卡（NEW） |
| **器材管理** | GET/POST/PUT | `/equipment` | 器材台账 + 报修流程（NEW） |
| **器材报修** | GET/POST/PUT | `/equipment/repairs` | 报修→审核→维修→修复（NEW） |
| **财务统计** | GET/POST | `/finances` | 收支记录 + 汇总（NEW） |
| 数据看板 | GET | `/analytics/*` | 岗位/性别/训练营/会员等级统计 |
| 操作审计 | GET | `/operation-logs` | 操作日志查询 |
| 文件上传 | POST | `/uploads` | OSS 图片上传 |

## 数据库

| 表名 | 说明 |
|------|------|
| `pf_department` | 运营部门 |
| `pf_staff` | 教练与运营人员（含 sys_role 系统角色） |
| `pf_staff_experience` | 员工工作经历 |
| `pf_training_camp` | 训练营 |
| `pf_member` | 会员档案 |
| `pf_operation_log` | 操作审计日志 |
| `pf_course` | 课程排班（NEW） |
| `pf_booking` | 课程预约记录（NEW） |
| `pf_member_card` | 会员卡（NEW） |
| `pf_checkin_log` | 签到消费日志（NEW） |
| `pf_equipment` | 器材台账（NEW） |
| `pf_equipment_repair` | 器材报修工单（NEW） |
| `pf_finance_record` | 财务收支记录（NEW） |

## 功能模块

### RBAC 权限体系
- **超管 (SUPER_ADMIN)**：全部菜单可见，含财务统计
- **管理员 (ADMIN)**：课程排班、器材管理等
- **教练 (COACH)**：仅会员管理 + 课程预约 + 数据看板
- 基于 JWT + 动态路由渲染差异化菜单

### 课程预约
- 课程排班管理（教练 + 时间 + 人数上限）
- 数据库唯一索引（`uk_member_course`）防重复预约
- 业务层乐观锁：先检查名额再 insert，防止超卖

### 会员卡消费
- 支持次卡/月卡/季卡/年卡
- 签到自动扣减次数，记录消费流水
- 剩余 ≤ 3 次前端标红预警

### 器材报修（全生命周期）
- 器材扫码报修 → 管理员审核 → 维修中 → 维修完成
- 每一步状态同步器材主表

### 财务统计
- 收支记录管理（会员费/课程费/器材维修/器材采购）
- 按时间段汇总收支

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Node.js 16+

### 数据库初始化

```bash
mysql -u root -p pulsefit < database/init.sql
mysql -u root -p pulsefit < pulsefit-server/src/main/resources/migration.sql
```

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

## License

MIT
