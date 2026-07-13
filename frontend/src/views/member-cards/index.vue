<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({ memberId: '', memberName: '', cardType: '次卡', totalTimes: 30, expireDate: '' })

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/member-cards/list')
    if (res.code === 1) { tableData.value = res.data; total.value = res.data.length }
    else ElMessage.error(res.msg || '加载失败')
  } catch { ElMessage.error('无法连接服务') }
  finally { loading.value = false }
}

const openAdd = () => { Object.assign(form, { memberId: '', memberName: '', cardType: '次卡', totalTimes: 30, expireDate: '' }); dialogVisible.value = true }

const doAdd = async () => {
  if (!form.memberId || !form.memberName) { ElMessage.warning('请填写完整'); return }
  try {
    const res = await request.post('/member-cards', { ...form, remainingTimes: form.totalTimes })
    if (res.code === 1) { ElMessage.success('开卡成功'); dialogVisible.value = false; fetchData() }
    else ElMessage.error(res.msg || '开卡失败')
  } catch { ElMessage.error('开卡失败') }
}

const doDeduct = async (row) => {
  try {
    const res = await request.put('/member-cards/' + row.id + '/deduct')
    if (res.code === 1) { ElMessage.success('扣费成功'); fetchData() }
    else ElMessage.error(res.msg || '扣费失败')
  } catch { ElMessage.error('扣费失败') }
}

onMounted(fetchData)
</script>

<template>
  <div>
    <div style="margin-bottom:12px; display:flex; justify-content:space-between; align-items:center">
      <h3 style="margin:0">会员卡管理</h3>
      <el-button type="primary" @click="openAdd">开通新卡</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" border stripe>
      <el-table-column prop="memberId" label="会员ID" width="80"/>
      <el-table-column prop="memberName" label="会员姓名" width="100"/>
      <el-table-column prop="cardType" label="卡类型" width="80"/>
      <el-table-column prop="totalTimes" label="总次数" width="80"/>
      <el-table-column prop="remainingTimes" label="剩余次数" width="100">
        <template #default="{row}">
          <el-tag :type="row.remainingTimes <= 3 ? 'danger' : 'success'" effect="dark">
            {{ row.remainingTimes }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="expireDate" label="到期日期" width="110"/>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{row}">{{ row.status === 'ACTIVE' ? '正常' : row.status }}</template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{row}">
          <el-button v-if="row.remainingTimes > 0" size="small" type="warning" @click="doDeduct(row)">手动扣费</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="开通会员卡" width="400px">
      <el-form label-width="80px">
        <el-form-item label="会员ID"><el-input v-model="form.memberId"/></el-form-item>
        <el-form-item label="会员姓名"><el-input v-model="form.memberName"/></el-form-item>
        <el-form-item label="卡类型">
          <el-select v-model="form.cardType">
            <el-option value="次卡"/><el-option value="月卡"/><el-option value="季卡"/><el-option value="年卡"/>
          </el-select>
        </el-form-item>
        <el-form-item label="次数"><el-input-number v-model="form.totalTimes" :min="1" :max="365"/></el-form-item>
        <el-form-item label="到期日期">
          <el-date-picker v-model="form.expireDate" type="date" value-format="YYYY-MM-DD" style="width:100%"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doAdd">开卡</el-button>
      </template>
    </el-dialog>
  </div>
</template>
