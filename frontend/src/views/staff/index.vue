<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const formRef = ref()
const submitting = ref(false)
const departmentOptions = ref([])
const selectedRows = ref([])

const searchForm = reactive({ name: '', gender: '', begin: '', end: '' })
const pagination = reactive({ page: 1, pageSize: 10 })

const form = reactive({
  id: null, username: '', password: '', name: '', gender: null,
  phone: '', role: null, salary: null, image: '', entryDate: '', departmentId: null,
})

const rules = {
  username: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  password: form.id ? [] : [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const genderMap = { 1: '男', 2: '女' }
const roleOptions = ['主教练', '助理教练', '运营主管', '课程顾问', '门店经理']

const fetchData = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, pageSize: pagination.pageSize, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] == null) delete params[k] })
    const res = await request.get('/staff', { params })
    if (res.code === 1) { tableData.value = res.data.rows; total.value = res.data.total }
    else ElMessage.error(res.msg || '加载员工数据失败')
  } catch { ElMessage.error('无法连接运营服务') }
  finally { loading.value = false }
}

const fetchDepartmentOptions = async () => {
  try { const res = await request.get('/departments'); if (res.code === 1) departmentOptions.value = res.data || [] } catch { /* ignore */ }
}

const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, { id: null, username: '', password: '', name: '', gender: null, phone: '', role: null, salary: null, image: '', entryDate: '', departmentId: null })
}

const openAdd = () => { resetForm(); dialogTitle.value = '新增员工'; dialogVisible.value = true }

const openEdit = async (row) => {
  try {
    const res = await request.get(`/staff/${row.id}`)
    if (res.code === 1) {
      const d = res.data
      Object.assign(form, {
        id: d.id, username: d.username, name: d.name, gender: d.gender,
        phone: d.phone, role: d.role, salary: d.salary, image: d.image,
        entryDate: d.entryDate, departmentId: d.departmentId,
      })
      form.password = ''
      dialogTitle.value = '编辑员工信息'
      dialogVisible.value = true
    }
  } catch { ElMessage.error('获取员工信息失败') }
}

const handleSubmit = async () => {
  if (!await formRef.value.validate().catch(() => false)) return
  submitting.value = true
  try {
    const payload = { ...form }
    if (!payload.password) delete payload.password
    const res = form.id ? await request.put('/staff', payload) : await request.post('/staff', payload)
    if (res.code === 1) {
      ElMessage.success(form.id ? '员工信息已更新' : '员工已创建')
      dialogVisible.value = false
      fetchData()
    } else ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('请求失败') }
  finally { submitting.value = false }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除员工「${row.name}」？`, '确认删除', { type: 'warning', confirmButtonText: '确认删除', cancelButtonText: '取消' })
    const res = await request.delete('/staff', { params: { ids: [row.id] } })
    if (res.code === 1) { ElMessage.success('已删除'); fetchData() }
    else ElMessage.error(res.msg || '删除失败')
  } catch { /* cancelled */ }
}

const handleBatchDelete = async () => {
  if (!selectedRows.value.length) return ElMessage.warning('请先选择员工')
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 名员工？`, '批量删除', { type: 'warning', confirmButtonText: '确认删除', cancelButtonText: '取消' })
    const res = await request.delete('/staff', { params: { ids: selectedRows.value.map(r => r.id) } })
    if (res.code === 1) { ElMessage.success('已删除'); fetchData() }
    else ElMessage.error(res.msg || '删除失败')
  } catch { /* cancelled */ }
}

const handleSelectionChange = (rows) => { selectedRows.value = rows }
const handleSizeChange = (v) => { pagination.pageSize = v; fetchData() }
const handlePageChange = (v) => { pagination.page = v; fetchData() }

onMounted(() => { fetchData(); fetchDepartmentOptions() })
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><span class="section-label">TEAM</span><h1>教练与员工</h1><p>管理所有门店教练、运营员工及岗位信息</p></div>
      <el-button type="primary" @click="openAdd"><el-icon><Plus /></el-icon> 新增员工</el-button>
    </div>

    <section class="surface search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="姓名"><el-input v-model="searchForm.name" placeholder="员工姓名" clearable style="width:170px" /></el-form-item>
        <el-form-item label="性别"><el-select v-model="searchForm.gender" placeholder="全部" clearable style="width:110px"><el-option label="男" :value="1" /><el-option label="女" :value="2" /></el-select></el-form-item>
        <el-form-item label="入职日期"><el-date-picker v-model="searchForm.begin" type="date" placeholder="起始" value-format="YYYY-MM-DD" style="width:145px" /></el-form-item>
        <el-form-item label="至"><el-date-picker v-model="searchForm.end" type="date" placeholder="截止" value-format="YYYY-MM-DD" style="width:145px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="pagination.page=1;fetchData()"><el-icon><Search /></el-icon> 搜索</el-button><el-button @click="Object.assign(searchForm,{name:'',gender:'',begin:'',end:''});pagination.page=1;fetchData()">重置</el-button></el-form-item>
      </el-form>
    </section>

    <section class="surface" style="margin-bottom:14px">
      <div class="batch-bar" v-if="selectedRows.length"><span>已选 {{ selectedRows.length }} 人</span><el-button type="danger" size="small" @click="handleBatchDelete">批量删除</el-button></div>
      <el-table v-loading="loading" :data="tableData" stripe style="width:100%" @selection-change="handleSelectionChange">
        <template #empty><div class="empty-state"><el-icon :size="40"><UserFilled /></el-icon><p>暂无员工数据</p><small>点击「新增员工」录入教练或运营人员</small></div></template>
        <el-table-column type="selection" width="50" />
        <el-table-column type="index" label="编号" width="70" align="center" />
        <el-table-column prop="name" label="姓名" width="110" show-overflow-tooltip />
        <el-table-column label="性别" width="70" align="center">
          <template #default="{ row }">{{ genderMap[row.gender] || '—' }}</template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" align="center" />
        <el-table-column prop="role" label="岗位" min-width="120" align="center" />
        <el-table-column prop="departmentName" label="所属部门" min-width="120" align="center" />
        <el-table-column prop="entryDate" label="入职日期" width="115" align="center" />
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openEdit(row)"><el-icon><Edit /></el-icon> 编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)"><el-icon><Delete /></el-icon> 删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-foot"><span v-if="total">共 {{ total }} 人</span><el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.pageSize" :total="total" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" /></div>
    </section>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="680px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" label-position="top">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="登录账号" prop="username"><el-input v-model="form.username" placeholder="员工登录账号" :disabled="!!form.id" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="登录密码" prop="password"><el-input v-model="form.password" type="password" placeholder="留空则不修改" show-password /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="姓名" prop="name"><el-input v-model="form.name" placeholder="真实姓名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="性别" prop="gender"><el-radio-group v-model="form.gender"><el-radio :label="1">男</el-radio><el-radio :label="2">女</el-radio></el-radio-group></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" placeholder="手机号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="岗位" prop="role"><el-select v-model="form.role" placeholder="选择岗位" clearable style="width:100%"><el-option v-for="r in roleOptions" :key="r" :label="r" :value="r" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="薪资" prop="salary"><el-input-number v-model="form.salary" :min="0" :step="1000" placeholder="月薪" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="入职日期" prop="entryDate"><el-date-picker v-model="form.entryDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="所属部门" prop="departmentId"><el-select v-model="form.departmentId" placeholder="选择所属运营部门" clearable style="width:100%"><el-option v-for="d in departmentOptions" :key="d.id" :label="d.name" :value="d.id" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<style scoped>
.search-bar { padding: 18px 22px 4px; margin-bottom: 0; }
.table-foot { display: flex; align-items: center; justify-content: space-between; padding: 14px 18px; color: var(--pf-ink-soft); font-size: 13px; }
.empty-state { padding: 48px 20px; text-align: center; color: var(--pf-ink-soft); }
.empty-state p { margin: 12px 0 4px; font-weight: 600; }
.empty-state small { font-size: 12px; color: #93988f; }
.batch-bar { display: flex; align-items: center; gap: 12px; padding: 10px 18px; background: #fef7f6; border-bottom: 1px solid #fddcd8; font-size: 13px; }
</style>
