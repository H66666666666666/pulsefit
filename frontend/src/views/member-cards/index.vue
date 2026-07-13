<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const query = reactive({ memberId: '' })
const card = ref(null)
const dialogVisible = ref(false)
const form = reactive({ memberId: '', memberName: '', cardType: '次卡', totalTimes: 30, expireDate: '' })

const fetchCard = async () => {
  if (!query.memberId) { ElMessage.warning('请输入会员ID'); return }
  try {
    const res = await request.get('/member-cards', { params: { memberId: query.memberId } })
    card.value = res.data
    if (!res.data) ElMessage.info('该会员暂无会员卡')
  } catch { ElMessage.error('查询失败') }
}

const doAdd = async () => {
  if (!form.memberId || !form.memberName) { ElMessage.warning('请填写完整'); return }
  try {
    const res = await request.post('/member-cards', { ...form, remainingTimes: form.totalTimes })
    if (res.code === 1) { ElMessage.success('开卡成功'); dialogVisible.value = false; query.memberId = form.memberId; fetchCard() }
    else ElMessage.error(res.msg || '开卡失败')
  } catch { ElMessage.error('开卡失败') }
}
</script>

<template>
  <div>
    <el-form :inline="true">
      <el-form-item label="会员ID"><el-input v-model="query.memberId" placeholder="输入会员ID"/></el-form-item>
      <el-form-item><el-button type="primary" @click="fetchCard">查询会员卡</el-button><el-button @click="dialogVisible = true; form.memberId = query.memberId">开新卡</el-button></el-form-item>
    </el-form>

    <el-card v-if="card" class="member-card" :class="{ 'low-times': card.remainingTimes <= 3 }">
      <template #header><span style="font-weight:bold;font-size:16px">会员卡信息</span></template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="会员">{{ card.memberName }}</el-descriptions-item>
        <el-descriptions-item label="卡类型">{{ card.cardType }}</el-descriptions-item>
        <el-descriptions-item label="总次数">{{ card.totalTimes }}</el-descriptions-item>
        <el-descriptions-item label="剩余次数"><span :style="{color: card.remainingTimes <= 3 ? 'red' : 'green', fontWeight: 'bold'}">{{ card.remainingTimes }}</span></el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ card.expireDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ card.status === 'ACTIVE' ? '正常' : card.status }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-dialog v-model="dialogVisible" title="开通会员卡" width="400px">
      <el-form label-width="80px">
        <el-form-item label="会员ID"><el-input v-model="form.memberId"/></el-form-item>
        <el-form-item label="会员姓名"><el-input v-model="form.memberName"/></el-form-item>
        <el-form-item label="卡类型"><el-select v-model="form.cardType"><el-option value="次卡"/><el-option value="月卡"/><el-option value="季卡"/><el-option value="年卡"/></el-select></el-form-item>
        <el-form-item label="次数"><el-input-number v-model="form.totalTimes" :min="1" :max="365"/></el-form-item>
        <el-form-item label="到期日期"><el-date-picker v-model="form.expireDate" type="date" value-format="YYYY-MM-DD" style="width:100%"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="doAdd">开卡</el-button></template>
    </el-dialog>
  </div>
</template>

<style scoped>
.member-card { max-width: 600px; margin-top: 10px; }
.low-times { border: 2px solid #f56c6c; }
</style>
