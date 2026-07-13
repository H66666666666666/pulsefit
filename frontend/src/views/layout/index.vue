<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const sysRole = ref('COACH')
const userName = ref('')

onMounted(() => {
  const userStr = localStorage.getItem('pulsefit_user')
  if (userStr) {
    const user = JSON.parse(userStr)
    sysRole.value = user.sysRole || 'COACH'
    userName.value = user.name || user.username
  }
})

const logout = () => {
  localStorage.removeItem('pulsefit_token')
  localStorage.removeItem('pulsefit_user')
  router.push('/login')
}

const isAdmin = () => sysRole.value === 'SUPER_ADMIN' || sysRole.value === 'ADMIN'
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <span class="title">PulseFit 健身俱乐部运营中心</span>
        <span class="right_tool">
          <span style="color:white; margin-right:12px">{{ userName }} ({{ sysRole }})</span>
          <a href="javascript:void(0)" @click="logout">退出登录</a>
        </span>
      </el-header>
      <el-container>
        <el-aside width="200px" class="aside">
          <el-menu router :default-active="$route.path" background-color="#f5f5f5">
            <el-menu-item index="/index">运营总览</el-menu-item>
            <el-sub-menu index="/member-mgmt">
              <template #title>会员管理</template>
              <el-menu-item index="/members">会员档案</el-menu-item>
              <el-menu-item index="/member-cards">会员卡管理</el-menu-item>
              <el-menu-item index="/bookings">课程预约</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="/staff-mgmt">
              <template #title>员工与部门</template>
              <el-menu-item index="/departments">运营部门</el-menu-item>
              <el-menu-item index="/staff">教练与员工</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="/course-mgmt" v-if="isAdmin()">
              <template #title>课程排班</template>
              <el-menu-item index="/courses">课程管理</el-menu-item>
              <el-menu-item index="/training-camps">训练营</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="/equipment-mgmt" v-if="isAdmin()">
              <template #title>器材管理</template>
              <el-menu-item index="/equipment">器材台账</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="/finance-mgmt" v-if="sysRole === 'SUPER_ADMIN'">
              <template #title>财务统计</template>
              <el-menu-item index="/finances">收支管理</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="/analytics-mgmt">
              <template #title>数据看板</template>
              <el-menu-item index="/analytics/staff">员工统计</el-menu-item>
              <el-menu-item index="/analytics/members">会员统计</el-menu-item>
              <el-menu-item index="/operation-logs">操作审计</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main><router-view></router-view></el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.header { background: linear-gradient(to right, #1a1a2e, #16213e, #0f3460, #e94560); display: flex; align-items: center; justify-content: space-between; padding: 0 20px; }
.title { color: white; font-size: 22px; font-family: 'Microsoft YaHei', sans-serif; line-height: 60px; font-weight: bold; }
.right_tool { display: flex; align-items: center; gap: 10px; }
a { color: white; text-decoration: none; cursor: pointer; }
.aside { min-height: calc(100vh - 60px); border-right: 1px solid #e0e0e0; }
.el-menu { border-right: none; }
</style>
