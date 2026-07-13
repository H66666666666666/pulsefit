<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增课程')
const submitting = ref(false)
const form = reactive({ id: null, courseName: '', coachName: '', courseDate: '', startTime: '', endTime: '', maxCapacity: 20, location: '' })
const query = reactive({ coachName: '', status: '', page: 1, pageSize: 10 })
const total = ref(0)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/courses', { params: query })
    if (res.code === 1) { tableData.value = res.data.rows; total.value = res.data.total }
    else ElMessage.error(res.msg || '加载失败')
  } catch { ElMessage.error('无法连接服务') }
  finally { loading.value = false }
}

const openAdd = () => {
  Object.keys(form).forEach(k => form[k] = null)
  form.maxCapacity = 20
  dialogTitle.value = '新增课程'; dialogVisible.value = true
}
const openEdit = async (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑课程'; dialogVisible.value = true
}
const handleSubmit = async () => {
  if (!form.courseName || !form.coachName || !form.courseDate) { ElMessage.warning('请填写必填项'); return }
  submitting.value = true
  try {
    const res = form.id ? await request.put('/courses', form) : await request.post('/courses', form)
    if (res.code === 1) { ElMessage.success(form.id ? '更新成功' : '新增成功'); dialogVisible.value = false; fetchData() }
    else ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
  finally { submitting.value = false }
}
const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该课程？', '提示', { type: 'warning' })
  const res = await request.delete(`/courses/${row.id}`)
  if (res.code === 1) { ElMessage.success('删除成功'); fetchData() }
  else ElMessage.error(res.msg || '删除失败')
}

onMounted(fetchData)
</script>

<template>
  <div>
    <el-form :inline="true" :model="query" @submit.prevent="fetchData">
      <el-form-item><el-input v-model="query.coachName" placeholder="教练姓名" clearable></el-input></el-form-item>
      <el-form-item><el-select v-model="query.status" placeholder="状态" clearable><el-option label="已排班" value="SCHEDULED"/><el-option label="已取消" value="CANCELLED"/></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="fetchData">查询</el-button><el-button @click="openAdd">新增课程</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" v-loading="loading" border>
      <el-table-column prop="courseName" label="课程名称"/><el-table-column prop="coachName" label="教练"/>
      <el-table-column prop="courseDate" label="日期"/><el-table-column prop="startTime" label="开始"/><el-table-column prop="endTime" label="结束"/>
      <el-table-column prop="currentBooked" label="已预约"/><el-table-column prop="maxCapacity" label="上限"/>
      <el-table-column prop="status" label="状态"><template #default="{row}">{{ row.status === 'SCHEDULED' ? '已排班' : row.status }}</template></el-table-column>
      <el-table-column label="操作" width="160"><template #default="{row}"><el-button size="small" @click="openEdit(row)">编辑</el-button><el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-pagination :total="total" :page-size="query.pageSize" v-model:current-page="query.page" @current-change="fetchData" layout="total, prev, pager, next" style="margin-top:10px;justify-content:flex-end"/>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="课程名称"><el-input v-model="form.courseName"/></el-form-item>
        <el-form-item label="教练姓名"><el-input v-model="form.coachName"/></el-form-item>
        <el-form-item label="日期"><el-date-picker v-model="form.courseDate" type="date" value-format="YYYY-MM-DD" style="width:100%"/></el-form-item>
        <el-form-item label="开始时间"><el-time-picker v-model="form.startTime" value-format="HH:mm:ss" style="width:100%"/></el-form-item>
        <el-form-item label="结束时间"><el-time-picker v-model="form.endTime" value-format="HH:mm:ss" style="width:100%"/></el-form-item>
        <el-form-item label="人数上限"><el-input-number v-model="form.maxCapacity" :min="1" :max="100"/></el-form-item>
        <el-form-item label="地点"><el-input v-model="form.location"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button></template>
    </el-dialog>
  </div>
</template>
