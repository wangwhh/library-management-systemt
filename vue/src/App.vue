<template>
    <div id="app">
        <!-- head -->
        <div style="height: 60px; line-height: 60px; background-color: white; margin-bottom: 5px">
            <img src="@/assets/logo.png" alt="" style="width: 40px; position: relative; top: 18px; left: 20px">
            <el-link to="/" underline="false" style="margin-left: 25px; font-size: 24px">图书管理系统</el-link>
        </div>

        <!-- left and body -->
        <div style="display: flex">
            <div style="width: 200px; min-height:calc(100vh - 100px); overflow: hidden; margin-right: 2px; background-color: white">
                <el-menu
                    default-active="/"
                    router class="el-menu-demo">
                    <el-menu-item index="/">
                        <i class="el-icon-house"></i>
                        <span slot="title">首页</span>
                    </el-menu-item>
                    <el-menu-item index="/book">
                        <i class="el-icon-reading"></i>
                        <span slot="title">图书库存</span>
                    </el-menu-item>
                    <el-menu-item index="/borrow">
                        <i class="el-icon-notebook-2"></i>
                        <span slot="title">借书记录</span>
                    </el-menu-item>
                    <el-menu-item index="/card">
                        <i class="el-icon-bank-card"></i>
                        <span slot="title">借书证</span>
                    </el-menu-item>
                    <el-menu-item index="/about">
                        <i class="el-icon-info"></i>
                        <span slot="title">关于</span>
                    </el-menu-item>
                </el-menu>
            </div>
            <div style="flex: 1; background-color: white">
                <router-view/>
            </div>
        </div>


    </div>
</template>
<script setup>
</script>