-- ============================================
-- PulseFit 数据库迁移脚本
-- 新增：角色权限、课程预约、会员卡、器材报修、财务统计
-- ============================================

-- 1. 员工表增加系统角色字段
ALTER TABLE pf_staff ADD COLUMN sys_role VARCHAR(20) DEFAULT 'COACH' COMMENT '系统角色: SUPER_ADMIN/ADMIN/COACH';

-- 2. 课程表（教练排班）
CREATE TABLE IF NOT EXISTS pf_course (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    coach_id INT NOT NULL COMMENT '教练ID',
    coach_name VARCHAR(50) COMMENT '教练姓名',
    course_date DATE NOT NULL COMMENT '上课日期',
    start_time TIME NOT NULL COMMENT '开始时间',
    end_time TIME NOT NULL COMMENT '结束时间',
    max_capacity INT DEFAULT 20 COMMENT '最大预约人数',
    current_booked INT DEFAULT 0 COMMENT '当前已预约人数',
    location VARCHAR(100) COMMENT '上课地点',
    status VARCHAR(20) DEFAULT 'SCHEDULED' COMMENT '状态: SCHEDULED/CANCELLED/FINISHED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_coach_time (coach_id, course_date, start_time) COMMENT '同一教练同时段唯一约束'
) COMMENT '课程排班表';

-- 3. 课程预约表
CREATE TABLE IF NOT EXISTS pf_booking (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL COMMENT '课程ID',
    member_id INT NOT NULL COMMENT '会员ID',
    member_name VARCHAR(50) COMMENT '会员姓名',
    booking_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
    status VARCHAR(20) DEFAULT 'BOOKED' COMMENT '状态: BOOKED/CANCELLED/SIGNED_IN/ABSENT',
    sign_in_time DATETIME COMMENT '签到时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_member_course (member_id, course_id) COMMENT '同一会员同一课程唯一预约',
    INDEX idx_course_id (course_id),
    INDEX idx_member_id (member_id)
) COMMENT '课程预约表';

-- 4. 会员卡表
CREATE TABLE IF NOT EXISTS pf_member_card (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL UNIQUE COMMENT '会员ID',
    member_name VARCHAR(50) COMMENT '会员姓名',
    card_type VARCHAR(30) COMMENT '卡类型: 次卡/月卡/季卡/年卡',
    total_times INT DEFAULT 0 COMMENT '总次数（次卡用）',
    remaining_times INT DEFAULT 0 COMMENT '剩余次数',
    expire_date DATE COMMENT '到期日期',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/FROZEN/EXPIRED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_member_id (member_id)
) COMMENT '会员卡表';

-- 5. 签到消费记录表
CREATE TABLE IF NOT EXISTS pf_checkin_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL COMMENT '会员ID',
    member_name VARCHAR(50) COMMENT '会员姓名',
    card_id INT COMMENT '会员卡ID',
    checkin_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    course_id INT COMMENT '关联课程ID',
    course_name VARCHAR(100) COMMENT '课程名称',
    times_consumed INT DEFAULT 1 COMMENT '消耗次数（次卡）',
    remaining_after INT COMMENT '消费后剩余次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_member_id (member_id),
    INDEX idx_checkin_time (checkin_time)
) COMMENT '签到消费记录表';

-- 6. 器材表
CREATE TABLE IF NOT EXISTS pf_equipment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    equipment_no VARCHAR(50) UNIQUE COMMENT '器材编号',
    equipment_name VARCHAR(100) NOT NULL COMMENT '器材名称',
    category VARCHAR(50) COMMENT '器材分类',
    purchase_date DATE COMMENT '购买日期',
    status VARCHAR(30) DEFAULT 'NORMAL' COMMENT '状态: NORMAL/REPORTED/AUDITING/REPAIRING/FIXED/SCRAPPED',
    location VARCHAR(100) COMMENT '存放位置',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '器材表';

-- 7. 器材报修记录表
CREATE TABLE IF NOT EXISTS pf_equipment_repair (
    id INT AUTO_INCREMENT PRIMARY KEY,
    equipment_id INT NOT NULL COMMENT '器材ID',
    equipment_no VARCHAR(50) COMMENT '器材编号',
    equipment_name VARCHAR(100) COMMENT '器材名称',
    reporter_id INT COMMENT '报修人ID',
    reporter_name VARCHAR(50) COMMENT '报修人姓名',
    fault_desc VARCHAR(500) COMMENT '故障描述',
    report_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报修时间',
    audit_status VARCHAR(30) DEFAULT 'PENDING' COMMENT '审核状态: PENDING/APPROVED/REJECTED',
    audit_time DATETIME COMMENT '审核时间',
    auditor_name VARCHAR(50) COMMENT '审核人',
    repair_start_time DATETIME COMMENT '维修开始时间',
    repair_end_time DATETIME COMMENT '维修结束时间',
    repair_desc VARCHAR(500) COMMENT '维修描述',
    repair_cost DECIMAL(10,2) DEFAULT 0 COMMENT '维修费用',
    status VARCHAR(30) DEFAULT 'REPORTED' COMMENT '整体状态: REPORTED→AUDITING→REPAIRING→FIXED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_equipment_id (equipment_id)
) COMMENT '器材报修记录表';

-- 8. 财务收支记录表
CREATE TABLE IF NOT EXISTS pf_finance_record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    record_type VARCHAR(20) NOT NULL COMMENT '类型: INCOME/EXPENSE',
    category VARCHAR(50) COMMENT '分类: MEMBERSHIP_FEE/COURSE_FEE/EQUIPMENT_REPAIR/EQUIPMENT_PURCHASE/OTHER',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    description VARCHAR(255) COMMENT '描述',
    related_id INT COMMENT '关联单据ID',
    record_date DATE NOT NULL COMMENT '记账日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_record_date (record_date),
    INDEX idx_record_type (record_type)
) COMMENT '财务收支记录表';
