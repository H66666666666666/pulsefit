<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新建训练营')
const formRef = ref()
const submitting = ref(false)
const staffOptions = ref([])

const searchForm = reactive({ name: '', begin: '', end: '' })
const pagination = reactive({ page: 1, pageSize: 10 })

const form = reactive({
  id: null, name: '', venue: '', startDate: '', endDate: '',
  headCoachId: null, programType: null,
})

const rules = {
  name: [{ required: true, message: '请输入训练营名称', trigger: 'blur' }],
  venue: [{ required: true, message: '请输入训练场地', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开营日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结营日期', trigger: 'change' }],
}

const programTypeOptions = ['体能训练', '力量训练', '燃脂塑形']
const statusTagType = (s) => s === '招募中' ? 'info' : s === '进行中' ? 'warning' : 'success'

const fetchData = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, pageSize: pagination.pageSize, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] == null) delete params[k] })
    const res = await request.get('/training-camps', { params })
    if (res.code === 1) {
      tableData.value = res.data.rows
      total.value = res.data.total
    } else {
      ElMessage.error(res.msg || '加载训练营数据失败')
    }
  } catch { ElMessage.error('无法连接运营服务') }
  finally { loading.value = false }
}

const fetchStaffOptions = async () => {
  try {
    const res = await request.get('/staff/list')
    if (res.code === 1) staffOptions.value = res.data || []
  } catch { /* ignore */ }
}

const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, { id: null, name: '', venue: '', startDate: '', endDate: '', headCoachId: null, programType: null })
}

const openAdd = () => {
  resetForm()
  dialogTitle.value = '新建训练营'
  dialogVisible.value = true
}

const openEdit = async (row) => {
  try {
    const res = await request.get(`/training-camps/${row.id}`)
    if (res.code === 1) {
      const d = res.data
      Object.assign(form, {
        id: d.id, name: d.name, venue: d.venue,
        startDate: d.startDate, endDate: d.endDate,
        headCoachId: d.headCoachId, programType: d.programType,
      })
      dialogTitle.value = '编辑训练营'
      dialogVisible.value = true
    }
  } catch { ElMessage.error('获取训练营信息失败') }
}

const handleSubmit = async () => {
  if (!await formRef.value.validate().catch(() => false)) return
  submitting.value = true
  try {
    const payload = { ...form }
    const res = form.id
      ? await request.put('/training-camps', payload)
      : await request.post('/training-camps', payload)
    if (res.code === 1) {
      ElMessage.success(form.id ? '训练营已更新' : '训练营已创建')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch { ElMessage.error('请求失败') }
  finally { submitting.value = false }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除训练营「${row.name}」？该操作不可恢复。`, '确认删除', { type: 'warning', confirmButtonText: '确认删除', cancelButtonText: '取消' })
    const res = await request.delete(`/training-camps/${row.id}`)
    if (res.code === 1) { ElMessage.success('已删除'); fetchData() }
    else ElMessage.error(res.msg || '删除失败')
  } catch { /* cancelled */ }
}

const handleSizeChange = (v) => { pagination.pageSize = v; fetchData() }
const handlePageChange = (v) => { pagination.page = v; fetchData() }

onMounted(() => { fetchData(); fetchStaffOptions() })
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><span class="section-label">TRAINING CAMPS</span><h1>训练营管理</h1><p>创建、排期与管理所有门店训练营项目</p></div>
      <el-button type="primary" @click="openAdd"><el-icon><Plus /></el-icon> 新建训练营</el-button>
    </div>

    <section class="surface search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="名称"><el-input v-model="searchForm.name" placeholder="训练营名称" clearable style="width:200px" /></el-form-item>
        <el-form-item label="开营日期"><el-date-picker v-model="searchForm.begin" type="date" placeholder="起始" value-format="YYYY-MM-DD" style="width:150px" /></el-form-item>
        <el-form-item label="至"><el-date-picker v-model="searchForm.end" type="date" placeholder="截止" value-format="YYYY-MM-DD" style="width:150px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="pagination.page=1;fetchData()"><el-icon><Search /></el-icon> 搜索</el-button><el-button @click="Object.assign(searchForm,{name:'',begin:'',end:''});pagination.page=1;fetchData()">重置</el-button></el-form-item>
      </el-form>
    </section>

    <section class="surface">
      <el-table v-loading="loading" :data="tableData" stripe style="width:100%">
        <template #empty><div class="empty-state"><el-icon :size="40"><Calendar /></el-icon><p>暂无训练营数据</p><small>点击「新建训练营」创建第一个训练营</small></div></template>
        <el-table-column type="index" label="编号" width="70" align="center" />
        <el-table-column prop="name" label="训练营名称" min-width="140" show-overflow-tooltip />
        <el-table-column prop="venue" label="训练场地" min-width="120" show-overflow-tooltip />
        <el-table-column prop="programType" label="训练科目" min-width="110" align="center" />
        <el-table-column prop="headCoachName" label="主管教练" width="110" align="center" />
        <el-table-column prop="startDate" label="开营日期" width="120" align="center" />
        <el-table-column prop="endDate" label="结营日期" width="120" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }"><el-tag :type="statusTagType(row.status)" size="small">{{ row.status || '—' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openEdit(row)"><el-icon><Edit /></el-icon> 编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)"><el-icon><Delete /></el-icon> 删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-foot"><span v-if="total">共 {{ total }} 条</span><el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.pageSize" :total="total" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" /></div>
    </section>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="620px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" label-position="top">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="训练营名称" prop="name"><el-input v-model="form.name" placeholder="如：夏季燃脂挑战营" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="训练场地" prop="venue"><el-input v-model="form.venue" placeholder="如：团体训练室 A" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="开营日期" prop="startDate"><el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结营日期" prop="endDate"><el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="主管教练" prop="headCoachId"><el-select v-model="form.headCoachId" placeholder="选择教练" clearable style="width:100%"><el-option v-for="e in staffOptions" :key="e.id" :label="e.name" :value="e.id" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="训练科目" prop="programType"><el-select v-model="form.programType" placeholder="选择科目" clearable style="width:100%"><el-option v-for="p in programTypeOptions" :key="p" :label="p" :value="p" /></el-select></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<style scoped>
.search-bar { padding: 18px 22px 4px; margin-bottom: 14px; }
.table-foot { display: flex; align-items: center; justify-content: space-between; padding: 14px 18px; color: var(--pf-ink-soft); font-size: 13px; }
.empty-state { padding: 48px 20px; text-align: center; color: var(--pf-ink-soft); }
.empty-state p { margin: 12px 0 4px; font-weight: 600; }
.empty-state small { font-size: 12px; color: #93988f; }
</style>
