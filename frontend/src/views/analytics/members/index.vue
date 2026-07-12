<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const error = ref('')
const campData = ref(null)
const membershipLevelData = ref([])
const degreeColors = ['#759f00', '#98b947', '#b4ca7c', '#d1dcae', '#171a17', '#5f655d']

const totalMembers = computed(() => {
  if (!campData.value) return 0
  return (campData.value.dataList || []).reduce((s, v) => s + (Number(v) || 0), 0)
})

const activeCamps = computed(() => (campData.value?.campList || []).length)

const maxCount = computed(() => {
  if (!campData.value) return 1
  return Math.max(1, ...(campData.value.dataList || []).map(v => Number(v) || 0))
})

const maxDegree = computed(() => {
  return Math.max(1, ...membershipLevelData.value.map(v => Number(v.value) || 0))
})

const fetchData = async () => {
  loading.value = true; error.value = ''
  try {
    const [campRes, levelRes] = await Promise.all([
      request.get('/analytics/camp-members'),
      request.get('/analytics/membership-levels'),
    ])
    if (campRes.code === 1) {
      campData.value = campRes.data
    }
    else error.value = '训练营分布数据加载失败'
    if (levelRes.code === 1) membershipLevelData.value = levelRes.data || []
    else error.value = error.value || '会员等级分布数据加载失败'
  } catch { error.value = '无法连接运营服务' }
  finally { loading.value = false }
}

onMounted(() => fetchData())
</script>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div><span class="section-label">MEMBER INSIGHTS</span><h1>会员洞察</h1><p>按训练营与会员等级维度分析会员群体画像，辅助运营决策</p></div>
      <el-button @click="fetchData()"><el-icon><Refresh /></el-icon> 刷新</el-button>
    </div>

    <div v-loading="loading" class="report-layout">
      <div v-if="error" class="surface error-card"><el-icon :size="28"><WarningFilled /></el-icon><p>{{ error }}</p></div>

      <template v-else>
        <div class="stat-row">
          <article class="surface stat-card"><span>注册会员</span><strong>{{ totalMembers }}</strong><small>人</small></article>
          <article class="surface stat-card"><span>活跃训练营</span><strong>{{ activeCamps }}</strong><small>个</small></article>
          <article class="surface stat-card"><span>会员等级</span><strong>{{ membershipLevelData.length }}</strong><small>维度</small></article>
        </div>

        <section class="surface chart-block">
          <h3>训练营会员分布</h3>
          <div v-if="campData && campData.campList?.length" class="bar-chart">
            <div v-for="(label, i) in campData.campList" :key="label" class="bar-row">
              <span class="bar-label" :title="label">{{ label }}</span>
              <span class="bar-track"><i class="bar-fill" :style="{ width: ((Number(campData.dataList[i]) || 0) / maxCount * 100) + '%' }"></i></span>
              <span class="bar-value">{{ campData.dataList[i] || 0 }}</span>
            </div>
          </div>
          <div v-else class="chart-empty"><p>暂无训练营分布数据</p></div>
        </section>

        <section class="surface chart-block">
          <h3>会员等级分布</h3>
          <div v-if="membershipLevelData.length" class="bar-chart">
            <div v-for="(item, i) in membershipLevelData" :key="item.name" class="bar-row">
              <span class="bar-label">{{ item.name }}</span>
              <span class="bar-track"><i class="bar-fill degree" :style="{ width: ((Number(item.value) || 0) / maxDegree * 100) + '%', background: degreeColors[i % degreeColors.length] }"></i></span>
              <span class="bar-value">{{ item.value || 0 }}</span>
            </div>
          </div>
          <div v-else class="chart-empty"><p>暂无会员等级分布数据</p></div>
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
.bar-label { font-size: 13px; color: var(--pf-ink); text-align: right; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.bar-track { height: 28px; background: #eef0ea; border-radius: 3px; overflow: hidden; }
.bar-fill { display: block; height: 100%; background: #759f00; border-radius: 3px; transition: width .6s ease; min-width: 4px; }
.bar-value { font-family: 'Barlow Condensed', sans-serif; font-size: 22px; font-weight: 700; color: var(--pf-ink); }
.chart-empty { padding: 36px; text-align: center; color: var(--pf-ink-soft); }
@media (max-width: 700px) { .stat-row { grid-template-columns: 1fr 1fr; } .bar-row { grid-template-columns: 80px 1fr 40px; gap: 10px; } }
</style>
