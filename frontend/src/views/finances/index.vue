<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const summary = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const query = reactive({ type: '', startDate: '', endDate: '', page: 1, pageSize: 10 })
const form = reactive({ recordType: 'INCOME', category: 'MEMBERSHIP_FEE', amount: 0, description: '', recordDate: '' })

const fetchData = async () => {
  loading.value = true
  try { const res = await request.get('/finances', { params: query }); tableData.value = res.data.rows; total.value = res.data.total } catch { }
  finally { loading.value = false }
}
const fetchSummary = async () => {
  const res = await request.get('/finances/summary', { params: { startDate: query.startDate, endDate: query.endDate } })
  summary.value = res.data || []
}
const doAdd = async () => {
  if (!form.amount || !form.recordDate) { ElMessage.warning('请填写金额和日期'); return }
  try { const res = await request.post('/finances', form); if (res.code === 1) { ElMessage.success('记录成功'); dialogVisible.value = false; fetchData(); fetchSummary() } } catch { }
}

onMounted(() => { fetchData(); fetchSummary() })
</script>

<template>
  <div>
    <el-form :inline="true">
      <el-form-item><el-select v-model="query.type" placeholder="类型" clearable><el-option label="收入" value="INCOME"/><el-option label="支出" value="EXPENSE"/></el-select></el-form-item>
      <el-form-item><el-date-picker v-model="query.startDate" type="date" value-format="YYYY-MM-DD" placeholder="开始日期"/></el-form-item>
      <el-form-item><el-date-picker v-model="query.endDate" type="date" value-format="YYYY-MM-DD" placeholder="结束日期"/></el-form-item>
      <el-form-item><el-button type="primary" @click="fetchData(); fetchSummary()">查询</el-button><el-button @click="dialogVisible = true">新增记录</el-button></el-form-item>
    </el-form>

    <el-row :gutter="20" style="margin-bottom:16px">
      <el-col :span="6" v-for="s in summary" :key="s.name">
        <el-statistic :title="s.name === 'INCOME' ? '总收入' : '总支出'" :value="s.value" prefix="¥"/>
      </el-col>
    </el-row>

    <el-table :data="tableData" v-loading="loading" border>
      <el-table-column prop="recordDate" label="日期"/><el-table-column prop="recordType" label="类型"><template #default="{row}">{{ row.recordType==='INCOME'?'收入':'支出' }}</template></el-table-column>
      <el-table-column prop="category" label="分类"/><el-table-column prop="amount" label="金额"/><el-table-column prop="description" label="描述"/>
    </el-table>
    <el-pagination :total="total" :page-size="query.pageSize" v-model:current-page="query.page" @current-change="fetchData" layout="total, prev, pager, next" style="margin-top:10px;justify-content:flex-end"/>

    <el-dialog v-model="dialogVisible" title="新增财务记录" width="400px"><el-form label-width="80px">
      <el-form-item label="类型"><el-radio-group v-model="form.recordType"><el-radio value="INCOME">收入</el-radio><el-radio value="EXPENSE">支出</el-radio></el-radio-group></el-form-item>
      <el-form-item label="分类"><el-select v-model="form.category"><el-option label="会员费" value="MEMBERSHIP_FEE"/><el-option label="课程费" value="COURSE_FEE"/><el-option label="器材维修" value="EQUIPMENT_REPAIR"/><el-option label="器材采购" value="EQUIPMENT_PURCHASE"/><el-option label="其他" value="OTHER"/></el-select></el-form-item>
      <el-form-item label="金额"><el-input-number v-model="form.amount" :min="0" :precision="2" style="width:100%"/></el-form-item>
      <el-form-item label="描述"><el-input v-model="form.description"/></el-form-item>
      <el-form-item label="日期"><el-date-picker v-model="form.recordDate" type="date" value-format="YYYY-MM-DD" style="width:100%"/></el-form-item>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="doAdd">保存</el-button></template>
    </el-form></el-dialog>
  </div>
</template>
