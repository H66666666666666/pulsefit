<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const repairList = ref([])
const total = ref(0)
const query = reactive({ keyword: '', page: 1, pageSize: 10 })

const eqDialog = ref(false); const eqForm = reactive({ id: null, equipmentNo: '', equipmentName: '', category: '', location: '', purchaseDate: '' })
const rptDialog = ref(false); const rptForm = reactive({ equipmentId: '', equipmentNo: '', equipmentName: '', reporterName: '', faultDesc: '' })
const repairDialog = ref(false); const currentRepairs = ref([])
const auditDialog = ref(false); const auditForm = reactive({ id: '', auditorName: '' }); const auditRadio = ref('')
const finishDialog = ref(false); const finishForm = reactive({ id: '', repairDesc: '', repairCost: 0 })

const fetchData = async () => {
  loading.value = true
  try { const res = await request.get('/equipment', { params: query }); tableData.value = res.data.rows; total.value = res.data.total } catch { }
  finally { loading.value = false }
}
const openEqAdd = () => { Object.keys(eqForm).forEach(k => eqForm[k] = null); eqDialog.value = true }
const handleEqSubmit = async () => {
  try {
    const res = eqForm.id ? await request.put('/equipment', eqForm) : await request.post('/equipment', eqForm)
    if (res.code === 1) { ElMessage.success('保存成功'); eqDialog.value = false; fetchData() }
  } catch { ElMessage.error('操作失败') }
}
const openReport = (row) => { Object.assign(rptForm, { equipmentId: row.id, equipmentNo: row.equipmentNo, equipmentName: row.equipmentName, reporterName: '' }); rptDialog.value = true }
const doReport = async () => {
  try { const res = await request.post('/equipment/repairs', rptForm); if (res.code === 1) { ElMessage.success('报修已提交'); rptDialog.value = false; fetchData() } } catch { }
}
const viewRepairs = async (row) => {
  const res = await request.get('/equipment/repairs', { params: { equipmentId: row.id } })
  currentRepairs.value = res.data || []; repairDialog.value = true
}
const doAudit = (row) => {
  Object.assign(auditForm, { id: row.id, auditorName: '' })
  auditRadio.value = ''
  // 先关掉报修弹窗，避免嵌套弹窗遮罩冲突
  repairDialog.value = false
  auditDialog.value = true
}
const submitAudit = async () => {
  if (!auditRadio.value) { ElMessage.warning('请选择通过或驳回'); return }
  await request.put(`/equipment/repairs/${auditForm.id}/audit`, { auditStatus: auditRadio.value, auditorName: auditForm.auditorName })
  ElMessage.success('审核完成'); auditDialog.value = false
  // 重新打开报修弹窗看最新状态
  const eqId = currentRepairs.value.find(r => r.id === auditForm.id)?.equipmentId
  if (eqId) viewRepairs({ id: eqId })
  fetchData()
}
const doStartRepair = async (row) => {
  await request.put(`/equipment/repairs/${row.id}/start`)
  ElMessage.success('开始维修')
  const eqId = currentRepairs.value.find(r => r.id === row.id)?.equipmentId
  if (eqId) viewRepairs({ id: eqId })
  fetchData()
}
const doFinishRepair = (row) => {
  finishForm.id = row.id; finishForm.repairDesc = ''; finishForm.repairCost = 0
  repairDialog.value = false
  finishDialog.value = true
}
const submitFinish = async () => {
  await request.put(`/equipment/repairs/${finishForm.id}/finish`, { repairDesc: finishForm.repairDesc, repairCost: finishForm.repairCost })
  ElMessage.success('维修完成'); finishDialog.value = false
  const eqId = currentRepairs.value.find(r => r.id === finishForm.id)?.equipmentId
  if (eqId) viewRepairs({ id: eqId })
  fetchData()
}

onMounted(fetchData)
</script>

<template>
  <div>
    <el-form :inline="true" @submit.prevent="fetchData">
      <el-form-item><el-input v-model="query.keyword" placeholder="名称/编号" clearable/></el-form-item>
      <el-form-item><el-button type="primary" @click="fetchData">查询</el-button><el-button @click="openEqAdd">新增加器材</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" v-loading="loading" border>
      <el-table-column prop="equipmentNo" label="编号"/><el-table-column prop="equipmentName" label="名称"/><el-table-column prop="category" label="分类"/>
      <el-table-column prop="status" label="状态"><template #default="{row}">{{ {NORMAL:'正常',REPORTED:'已报修',AUDITING:'审核中',REPAIRING:'维修中',FIXED:'已修复',SCRAPPED:'已报废'}[row.status] }}</template></el-table-column>
      <el-table-column label="操作" width="300"><template #default="{row}">
        <el-button size="small" @click="openReport(row)" v-if="row.status === 'NORMAL'">报修</el-button>
        <el-button size="small" type="info" @click="viewRepairs(row)">查看报修</el-button>
      </template></el-table-column>
    </el-table>
    <el-pagination :total="total" :page-size="query.pageSize" v-model:current-page="query.page" @current-change="fetchData" layout="total, prev, pager, next" style="margin-top:10px;justify-content:flex-end"/>

    <!-- Equipment add/edit dialog -->
    <el-dialog v-model="eqDialog" title="器材信息" width="400px">
      <el-form label-width="80px">
        <el-form-item label="编号"><el-input v-model="eqForm.equipmentNo"/></el-form-item>
        <el-form-item label="名称"><el-input v-model="eqForm.equipmentName"/></el-form-item>
        <el-form-item label="分类"><el-input v-model="eqForm.category"/></el-form-item>
        <el-form-item label="位置"><el-input v-model="eqForm.location"/></el-form-item>
        <el-form-item label="购买日期"><el-date-picker v-model="eqForm.purchaseDate" type="date" value-format="YYYY-MM-DD" style="width:100%"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="eqDialog = false">取消</el-button>
        <el-button type="primary" @click="handleEqSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- Report dialog -->
    <el-dialog v-model="rptDialog" title="器材报修" width="400px">
      <el-form label-width="80px">
        <el-form-item label="器材">{{ rptForm.equipmentName }} ({{ rptForm.equipmentNo }})</el-form-item>
        <el-form-item label="报修人"><el-input v-model="rptForm.reporterName"/></el-form-item>
        <el-form-item label="故障描述"><el-input v-model="rptForm.faultDesc" type="textarea"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rptDialog = false">取消</el-button>
        <el-button type="danger" @click="doReport">提交报修</el-button>
      </template>
    </el-dialog>

    <!-- Repair list dialog -->
    <el-dialog v-model="repairDialog" title="报修记录" width="700px">
      <el-table :data="currentRepairs" border>
        <el-table-column prop="reporterName" label="报修人"/><el-table-column prop="faultDesc" label="故障"/>
        <el-table-column prop="status" label="状态"><template #default="{row}">{{ {REPORTED:'待审核',AUDITING:'审核通过待维修',REPAIRING:'维修中',FIXED:'已修复'}[row.status] || row.status }}</template></el-table-column>
        <el-table-column label="操作" width="200"><template #default="{row}">
          <el-button v-if="row.status === 'REPORTED'" size="small" type="warning" @click="doAudit(row)">审核</el-button>
          <el-button v-if="row.status === 'AUDITING'" size="small" type="primary" @click="doStartRepair(row)">开始维修</el-button>
          <el-button v-if="row.status === 'REPAIRING'" size="small" type="success" @click="doFinishRepair(row)">维修完成</el-button>
        </template></el-table-column>
      </el-table>
    </el-dialog>

    <!-- Audit dialog -->
    <el-dialog v-model="auditDialog" title="审核报修" width="380px" append-to-body :close-on-click-modal="false">
      <el-form label-width="80px">
        <el-form-item label="审核结果">
          <el-button :type="auditRadio === 'APPROVED' ? 'primary' : ''" @click="auditRadio = 'APPROVED'" style="margin-right:12px">通过</el-button>
          <el-button :type="auditRadio === 'REJECTED' ? 'danger' : ''" @click="auditRadio = 'REJECTED'">驳回</el-button>
        </el-form-item>
        <el-form-item label="审核人"><el-input v-model="auditForm.auditorName"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialog = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确认</el-button>
      </template>
    </el-dialog>

    <!-- Finish repair dialog -->
    <el-dialog v-model="finishDialog" title="维修完成" width="350px" append-to-body :close-on-click-modal="false">
      <el-form label-width="80px">
        <el-form-item label="维修描述"><el-input v-model="finishForm.repairDesc"/></el-form-item>
        <el-form-item label="费用"><el-input-number v-model="finishForm.repairCost" :min="0"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="finishDialog = false">取消</el-button>
        <el-button type="primary" @click="submitFinish">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>
