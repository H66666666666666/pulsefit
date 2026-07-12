<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增运营部门')
const formRef = ref()
const submitting = ref(false)

const form = reactive({ id: null, name: '' })
const rules = { name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }] }

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/departments')
    if (res.code === 1) tableData.value = res.data || []
    else ElMessage.error(res.msg || '加载部门数据失败')
  } catch { ElMessage.error('无法连接运营服务') }
  finally { loading.value = false }
}

const openAdd = () => {
  form.id = null; form.name = ''
  formRef.value?.resetFields()
  dialogTitle.value = '新增运营部门'
  dialogVisible.value = true
}

const openEdit = async (row) => {
  try {
    const res = await request.get(`/departments/${row.id}`)
    if (res.code === 1) {
      form.id = res.data.id; form.name = res.data.name
      dialogTitle.value = '编辑运营部门'
      dialogVisible.value = true
    }
  } catch { ElMessage.error('获取部门信息失败') }
}

const handleSubmit = async () => {
  if (!await formRef.value.validate().catch(() => false)) return
  submitting.value = true
  try {
    const res = form.id
      ? await request.put('/departments', { id: form.id, name: form.name })
      : await request.post('/departments', { name: form.name })
    if (res.code === 1) {
      ElMessage.success(form.id ? '部门已更新' : '部门已创建')
      dialogVisible.value = false
      fetchData()
    } else ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('请求失败') }
  finally { submitting.value = false }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除运营部门「${row.name}」？若部门下仍有员工，将无法删除。`, '确认删除', { type: 'warning', confirmButtonText: '确认删除', cancelButtonText: '取消' })
    const res = await request.delete('/departments', { params: { id: row.id } })
    if (res.code === 1) { ElMessage.success('已删除'); fetchData() }
    else ElMessage.error(res.msg || '删除失败')
  } catch { /* cancelled */ }
}

onMounted(() => fetchData())
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><span class="section-label">DEPARTMENTS</span><h1>运营部门</h1><p>管理门店运营组织架构中的各部门设置</p></div>
      <el-button type="primary" @click="openAdd"><el-icon><Plus /></el-icon> 新增部门</el-button>
    </div>

    <section class="surface">
      <el-table v-loading="loading" :data="tableData" stripe style="width:100%">
        <template #empty><div class="empty-state"><el-icon :size="40"><OfficeBuilding /></el-icon><p>暂无部门数据</p><small>点击「新增部门」创建第一个运营部门</small></div></template>
        <el-table-column type="index" label="编号" width="80" align="center" />
        <el-table-column prop="name" label="部门名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="190" align="center" />
        <el-table-column prop="updateTime" label="最近更新" width="190" align="center" />
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openEdit(row)"><el-icon><Edit /></el-icon> 编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)"><el-icon><Delete /></el-icon> 删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-foot"><span v-if="tableData.length">共 {{ tableData.length }} 个部门</span></div>
    </section>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="460px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="部门名称" prop="name"><el-input v-model="form.name" placeholder="例如：运营管理部、教练部、市场拓展部" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<style scoped>
.table-foot { display: flex; align-items: center; padding: 14px 18px; color: var(--pf-ink-soft); font-size: 13px; }
.empty-state { padding: 48px 20px; text-align: center; color: var(--pf-ink-soft); }
.empty-state p { margin: 12px 0 4px; font-weight: 600; }
.empty-state small { font-size: 12px; color: #93988f; }
</style>
