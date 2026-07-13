<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginForm = ref({ username: '', password: '' })

const doLogin = async () => {
  try {
    const res = await request.post('/auth/login', loginForm.value)
    if (res.data && res.data.token) {
      localStorage.setItem('pulsefit_token', res.data.token)
      localStorage.setItem('pulsefit_user', JSON.stringify(res.data))
      ElMessage.success('登录成功')
      router.push('/index')
    } else {
      ElMessage.error('用户名或密码错误')
    }
  } catch (e) {
    ElMessage.error('登录失败')
  }
}

const doReset = () => { loginForm.value = { username: '', password: '' } }
</script>

<template>
  <div id="container">
    <div class="login-form">
      <el-form label-width="80px" @submit.prevent="doLogin">
        <p class="title">PulseFit 健身俱乐部</p>
        <p style="text-align:center; color:#666; margin-bottom:20px;">运营管理中心</p>
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" @keyup.enter="doLogin"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="primary" @click="doLogin">登 录</el-button>
          <el-button class="button" type="info" @click="doReset">重 置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
#container { display: flex; align-items: center; justify-content: center; height: 100vh; background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%); }
.login-form { width: 420px; padding: 40px 30px; border-radius: 10px; background-color: white; box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3); }
.title { font-size: 26px; font-family: 'Microsoft YaHei', sans-serif; text-align: center; font-weight: bold; color: #1a1a2e; }
.button { margin-top: 20px; width: 140px; }
</style>
