<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pagination = reactive({ page: 1, pageSize: 10 })

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/operation-logs', { params: { page: pagination.page, pageSize: pagination.pageSize } })
    if (res.code === 1) { tableData.value = res.data.rows; total.value = res.data.total }
    else ElMessage.error(res.msg || '加载日志失败')
  } catch { ElMessage.error('无法连接运营服务') }
  finally { loading.value = false }
}

const handleSizeChange = (v) => { pagination.pageSize = v; fetchData() }
const handlePageChange = (v) => { pagination.page = v; fetchData() }
const formatTime = (v) => v ? v.replace('T', ' ').substring(0, 19) : '—'

onMounted(() => fetchData())
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><span class="section-label">AUDIT LOG</span><h1>操作审计</h1><p>记录每一次运营操作的来源、方法与耗时，支持安全审计与合规追溯</p></div>
      <el-button @click="fetchData()"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>

    <section class="surface">
      <el-table v-loading="loading" :data="tableData" stripe style="width:100%">
        <template #empty><div class="empty-state"><el-icon :size="40"><Document /></el-icon><p>暂无操作记录</p><small>系统操作将自动记录在此处</small></div></template>
        <el-table-column type="index" label="编号" width="70" align="center" />
        <el-table-column prop="operatorName" label="操作人" width="120" align="center" />
        <el-table-column label="操作时间" width="175" align="center">
          <template #default="{ row }">{{ formatTime(row.operationTime) }}</template>
        </el-table-column>
        <el-table-column prop="className" label="操作模块" min-width="180" show-overflow-tooltip />
        <el-table-column prop="methodName" label="操作方法" min-width="180" show-overflow-tooltip />
        <el-table-column label="耗时" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.costTime > 500 ? 'warning' : 'success'" size="small">{{ row.costTime }} ms</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作参数" min-width="200" show-overflow-tooltip>
          <template #default="{ row }"><code class="log-param">{{ row.methodParams || '—' }}</code></template>
        </el-table-column>
        <el-table-column label="返回值" width="120" show-overflow-tooltip>
          <template #default="{ row }"><code class="log-param">{{ (row.returnValue || '—').substring(0, 60) }}</code></template>
        </el-table-column>
      </el-table>
      <div class="table-foot"><span v-if="total">共 {{ total }} 条审计记录</span><el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.pageSize" :total="total" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" /></div>
    </section>
  </div>
</template>

<style scoped>
.table-foot { display: flex; align-items: center; justify-content: space-between; padding: 14px 18px; color: var(--pf-ink-soft); font-size: 13px; }
.empty-state { padding: 48px 20px; text-align: center; color: var(--pf-ink-soft); }
.empty-state p { margin: 12px 0 4px; font-weight: 600; }
.empty-state small { font-size: 12px; color: #93988f; }
.log-param { padding: 2px 6px; background: #f0f1ed; border-radius: 3px; font-size: 11px; color: var(--pf-ink-soft); word-break: break-all; }
</style>
