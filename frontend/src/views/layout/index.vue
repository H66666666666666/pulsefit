<script setup>
import router from '../../router';


</script>

<template>
  <div class="common-layout">
    <el-container>
      <!-- Header 区域 -->
      <el-header class="header">
        <span class="title">Tlias智能学习辅助系统</span>
        <span class="right_tool">
          <a href="">
            <el-icon><EditPen /></el-icon> 修改密码 &nbsp;&nbsp;&nbsp; |  &nbsp;&nbsp;&nbsp;
          </a>
          <a href="">
            <el-icon><SwitchButton /></el-icon> 退出登录
          </a>
        </span>
      </el-header>
      
      <el-container>
        <!-- 左侧菜单 -->
        <el-aside width="200px" class="aside">
          <el-menu router="true">
            <router-link to="/index">
              <el-menu-item index="/index">
                <div style="text-align: center; width: 100%;"><el-icon><Promotion /></el-icon>首页</div>
              </el-menu-item>
            </router-link>
          <el-sub-menu index="/manage">
            <template #title>
              <div style="text-align: center; width: 100%;"><el-icon><Menu /></el-icon> 班级学员管理</div>
            </template>
           
            <el-menu-item-group>
              
              <el-menu-item index="/clazz">
                    <div style="text-align: center; width: 100%;"><el-icon><HomeFilled /></el-icon>班级管理</div>
              </el-menu-item>
              <el-menu-item index="/stu">
                    <div style="text-align: center; width: 100%;"><el-icon><UserFilled /></el-icon>学员管理</div>
              </el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
            
           
             
    
          <el-sub-menu index="/system">
            <template #title>
              <div style="text-align: center; width: 100%;"><el-icon><HelpFilled /></el-icon>系统信息管理</div>
            </template>
            <el-menu-item-group>
            
              <el-menu-item index="/dept">
                <div style="text-align: center; width: 100%;"><el-icon><HelpFilled /></el-icon>部门管理</div>
              </el-menu-item>
              <el-menu-item index="/emp">
                <div style="text-align: center; width: 100%;"><el-icon><Avatar /></el-icon>员工管理</div>
              </el-menu-item>
            </el-menu-item-group>
           
              
            
          </el-sub-menu>



            <!-- 数据统计管理 -->
  <el-sub-menu index="/report">

    <template #title>
      <div style="text-align: center; width: 100%;"><el-icon><Histogram /></el-icon>数据统计管理</div>
    </template>

​    <el-menu-item index="/empReport">
      <div style="text-align: center; width: 100%;"><el-icon><InfoFilled /></el-icon>员工信息统计</div>
    </el-menu-item>
​    <el-menu-item index="/stuReport">
      <div style="text-align: center; width: 100%;"><el-icon><Share /></el-icon>学员信息统计</div>
    </el-menu-item>
​    <el-menu-item index="/log">
​      <div style="text-align: center; width: 100%;"><el-icon><Document /></el-icon>日志信息统计</div>
​    </el-menu-item>
  </el-sub-menu>
        </el-menu>
        </el-aside>
        
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
      
    </el-container>
  </div>
</template>

<style scoped>
.header {
  background-image: linear-gradient(to right, #00547d, #007fa4, #00aaa0, #00d072, #a8eb12);
}

.title {
  color: white;
  font-size: 40px;
  font-family: 楷体;
  line-height: 60px;
  font-weight: bolder;
}

.right_tool{
  float: right;
  line-height: 60px;
}

a {
  color: white;
  text-decoration: none;
}

.aside {
  width: 220px;
  border-right: 1px solid #ccc;
  height: 730px;
}
</style>
