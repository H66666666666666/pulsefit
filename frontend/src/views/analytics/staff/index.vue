<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const error = ref('')
const roleData = ref(null)
const genderData = ref([])

const totalStaff = computed(() => {
  if (!roleData.value) return 0
  const list = roleData.value.roleList || []
  const data = roleData.value.dataList || []
  return data.reduce((s, v) => s + (Number(v) || 0), 0)
})

const maxRoleCount = computed(() => {
  if (!roleData.value) return 1
  return Math.max(1, ...(roleData.value.dataList || []).map(v => Number(v) || 0))
})

const maxGenderCount = computed(() => {
  return Math.max(1, ...genderData.value.map(v => Number(v.value) || 0))
})

const fetchData = async () => {
  loading.value = true; error.value = ''
  try {
    const [roleRes, genderRes] = await Promise.all([
      request.get('/analytics/staff-roles'),
      request.get('/analytics/staff-gender'),
    ])
    if (roleRes.code === 1) roleData.value = roleRes.data
    else error.value = '岗位数据加载失败'
    if (genderRes.code === 1) genderData.value = genderRes.data || []
    else error.value = error.value || '性别数据加载失败'
  } catch { error.value = '无法连接运营服务' }
  finally { loading.value = false }
}

onMounted(() => fetchData())
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><span class="section-label">TEAM STRUCTURE</span><h1>团队结构</h1><p>按岗位与性别维度查看教练和员工的全景分布</p></div>
      <el-button @click="fetchData()"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>

    <div v-loading="loading" class="report-layout">
      <div v-if="error" class="surface error-card"><el-icon :size="28"><WarningFilled /></el-icon><p>{{ error }}</p></div>

      <template v-else>
        <div class="stat-row">
          <article class="surface stat-card"><span>在岗员工</span><strong>{{ totalStaff }}</strong><small>人</small></article>
          <article class="surface stat-card"><span>岗位类别</span><strong>{{ (roleData?.roleList || []).length }}</strong><small>类</small></article>
          <article class="surface stat-card"><span>性别分布</span><strong>{{ genderData.length }}</strong><small>维度</small></article>
        </div>

        <section class="surface chart-block">
          <h3>岗位人数分布</h3>
          <div v-if="roleData && roleData.roleList?.length" class="bar-chart">
            <div v-for="(label, i) in roleData.roleList" :key="label" class="bar-row">
              <span class="bar-label">{{ label }}</span>
              <span class="bar-track"><i class="bar-fill" :style="{ width: ((Number(roleData.dataList[i]) || 0) / maxRoleCount * 100) + '%' }"></i></span>
              <span class="bar-value">{{ roleData.dataList[i] || 0 }}</span>
            </div>
          </div>
          <div v-else class="chart-empty"><p>暂无岗位分布数据</p></div>
        </section>

        <section class="surface chart-block">
          <h3>性别分布</h3>
          <div v-if="genderData.length" class="bar-chart">
            <div v-for="item in genderData" :key="item.name" class="bar-row">
              <span class="bar-label">{{ item.name }}</span>
              <span class="bar-track"><i class="bar-fill gender" :style="{ width: ((Number(item.value) || 0) / maxGenderCount * 100) + '%' }"></i></span>
              <span class="bar-value">{{ item.value || 0 }}</span>
            </div>
          </div>
          <div v-else class="chart-empty"><p>暂无性别分布数据</p></div>
        </section>
      </template>
    </div>
  </div>
</template>

<style scoped>
.report-layout { display: flex; flex-direction: column; gap: 16px; }
.error-card { display: flex; align-items: center; gap: 12px; padding: 32px; color: var(--pf-danger); font-size: 14px; }
.stat-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 14px; }
.stat-card { padding: 26px 28px; }
.stat-card span { display: block; color: var(--pf-ink-soft); font-size: 12px; font-weight: 600; letter-spacing: 0.5px; }
.stat-card strong { display: block; margin-top: 10px; font-family: 'Barlow Condensed', sans-serif; font-size: 52px; line-height: 1; }
.stat-card small { display: block; margin-top: 4px; color: #93988f; font-size: 12px; }
.chart-block { padding: 26px 28px; }
.chart-block h3 { margin: 0 0 22px; font-size: 17px; }
.bar-chart { display: flex; flex-direction: column; gap: 18px; }
.bar-row { display: grid; grid-template-columns: 100px 1fr 50px; align-items: center; gap: 16px; }
.bar-label { font-size: 13px; color: var(--pf-ink); text-align: right; }
.bar-track { height: 28px; background: #eef0ea; border-radius: 3px; overflow: hidden; }
.bar-fill { display: block; height: 100%; background: #759f00; border-radius: 3px; transition: width .6s ease; min-width: 4px; }
.bar-fill.gender { background: var(--pf-ink); }
.bar-value { font-family: 'Barlow Condensed', sans-serif; font-size: 22px; font-weight: 700; color: var(--pf-ink); }
.chart-empty { padding: 36px; text-align: center; color: var(--pf-ink-soft); }
@media (max-width: 700px) { .stat-row { grid-template-columns: 1fr 1fr; } .bar-row { grid-template-columns: 80px 1fr 40px; gap: 10px; } }
</style>
