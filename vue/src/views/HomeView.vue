<template>
    <div class="home" style="margin-left: 50px">
        <img src="../assets/home.png" height="700"/>
        <br/>
        <el-button type="primary" plain @click="book" style="margin-left: 250px">图书库存管理</el-button>
        <el-button type="primary" plain @click="borrow" style="margin-left: 50px">借书管理</el-button>
        <el-button type="primary" plain @click="card" style="margin-left: 50px">借书证管理</el-button>

    </div>
</template>

<script>
export default {
    data(){
        return{
        }
    },
    methods:{
        book(){
            this.$router.push('/book')
        },
        borrow(){
            this.$router.push('/borrow')
        },
        card(){
            this.$router.push('/card')
        }
    }
}
</script>