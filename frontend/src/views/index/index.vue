<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()

const stats = ref([
  { label: '在册会员', value: '--', note: 'MEMBERS', icon: 'User', tone: 'lime' },
  { label: '活跃训练营', value: '--', note: 'CAMPS', icon: 'Calendar', tone: 'dark' },
  { label: '教练与员工', value: '--', note: 'TEAM', icon: 'Avatar', tone: 'light' },
  { label: '运营部门', value: '--', note: 'DEPARTMENTS', icon: 'OfficeBuilding', tone: 'light' },
])

const todayCamps = ref([])
const today = new Intl.DateTimeFormat('zh-CN', { month: 'long', day: 'numeric', weekday: 'long' }).format(new Date())

const formatDateLabel = (d) => {
  if (!d) return '--'
  const date = new Date(d.length === 10 ? d + 'T00:00:00' : d)
  const now = new Date()
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  const target = new Date(date.getFullYear(), date.getMonth(), date.getDate())
  const diff = Math.round((target - today) / 86400000)
  if (diff === 0) return '今天'
  if (diff === 1) return '明天'
  if (diff === -1) return '昨天'
  if (diff < 0) return `${Math.abs(diff)}天前`
  if (diff <= 7) return `${diff}天后`
  return `${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

const timelineLabel = (start, end) => {
  const now = new Date()
  if (now < new Date(start)) return '待开始'
  if (now > new Date(end)) return '已结束'
  return '进行中'
}

const isLive = (start, end) => {
  const now = new Date()
  return now >= new Date(start) && now <= new Date(end)
}

const openTrainingCamp = () => router.push('/training-camps')

onMounted(async () => {
  const endpoints = ['/members?page=1&pageSize=1', '/training-camps?page=1&pageSize=100', '/staff?page=1&pageSize=1', '/departments']
  const results = await Promise.allSettled(endpoints.map((url) => request.get(url)))
  const values = [
    results[0].value?.data?.total,
    results[1].value?.data?.rows?.filter((item) => item.status !== '已结束').length,
    results[2].value?.data?.total,
    results[3].value?.data?.length,
  ]
  stats.value = stats.value.map((item, index) => ({ ...item, value: values[index] ?? '--' }))

  const campsData = results[1].value?.data
  if (campsData) {
    todayCamps.value = (campsData.rows || [])
      .filter(c => c.status !== '已结束')
      .sort((a, b) => new Date(a.startDate) - new Date(b.startDate))
      .slice(0, 5)
  }
})
</script>

<template>
  <div class="page-shell dashboard">
    <div class="page-heading">
      <div><span class="section-label">DAILY PULSE</span><h1>运营总览</h1><p>{{ today }} · 查看门店组织、训练计划与会员状态</p></div>
      <el-button type="primary" @click="openTrainingCamp"><el-icon><Plus /></el-icon> 新建训练营</el-button>
    </div>

    <section class="stat-grid">
      <article v-for="item in stats" :key="item.label" class="stat" :class="item.tone">
        <div><span>{{ item.note }}</span><el-icon><component :is="item.icon" /></el-icon></div>
        <strong>{{ item.value }}</strong><p>{{ item.label }}</p>
      </article>
    </section>

    <section class="dashboard-grid">
      <article class="club-focus">
        <div class="focus-copy"><span>FOCUS / 01</span><h2>保持训练节奏，<br>也保持运营节奏。</h2><p>从会员入营到教练排班，PulseFit 让每个关键动作清晰可见。</p><el-button @click="router.push('/members')">查看会员状态 <el-icon><Right /></el-icon></el-button></div>
      </article>
      <article class="surface schedule">
        <div class="panel-head"><div><span class="section-label">TODAY</span><h3>今日训练安排</h3></div><button @click="router.push('/training-camps')">全部训练营</button></div>
        <div v-if="!todayCamps.length" class="timeline-empty">
          <el-icon :size="32"><Calendar /></el-icon>
          <p>今日暂无训练安排</p>
          <small>点击上方按钮创建第一个训练营</small>
        </div>
        <div v-else class="timeline">
          <div v-for="camp in todayCamps" :key="camp.id">
            <time>{{ formatDateLabel(camp.startDate) }}</time>
            <i :class="{ lime: isLive(camp.startDate, camp.endDate) }"></i>
            <span>
              <b>{{ camp.name }}</b>
              <small>{{ camp.venue || '—' }} · {{ camp.programType || '综合训练' }} · {{ timelineLabel(camp.startDate, camp.endDate) }}</small>
            </span>
          </div>
        </div>
      </article>
    </section>

    <section class="surface action-strip">
      <div><span class="section-label">QUICK ACTIONS</span><h3>常用操作</h3></div>
      <button @click="router.push('/members')"><el-icon><User /></el-icon><span><b>会员档案</b><small>查询与维护会员信息</small></span><el-icon><Right /></el-icon></button>
      <button @click="router.push('/staff')"><el-icon><Avatar /></el-icon><span><b>团队名册</b><small>管理教练与运营员工</small></span><el-icon><Right /></el-icon></button>
      <button @click="router.push('/analytics/members')"><el-icon><TrendCharts /></el-icon><span><b>会员洞察</b><small>查看人群与训练分布</small></span><el-icon><Right /></el-icon></button>
    </section>
  </div>
</template>

<style scoped>
.stat-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; margin-bottom: 16px; }.stat { min-height: 168px; padding: 22px; background: #fff; border: 1px solid var(--pf-line); border-radius: 6px; }.stat > div { display: flex; justify-content: space-between; color: #777d74; font-size: 10px; letter-spacing: 1.2px; }.stat .el-icon { font-size: 20px; }.stat strong { display: block; margin-top: 30px; font-family: 'Barlow Condensed', sans-serif; font-size: 58px; line-height: .8; }.stat p { margin: 13px 0 0; color: #6d736a; font-size: 13px; }.stat.lime { background: var(--pf-accent); border-color: var(--pf-accent); }.stat.lime > div,.stat.lime p { color: #3b421d; }.stat.dark { color: #fff; background: #272b27; border-color: #272b27; }.stat.dark > div,.stat.dark p { color: #aeb4ac; }
.dashboard-grid { display: grid; grid-template-columns: 1.35fr 1fr; gap: 16px; }.club-focus { position: relative; min-height: 380px; overflow: hidden; color: #fff; background: linear-gradient(90deg, rgba(13,15,13,.92), rgba(13,15,13,.18)), url('../../assets/pulsefit-gym.jpg') center/cover; border-radius: 6px; }.focus-copy { position: absolute; top: 0; bottom: 0; display: flex; flex-direction: column; justify-content: center; max-width: 520px; padding: 45px; }.focus-copy > span { color: var(--pf-accent); font-size: 10px; letter-spacing: 1.5px; }.focus-copy h2 { margin: 15px 0; font-family: 'Barlow Condensed', 'Microsoft YaHei', sans-serif; font-size: 44px; line-height: 1.02; }.focus-copy p { max-width: 400px; margin: 0 0 25px; color: #c9cec6; font-size: 14px; line-height: 1.7; }.focus-copy .el-button { align-self: flex-start; color: #171a17; background: var(--pf-accent); border-color: var(--pf-accent); }
.schedule { padding: 26px; }.panel-head { display: flex; align-items: flex-start; justify-content: space-between; }.panel-head h3,.action-strip h3 { margin: 5px 0 0; font-size: 19px; }.panel-head button { color: #62685f; background: none; border: 0; cursor: pointer; font-size: 12px; }.timeline { margin-top: 28px; }.timeline > div { display: grid; grid-template-columns: 52px 15px 1fr; gap: 12px; min-height: 82px; }.timeline time { color: #737970; font-size: 11px; }.timeline i { position: relative; display: block; width: 9px; height: 9px; background: #c9cdc5; border: 2px solid white; border-radius: 50%; box-shadow: 0 0 0 1px #c9cdc5; }.timeline i::after { position: absolute; top: 12px; left: 3px; width: 1px; height: 58px; content: ''; background: #dfe2da; }.timeline > div:last-child i::after { display: none; }.timeline i.lime { background: var(--pf-accent-strong); box-shadow: 0 0 0 1px var(--pf-accent-strong); }.timeline b,.timeline small { display: block; }.timeline b { font-size: 13px; }.timeline small { margin-top: 7px; color: #858b82; font-size: 11px; }
.timeline-empty { padding: 40px 20px; text-align: center; color: #858b82; }
.timeline-empty p { margin: 12px 0 4px; font-weight: 600; color: #737970; }
.timeline-empty small { font-size: 12px; }
.action-strip { display: grid; grid-template-columns: .8fr repeat(3, 1fr); gap: 0; margin-top: 16px; padding: 18px 24px; }.action-strip > div { padding: 10px 18px 10px 0; }.action-strip button { display: grid; grid-template-columns: 30px 1fr 20px; align-items: center; gap: 9px; padding: 10px 20px; color: var(--pf-ink); background: none; border: 0; border-left: 1px solid var(--pf-line); cursor: pointer; text-align: left; }.action-strip button > .el-icon:first-child { font-size: 20px; }.action-strip b,.action-strip small { display: block; }.action-strip b { font-size: 12px; }.action-strip small { margin-top: 5px; color: #858b82; font-size: 10px; }
@media (max-width: 1100px) { .stat-grid { grid-template-columns: repeat(2, 1fr); }.dashboard-grid { grid-template-columns: 1fr; }.action-strip { grid-template-columns: 1fr; }.action-strip button { border-top: 1px solid var(--pf-line); border-left: 0; } }
@media (max-width: 560px) { .stat-grid { grid-template-columns: 1fr 1fr; }.stat { min-height: 140px; padding: 17px; }.stat strong { font-size: 45px; }.focus-copy { padding: 28px; }.focus-copy h2 { font-size: 36px; } }
</style>
