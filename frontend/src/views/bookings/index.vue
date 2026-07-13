<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({ courseId: '', memberId: '', memberName: '' })
const query = reactive({ memberId: '' })
const total = ref(0)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/bookings', { params: query })
    if (res.code === 1) { tableData.value = res.data; total.value = res.data.length }
    else ElMessage.error(res.msg || '加载失败')
  } catch { ElMessage.error('无法连接服务') }
  finally { loading.value = false }
}

const doBook = async () => {
  if (!form.memberId || !form.memberName || !form.courseId) { ElMessage.warning('请填写完整信息'); return }
  try {
    const res = await request.post('/bookings', { courseId: form.courseId, memberId: form.memberId, memberName: form.memberName })
    if (res.code === 1) { ElMessage.success('预约成功'); dialogVisible.value = false; fetchData() }
    else ElMessage.error(res.msg || '预约失败')
  } catch (e) { ElMessage.error('预约失败: ' + (e.response?.data?.msg || '')) }
}

const doCancel = async (row) => {
  await ElMessageBox.confirm('确定取消该预约？', '提示', { type: 'warning' })
  const res = await request.put(`/bookings/${row.id}/cancel`)
  if (res.code === 1) { ElMessage.success('已取消'); fetchData() }
}

const doCheckin = async (row) => {
  await ElMessageBox.confirm('确定签到？（将扣减会员卡次数）', '签到确认', { type: 'warning' })
  try {
    const res = await request.post(`/bookings/${row.id}/checkin`, { memberId: row.memberId, memberName: row.memberName, courseId: row.courseId, courseName: row.courseName })
    if (res.code === 1) { ElMessage.success('签到成功'); fetchData() }
    else ElMessage.error(res.msg || '签到失败')
  } catch (e) { ElMessage.error('签到失败') }
}

onMounted(fetchData)
</script>

<template>
  <div>
    <el-form :inline="true">
      <el-form-item label="会员ID"><el-input v-model="query.memberId" placeholder="输入会员ID查询" clearable></el-input></el-form-item>
      <el-form-item><el-button type="primary" @click="fetchData">查询</el-button><el-button @click="dialogVisible = true; Object.assign(form, { courseId: '', memberId: '', memberName: '' })">新增预约</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" v-loading="loading" border>
      <el-table-column prop="courseName" label="课程"/><el-table-column prop="coachName" label="教练"/>
      <el-table-column prop="memberName" label="会员"/><el-table-column prop="courseDateStr" label="时间"/>
      <el-table-column prop="status" label="状态"><template #default="{row}">{{ {BOOKED:'已预约',CANCELLED:'已取消',SIGNED_IN:'已签到',ABSENT:'缺席'}[row.status] || row.status }}</template></el-table-column>
      <el-table-column label="操作" width="240"><template #default="{row}">
        <el-button v-if="row.status==='BOOKED'" size="small" type="success" @click="doCheckin(row)">签到扣费</el-button>
        <el-button v-if="row.status==='BOOKED'" size="small" type="danger" @click="doCancel(row)">取消</el-button>
      </template></el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="新增课程预约" width="400px">
      <el-form label-width="80px">
        <el-form-item label="课程ID"><el-input v-model="form.courseId" placeholder="输入课程编号"/></el-form-item>
        <el-form-item label="会员ID"><el-input v-model="form.memberId" placeholder="输入会员编号"/></el-form-item>
        <el-form-item label="会员姓名"><el-input v-model="form.memberName"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="doBook">确认预约</el-button></template>
    </el-dialog>
  </div>
</template>
