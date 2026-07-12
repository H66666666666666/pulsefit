<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const serviceAlertVisible = ref(false)
const dialogTitle = ref('新增会员')
const formRef = ref()
const submitting = ref(false)
const campOptions = ref([])
const serviceAlertTarget = ref(null)
const serviceAlertPoints = ref(0)

const searchForm = reactive({ name: '', trainingCampId: '', membershipLevel: '' })
const pagination = reactive({ page: 1, pageSize: 10 })

const form = reactive({
  id: null, name: '', memberNo: '', gender: null, phone: '', idCard: '',
  referralChannel: null, address: '', membershipLevel: null, membershipDate: '', trainingCampId: null,
})

const rules = {
  name: [{ required: true, message: '请输入会员姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
}

const genderMap = { 1: '男', 2: '女' }
const membershipLevelOptions = ['基础会员', '银卡会员', '黄金会员', '铂金会员', '钻石会员', '黑金会员']

const fetchData = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, pageSize: pagination.pageSize, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] == null) delete params[k] })
    const res = await request.get('/members', { params })
    if (res.code === 1) { tableData.value = res.data.rows; total.value = res.data.total }
    else ElMessage.error(res.msg || '加载会员数据失败')
  } catch { ElMessage.error('无法连接运营服务') }
  finally { loading.value = false }
}

const fetchCampOptions = async () => {
  try { const res = await request.get('/training-camps/list'); if (res.code === 1) campOptions.value = res.data || [] } catch { /* ignore */ }
}

const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, { id: null, name: '', memberNo: '', gender: null, phone: '', idCard: '', referralChannel: null, address: '', membershipLevel: null, membershipDate: '', trainingCampId: null })
}

const openAdd = () => { resetForm(); dialogTitle.value = '新增会员'; dialogVisible.value = true }

const openEdit = async (row) => {
  try {
    const res = await request.get(`/members/${row.id}`)
    if (res.code === 1) {
      const d = res.data
      Object.assign(form, {
        id: d.id, name: d.name, memberNo: d.memberNo, gender: d.gender,
        phone: d.phone, idCard: d.idCard, referralChannel: d.referralChannel,
        address: d.address, membershipLevel: d.membershipLevel, membershipDate: d.membershipDate, trainingCampId: d.trainingCampId,
      })
      dialogTitle.value = '编辑会员信息'
      dialogVisible.value = true
    }
  } catch { ElMessage.error('获取会员信息失败') }
}

const handleSubmit = async () => {
  if (!await formRef.value.validate().catch(() => false)) return
  submitting.value = true
  try {
    const res = form.id ? await request.put('/members', { ...form }) : await request.post('/members', { ...form })
    if (res.code === 1) {
      ElMessage.success(form.id ? '会员信息已更新' : '会员已创建')
      dialogVisible.value = false
      fetchData()
    } else ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('请求失败') }
  finally { submitting.value = false }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除会员「${row.name}」？`, '确认删除', { type: 'warning', confirmButtonText: '确认删除', cancelButtonText: '取消' })
    const res = await request.delete('/members', { params: { ids: [row.id] } })
    if (res.code === 1) { ElMessage.success('已删除'); fetchData() }
    else ElMessage.error(res.msg || '删除失败')
  } catch { /* cancelled */ }
}

const openServiceAlert = (row) => { serviceAlertTarget.value = row; serviceAlertPoints.value = 0; serviceAlertVisible.value = true }

const handleServiceAlert = async () => {
  if (!serviceAlertPoints.value || serviceAlertPoints.value <= 0) return ElMessage.warning('请输入有效的风险积分值')
  submitting.value = true
  try {
    const res = await request.put(`/members/service-alert/${serviceAlertTarget.value.id}/${serviceAlertPoints.value}`)
    if (res.code === 1) {
      ElMessage.success(`已对 ${serviceAlertTarget.value.name} 调整服务提醒，+${serviceAlertPoints.value} 风险积分`)
      serviceAlertVisible.value = false
      fetchData()
    } else ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('请求失败') }
  finally { submitting.value = false }
}

const handleSizeChange = (v) => { pagination.pageSize = v; fetchData() }
const handlePageChange = (v) => { pagination.page = v; fetchData() }

onMounted(() => { fetchData(); fetchCampOptions() })
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><span class="section-label">MEMBERS</span><h1>会员管理</h1><p>管理所有门店注册会员档案、训练记录与服务提醒</p></div>
      <el-button type="primary" @click="openAdd"><el-icon><Plus /></el-icon> 新增会员</el-button>
    </div>

    <section class="surface search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="姓名"><el-input v-model="searchForm.name" placeholder="会员姓名" clearable style="width:160px" /></el-form-item>
        <el-form-item label="所属训练营"><el-select v-model="searchForm.trainingCampId" placeholder="全部" clearable style="width:170px"><el-option v-for="c in campOptions" :key="c.id" :label="c.name" :value="c.id" /></el-select></el-form-item>
        <el-form-item label="会员等级"><el-select v-model="searchForm.membershipLevel" placeholder="全部" clearable style="width:130px"><el-option v-for="lvl in membershipLevelOptions" :key="lvl" :label="lvl" :value="lvl" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="pagination.page=1;fetchData()"><el-icon><Search /></el-icon> 搜索</el-button><el-button @click="Object.assign(searchForm,{name:'',trainingCampId:'',membershipLevel:''});pagination.page=1;fetchData()">重置</el-button></el-form-item>
      </el-form>
    </section>

    <section class="surface">
      <el-table v-loading="loading" :data="tableData" stripe style="width:100%">
        <template #empty><div class="empty-state"><el-icon :size="40"><User /></el-icon><p>暂无会员数据</p><small>点击「新增会员」录入第一位会员</small></div></template>
        <el-table-column type="index" label="编号" width="70" align="center" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column label="性别" width="70" align="center">
          <template #default="{ row }">{{ genderMap[row.gender] || '—' }}</template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="125" align="center" />
        <el-table-column prop="trainingCampName" label="所属训练营" min-width="130" show-overflow-tooltip />
        <el-table-column prop="membershipLevel" label="会员等级" min-width="90" align="center" />
        <el-table-column label="服务提醒" width="90" align="center">
          <template #default="{ row }"><span :class="{ 'danger-num': row.serviceAlertCount > 0 }">{{ row.serviceAlertCount ?? 0 }}</span></template>
        </el-table-column>
        <el-table-column label="风险积分" width="90" align="center">
          <template #default="{ row }"><span :class="{ 'danger-num': row.riskPoints > 0 }">{{ row.riskPoints ?? 0 }}</span></template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openEdit(row)"><el-icon><Edit /></el-icon> 编辑</el-button>
            <el-button type="warning" link size="small" @click="openServiceAlert(row)"><el-icon><WarningFilled /></el-icon> 提醒</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)"><el-icon><Delete /></el-icon> 删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-foot"><span v-if="total">共 {{ total }} 名会员</span><el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.pageSize" :total="total" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" /></div>
    </section>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" label-position="top">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="姓名" prop="name"><el-input v-model="form.name" placeholder="会员真实姓名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="编号" prop="memberNo"><el-input v-model="form.memberNo" placeholder="会员编号" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="性别" prop="gender"><el-radio-group v-model="form.gender"><el-radio :label="1">男</el-radio><el-radio :label="2">女</el-radio></el-radio-group></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" placeholder="手机号" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="身份证号" prop="idCard"><el-input v-model="form.idCard" placeholder="身份证号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="推荐渠道" prop="referralChannel"><el-radio-group v-model="form.referralChannel"><el-radio label="线上渠道">线上渠道</el-radio><el-radio label="门店渠道">门店渠道</el-radio></el-radio-group></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="会员等级" prop="membershipLevel"><el-select v-model="form.membershipLevel" placeholder="选择会员等级" clearable style="width:100%"><el-option v-for="lvl in membershipLevelOptions" :key="lvl" :label="lvl" :value="lvl" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="会籍日期" prop="membershipDate"><el-date-picker v-model="form.membershipDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="联系地址" prop="address"><el-input v-model="form.address" placeholder="联系地址" /></el-form-item>
        <el-form-item label="分配训练营" prop="trainingCampId"><el-select v-model="form.trainingCampId" placeholder="选择训练营" clearable style="width:100%"><el-option v-for="c in campOptions" :key="c.id" :label="c.name" :value="c.id" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="serviceAlertVisible" title="服务提醒处理" width="420px" :close-on-click-modal="false">
      <div style="margin-bottom:16px">对会员 <strong>{{ serviceAlertTarget?.name }}</strong> 进行服务提醒处理，当前累计提醒 <strong>{{ serviceAlertTarget?.serviceAlertCount ?? 0 }}</strong> 次，风险积分 <strong>{{ serviceAlertTarget?.riskPoints ?? 0 }}</strong> 分。</div>
      <el-form label-position="top">
        <el-form-item label="本次风险积分"><el-input-number v-model="serviceAlertPoints" :min="1" :max="100" :step="1" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="serviceAlertVisible=false">取消</el-button><el-button type="warning" :loading="submitting" @click="handleServiceAlert">确认处理</el-button></template>
    </el-dialog>
  </div>
</template>

<style scoped>
.search-bar { padding: 18px 22px 4px; margin-bottom: 14px; }
.table-foot { display: flex; align-items: center; justify-content: space-between; padding: 14px 18px; color: var(--pf-ink-soft); font-size: 13px; }
.empty-state { padding: 48px 20px; text-align: center; color: var(--pf-ink-soft); }
.empty-state p { margin: 12px 0 4px; font-weight: 600; }
.empty-state small { font-size: 12px; color: #93988f; }
.danger-num { color: var(--pf-danger); font-weight: 700; }
</style>
