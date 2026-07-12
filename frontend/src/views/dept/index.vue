<script setup>
import { ref, onMounted} from 'vue'
// 明确导入使用的 Element Plus 组件
import { ElButton, ElTable, ElTableColumn } from 'element-plus'
import axios from 'axios';
import {queryAllApi} from "@/api/dept";


//构子函数
onMounted(()=>{
  search();
})
//查询
const deptList = ref([])

const search =async() =>{
  // const result =await axios.get('https://m1.apifoxmock.com/m1/7338016-7068131-default/depts?apifoxApiId=394613379')
  // if(result.data.code ==1){//JS隐式类型转换   0-false，其他数字-true
  //   deptList.value =result.data.data
  // }

    const result =await queryAllApi();
  if(result.code ==1){//JS隐式类型转换   0-false，其他数字-true
    deptList.value =result.data
  }
}

</script>

<template>
  <h1>部门管理</h1>

  <!-- 按钮 -->
  <div class="container">
    <el-button type="primary">+ 新增部门</el-button>
  </div>

  <!-- 表格 -->
  <div class="container">
    <el-table :data="deptList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="100" align="center"/>
      <el-table-column prop="name" label="部门名称" width="260" align="center"/>
      <el-table-column prop="updateTime" label="最后操作时间" width="300" align="center"/>
      <el-table-column label="操作"  align="center">
        <template #default="scope">
          <el-button type="primary" size="small"><el-icon><EditPen /></el-icon>编辑</el-button>
          <el-button type="danger" size="small"><el-icon><DeleteFilled /></el-icon>删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- dialog对话框 -->
   
</template>

<style scoped>
.container {
  margin: 10px 0px;
}
</style>